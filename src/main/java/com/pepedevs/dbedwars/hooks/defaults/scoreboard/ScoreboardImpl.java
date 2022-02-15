package com.pepedevs.dbedwars.hooks.defaults.scoreboard;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import com.pepedevs.dbedwars.api.adventure.AdventureUtils;
import com.pepedevs.dbedwars.api.hooks.scoreboard.Scoreboard;
import com.pepedevs.dbedwars.api.messaging.message.Message;
import com.pepedevs.dbedwars.api.util.Key;
import com.pepedevs.dbedwars.api.version.Version;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ScoreboardImpl implements Scoreboard {

    private static final int MAX_DISPLAY_NAME_LENGTH = 32;
    private static final int MAX_ELEMENTS_LENGTH = 48;
    private static final ChatColor[] COLOR_CODES = ChatColor.values();

    private final String id;
    private UUID uuid;
    private Message title;
    private List<Message> elements;
    private Message oldTitle;
    private List<Message> oldElements;
    private final Key<String> keyName;

    public ScoreboardImpl(Player player, Message title) {
        Validate.isTrue(!(title.getLines().get(0).length() > MAX_DISPLAY_NAME_LENGTH && Version.SERVER_VERSION.isOlder(Version.v1_13_R1)),
                "Title is longer than 32 chars.");
        this.id = "bwboard-" + ThreadLocalRandom.current().nextInt(99999);
        this.uuid = player.getUniqueId();
        this.title = title;
        this.oldTitle = this.title;
        this.elements = new CopyOnWriteArrayList<>();
        this.oldElements = new CopyOnWriteArrayList<>();
        this.keyName = Key.of(this.id);
    }

    public void show() {
        this.sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE);
        this.sendDisplayObjectivePacket();
        for (int i = 0; i < this.elements.size(); i++) {
            this.sendScorePacket(i, WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM);
            this.sendTeamPacket(i, WrapperPlayServerTeams.TeamMode.CREATE);
        }
    }

    public void hide(){

    }

    @Override
    public void setTitle(Message title) {
        this.title = title;
    }

    @Override
    public Message getTitle() {
        return this.title;
    }

    @Override
    public Message getLine(int index) {
        this.checkLineNumber(index, true, false);
        return this.elements.get(index);
    }

    @Override
    public void addLines(Collection<Message> lines) {
        this.checkLineNumber(this.elements.size() + elements.size(), false, true);
        if (Version.SERVER_VERSION.isOlder(Version.v1_13_R1)) {
            int lineCount = 0;
            for (Message message : elements) {
                if (message != null && this.getLength(message.asComponentWithPAPI(this.getViewer())[0]) > MAX_ELEMENTS_LENGTH) {
                    throw new IllegalArgumentException("Line " + lineCount + " is longer than 48 chars");
                }
                lineCount++;
            }
        }
        this.oldElements = new ArrayList<>(this.elements);
        this.elements.addAll(lines);
    }

    @Override
    public void setLine(int index, Message line) {
        this.checkLineNumber(index, true, false);
        this.checkLineNumber(this.elements.size() + elements.size(), false, true);
        if (Version.SERVER_VERSION.isOlder(Version.v1_13_R1)) {
            if (line != null && this.getLength(line.asComponentWithPAPI(this.getViewer())[0]) > MAX_ELEMENTS_LENGTH) {
                throw new IllegalArgumentException("Line `" + line + "` is longer than 40 chars");
            }
        }
        this.oldElements = new ArrayList<>(this.elements);
        this.elements.set(index, line);
    }

    @Override
    public List<Message> getLines() {
        return Collections.unmodifiableList(this.elements);
    }

    @Override
    public void clearLines() {
        this.oldElements = new ArrayList<>(this.elements);
        this.elements.clear();
    }

    @Override
    public void removeLine(int index) {
        this.checkLineNumber(index, true, false);
        this.oldElements = new ArrayList<>(this.elements);
        this.elements.remove(index);
    }

    @Override
    public Player getViewer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public void update() {
        this.update(true);
    }

    public void update(boolean force) {
        if (force) {
            this.sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE);
            for (int i = this.elements.size(); i > 0; i--) {
                this.sendTeamPacket(i - 1, WrapperPlayServerTeams.TeamMode.REMOVE);
                this.sendScorePacket(i - 1, WrapperPlayServerUpdateScore.Action.REMOVE_ITEM);
            }
            for (int i = 0; i < this.elements.size(); i++) {
                this.sendScorePacket(i, WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM);
                this.sendTeamPacket(i, WrapperPlayServerTeams.TeamMode.CREATE);
            }
            this.oldElements = new ArrayList<>(this.elements);
        } else {
            if (!this.oldTitle.equals(this.title))
                this.sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE);

            if (this.oldElements.size() != this.elements.size()) {
                List<Message> oldLinesCopy = new ArrayList<>(this.oldElements);

                if (this.oldElements.size() > this.elements.size()) {
                    for (int i = oldLinesCopy.size(); i > this.elements.size(); i--) {
                        this.sendTeamPacket(i - 1, WrapperPlayServerTeams.TeamMode.REMOVE);
                        this.sendScorePacket(i - 1, WrapperPlayServerUpdateScore.Action.REMOVE_ITEM);

                        this.oldElements.remove(0);
                    }
                } else {
                    for (int i = oldLinesCopy.size(); i < this.elements.size(); i++) {
                        this.sendScorePacket(i, WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM);
                        this.sendTeamPacket(i, WrapperPlayServerTeams.TeamMode.CREATE);

                        this.oldElements.add(this.oldElements.size() - i, this.elements.get(this.elements.size() - 1 - i));
                    }
                }
            }

            for (int i = 0; i < this.elements.size(); i++) {
                if (!Objects.equals(this.oldElements.get(this.oldElements.size() - 1 - i),
                        this.elements.get(this.elements.size() - 1 - i))) {
                    this.sendTeamPacket(i, WrapperPlayServerTeams.TeamMode.UPDATE);
                }
            }
        }

        this.oldTitle = title;
    }

    private void checkLineNumber(int line, boolean checkInRange, boolean checkMax) {
        Validate.isTrue(line > 0, "Line number must be positive");

        if (checkInRange && line >= this.elements.size()) {
            throw new IllegalArgumentException("Line number must be under " + this.elements.size());
        }

        if (checkMax && line >= 15) {
            throw new IllegalArgumentException("Line number is too high: " + line);
        }
    }

    private int getLength(Component message) {
        return PlainTextComponentSerializer.plainText().serialize(message).length();
    }

    // ------------------------------------

    private void sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode mode) {
        WrapperPlayServerScoreboardObjective packet = new WrapperPlayServerScoreboardObjective(this.id, mode,
                Optional.of(AdventureUtils.toVanillaString(this.title.asComponentWithPAPI(this.getViewer())[0])),
                Optional.of(WrapperPlayServerScoreboardObjective.HealthDisplay.INTEGER));
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    private void sendDisplayObjectivePacket() {
        WrapperPlayServerDisplayScoreboard packet = new WrapperPlayServerDisplayScoreboard(1, this.id);
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    private void sendScorePacket(int score, WrapperPlayServerUpdateScore.Action action) {
        WrapperPlayServerUpdateScore packet = new WrapperPlayServerUpdateScore(COLOR_CODES[score].toString(), action, this.id, Optional.of(score));
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    private void sendTeamPacket(int score, WrapperPlayServerTeams.TeamMode mode) {
        if (mode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || mode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
            throw new UnsupportedOperationException();
        }

        int maxLength = Version.SERVER_VERSION.isOlder(Version.v1_13_R1) ? 16 : 1024;

        String line = AdventureUtils.toVanillaString(this.elements.get(this.elements.size() - 1 - score).asComponentWithPAPI(this.getViewer())[0]);
        String prefix;
        String suffix = null;

        if (line.isEmpty()) {
            prefix = COLOR_CODES[score].toString() + ChatColor.RESET;
        } else if (line.length() <= maxLength) {
            prefix = line;
        } else {
            // Prevent splitting color codes
            int index = line.charAt(maxLength - 1) == ChatColor.COLOR_CHAR ? (maxLength - 1) : maxLength;
            prefix = line.substring(0, index);
            String suffixTmp = line.substring(index);
            ChatColor chatColor = null;

            if (suffixTmp.length() >= 2 && suffixTmp.charAt(0) == ChatColor.COLOR_CHAR) {
                chatColor = ChatColor.getByChar(suffixTmp.charAt(1));
            }

            String color = ChatColor.getLastColors(prefix);
            boolean addColor = chatColor == null || chatColor.isFormat();

            suffix = (addColor ? (color.isEmpty() ? ChatColor.RESET.toString() : color) : "") + suffixTmp;
        }

        if (prefix.length() > maxLength || (suffix != null && suffix.length() > maxLength)) {
            // Something went wrong, just cut to prevent client crash/kick
            prefix = prefix.substring(0, maxLength);
            suffix = (suffix != null) ? suffix.substring(0, maxLength) : null;
        }

        WrapperPlayServerTeams.ScoreBoardTeamInfo info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                AdventureSerializer.asAdventure(this.id + ':' + score),
                AdventureSerializer.asAdventure(prefix),
                AdventureSerializer.asAdventure(suffix == null ? "" : suffix),
                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                WrapperPlayServerTeams.CollisionRule.ALWAYS,
                NamedTextColor.WHITE,
                WrapperPlayServerTeams.OptionData.NONE
        );
        WrapperPlayServerTeams packet = new WrapperPlayServerTeams(id, mode, Optional.of(info), Collections.singletonList(COLOR_CODES[score].toString()));
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    @Override
    public Key<String> getKey() {
        return this.keyName;
    }
}
