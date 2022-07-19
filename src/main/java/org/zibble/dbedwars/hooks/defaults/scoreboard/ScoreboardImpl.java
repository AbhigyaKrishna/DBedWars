package org.zibble.dbedwars.hooks.defaults.scoreboard;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisplayScoreboard;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateScore;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.Scoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.key.Key;
import org.zibble.dbedwars.api.version.Version;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ScoreboardImpl implements Scoreboard {

    protected static final int MAX_DISPLAY_NAME_LENGTH = 32;
    protected static final int MAX_ELEMENTS_LENGTH = 48;

    protected static final int MAX_OBJECTIVES_LENGTH = Version.SERVER_VERSION.isOlderEquals(Version.v1_12_R1) ? 16 : 1024;
    protected static final ChatColor[] COLOR_CODES = ChatColor.values();

    protected final String id;
    protected UUID uuid;
    protected Message title;
    protected List<Message> elements;
    protected boolean shown;

    public ScoreboardImpl(Player player, Message title) {
        Preconditions.checkArgument(this.getLength(title.asComponent()[0]) < MAX_DISPLAY_NAME_LENGTH && Version.SERVER_VERSION.isOlder(Version.v1_13_R1),
                "Title is longer than " + MAX_DISPLAY_NAME_LENGTH + " chars.");
        this.id = "bwboard-" + ThreadLocalRandom.current().nextInt(99999);
        this.uuid = player.getUniqueId();
        this.title = title;
        this.elements = new CopyOnWriteArrayList<>();
    }

    @Override
    public void show() {
        this.sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode.CREATE);
        this.sendDisplayObjectivePacket();
        for (int i = 0; i < this.elements.size(); i++) {
            this.sendScorePacket(i, WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM);
            this.sendTeamPacket(i, WrapperPlayServerTeams.TeamMode.CREATE);
        }
        this.shown = true;
    }

    @Override
    public void hide() {
        for (int i = 0; i < this.elements.size(); i++) {
            this.sendTeamPacket(i - 1, WrapperPlayServerTeams.TeamMode.REMOVE);
        }
        this.sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode.REMOVE);
        this.shown = false;
    }

    @Override
    public boolean isShown() {
        return this.shown;
    }

    @Override
    public Message getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(Message title) {
        this.title = title;
    }

    @Override
    public Message getLine(int index) {
        this.checkLineNumber(index, true, false);
        return this.elements.get(index);
    }

    @Override
    public void addLines(Collection<Message> lines) {
        this.checkLineNumber(this.elements.size() + lines.size(), false, true);
        if (Version.SERVER_VERSION.isOlder(Version.v1_13_R1)) {
            int lineCount = 0;
            for (Message message : elements) {
                if (message != null && this.getLength(message.asComponentWithPAPI(this.getViewer())[0]) > MAX_ELEMENTS_LENGTH) {
                    throw new IllegalArgumentException("Line " + lineCount + " is longer than 48 chars");
                }
                lineCount++;
            }
        }
        this.elements.addAll(lines);
    }

    @Override
    public void setLine(int index, Message line) {
        this.checkLineNumber(index, true, false);
        this.checkLineNumber(this.elements.size() + 1, false, true);
        if (Version.SERVER_VERSION.isOlder(Version.v1_13_R1)) {
            if (line != null && this.getLength(line.asComponentWithPAPI(this.getViewer())[0]) > MAX_ELEMENTS_LENGTH) {
                throw new IllegalArgumentException("Line `" + line + "` is longer than 40 chars");
            }
        }
        this.elements.set(index, line);
    }

    @Override
    public List<Message> getLines() {
        return Collections.unmodifiableList(this.elements);
    }

    @Override
    public void clearLines() {
        this.elements.clear();
    }

    @Override
    public void removeLine(int index) {
        this.checkLineNumber(index, true, false);
        this.elements.remove(index);
    }

    @Override
    public Player getViewer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public void update() {
        this.sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE);
        for (int i = 0; i < this.elements.size(); i++) {
            this.sendScorePacket(i, WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM);
            this.sendTeamPacket(i, WrapperPlayServerTeams.TeamMode.UPDATE);
        }
    }

    @Override
    public void refreshLines() {
        this.sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode.UPDATE);
        for (int i = this.elements.size(); i > 0; i--) {
            this.sendTeamPacket(i - 1, WrapperPlayServerTeams.TeamMode.REMOVE);
            this.sendScorePacket(i - 1, WrapperPlayServerUpdateScore.Action.REMOVE_ITEM);
        }
        for (int i = 0; i < this.elements.size(); i++) {
            this.sendScorePacket(i, WrapperPlayServerUpdateScore.Action.CREATE_OR_UPDATE_ITEM);
            this.sendTeamPacket(i, WrapperPlayServerTeams.TeamMode.CREATE);
        }
    }

    private void checkLineNumber(int line, boolean checkInRange, boolean checkMax) {
        Preconditions.checkArgument(line > 0, "Line number must be positive");
        if (checkInRange) {
            Preconditions.checkArgument(line <= this.elements.size(), String.format("Line number must be under %s", this.elements.size()));
        }
        if (checkMax) {
            Preconditions.checkArgument(line <= 15, String.format("Line number is too high: %s", line));
        }
    }

    protected int getLength(Component message) {
        return PlainTextComponentSerializer.plainText().serialize(message).length();
    }

    // ------------------------------------

    protected void sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode mode) {
        WrapperPlayServerScoreboardObjective packet = new WrapperPlayServerScoreboardObjective(this.id, mode,
                this.title.asComponentWithPAPI(this.getViewer())[0],
                WrapperPlayServerScoreboardObjective.RenderType.INTEGER);
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    protected void sendDisplayObjectivePacket() {
        WrapperPlayServerDisplayScoreboard packet = new WrapperPlayServerDisplayScoreboard(1, this.id);
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    protected void sendScorePacket(int score, WrapperPlayServerUpdateScore.Action action) {
        WrapperPlayServerUpdateScore packet = new WrapperPlayServerUpdateScore(COLOR_CODES[score].toString(), action, this.id, Optional.of(score));
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    protected void sendTeamPacket(int score, WrapperPlayServerTeams.TeamMode mode) {
        if (mode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || mode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
            throw new UnsupportedOperationException();
        }

        if (mode == WrapperPlayServerTeams.TeamMode.REMOVE) {
            WrapperPlayServerTeams packet = new WrapperPlayServerTeams(id, mode, Optional.empty());
            PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
            return;
        }

        String line = AdventureUtils.toVanillaString(this.elements.get(this.elements.size() - 1 - score).asComponentWithPAPI(this.getViewer())[0]);
        String prefix;
        String suffix = null;

        if (line.isEmpty()) {
            prefix = COLOR_CODES[score].toString() + ChatColor.RESET;
        } else if (line.length() <= MAX_OBJECTIVES_LENGTH) {
            prefix = line;
        } else {
            // Prevent splitting color codes
            int index = line.charAt(MAX_OBJECTIVES_LENGTH - 1) == ChatColor.COLOR_CHAR ? (MAX_OBJECTIVES_LENGTH - 1) : MAX_OBJECTIVES_LENGTH;
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

        if (prefix.length() > MAX_OBJECTIVES_LENGTH || (suffix != null && suffix.length() > MAX_OBJECTIVES_LENGTH)) {
            // Something went wrong, just cut to prevent client crash/kick
            prefix = prefix.substring(0, MAX_OBJECTIVES_LENGTH);
            suffix = (suffix != null) ? suffix.substring(0, MAX_OBJECTIVES_LENGTH) : null;
        }

        WrapperPlayServerTeams.ScoreBoardTeamInfo info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                Component.text(this.id + ':' + score),
                AdventureUtils.fromLegacyText(prefix),
                AdventureUtils.fromLegacyText(suffix == null ? "" : suffix),
                WrapperPlayServerTeams.NameTagVisibility.ALWAYS,
                WrapperPlayServerTeams.CollisionRule.ALWAYS,
                AdventureUtils.asNamedTextColor(COLOR_CODES[score]),
                WrapperPlayServerTeams.OptionData.NONE
        );

        WrapperPlayServerTeams packet = new WrapperPlayServerTeams(this.id + ':' + score, mode, Optional.of(info), COLOR_CODES[score].toString());
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);

//        TextComponent component = (TextComponent) this.elements.get(this.elements.size() - 1 - score).asComponentWithPAPI(this.getViewer())[0];
//        TextComponent prefix;
//        TextComponent suffix = null;
//        NamedTextColor color = AdventureUtils.asNamedTextColor(COLOR_CODES[score]);
//        int length = this.getLength(component);
//        if (component.children().isEmpty() && !component.hasStyling() && component.color() == null && Strings.isNullOrEmpty(component.content())) {
//            prefix = Component.empty().color(color);
//        } else if (length <= MAX_OBJECTIVES_LENGTH) {
//            prefix = component;
//        } else {
//            prefix = this.limitComponentSize(0, MAX_OBJECTIVES_LENGTH, component);
//            suffix = this.limitComponentSize(MAX_OBJECTIVES_LENGTH, Math.min(length, MAX_OBJECTIVES_LENGTH * 2), component);
//        }
//        suffix = suffix == null ? Component.empty().color(color) : suffix;
//
//        Debugger.debug("-----------------------------------------------------");
//        Debugger.debug("Line: " + AdventureUtils.toVanillaString(component));
//        Debugger.debug("Prefix: " + AdventureUtils.toVanillaString(prefix));
//        Debugger.debug("Suffix: " + AdventureUtils.toVanillaString(suffix));
//        Debugger.debug("-----------------------------------------------------");
//
//        WrapperPlayServerTeams.ScoreBoardTeamInfo info = new WrapperPlayServerTeams.ScoreBoardTeamInfo(
//                Component.text(this.id + ':' + score),
//                prefix.compact(),
//                suffix.compact(),
//                WrapperPlayServerTeams.NameTagVisibility.NEVER,
//                WrapperPlayServerTeams.CollisionRule.ALWAYS,
//                color,
//                WrapperPlayServerTeams.OptionData.NONE
//        );
//        WrapperPlayServerTeams packet = new WrapperPlayServerTeams(id, mode, Optional.of(info), COLOR_CODES[score].toString());
//        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

//    protected TextComponent limitComponentSize(int begin, int max, final TextComponent component) {
//        boolean legacy = Version.SERVER_VERSION.isOlderEquals(Version.v1_12_R1);
//        int length = begin;
//        TextComponent temp = component;
//        ArrayList<Component> children;
//        if (legacy) {
//            length -= this.countModifiersLength(temp);
//        }
//        if (length > 0) {
//            children = new ArrayList<>(temp.children());
//            if (temp.content().length() > length) {
//                temp = temp.content(temp.content().substring(length));
//                length = 0;
//            } else {
//                length -= temp.content().length();
//                temp = temp.content("");
//            }
//            while (length > 0 && !children.isEmpty()) {
//                TextComponent textComponent = (TextComponent) children.get(0);
//                int modifierLength = legacy ? this.countModifiersLength(textComponent) : 0;
//                int diff = length - textComponent.content().length() - modifierLength;
//                if (diff > 0) {
//                    children.remove(0);
//                    length = diff;
//                } else {
//                    children.set(0, textComponent.content(textComponent.content().substring(length - modifierLength)));
//                    length = 0;
//                }
//            }
//            temp = temp.children(children);
//        }
//
//        length = max;
//        if (legacy) {
//            length -= this.countModifiersLength(temp);
//        }
//        if (length > 0) {
//            children = new ArrayList<>(temp.children());
//            if (temp.content().length() > length) {
//                temp = temp.content(temp.content().substring(0, length));
//                length = 0;
//            } else {
//                length -= temp.content().length();
//            }
//
//            int cursor = 0;
//            while (length > 0 && cursor < children.size()) {
//                TextComponent textComponent = (TextComponent) children.get(cursor);
//                int modifierLength = legacy ? this.countModifiersLength(textComponent) : 0;
//                int diff = length - textComponent.content().length() - modifierLength;
//                if (diff > 0) {
//                    cursor++;
//                    length = diff;
//                } else {
//                    children.set(cursor, textComponent.content(textComponent.content().substring(0, length - modifierLength)));
//                    length = 0;
//                }
//            }
//            while (cursor < children.size()) {
//                children.remove(cursor);
//            }
//            temp = temp.children(children);
//        }
//
//        return temp;
//    }
//
//    protected int countModifiersLength(TextComponent component) {
//        int count = 0;
//        Style style = component.style();
//        if (style.color() != null) {
//            count += 2;
//        }
//
//        if (!style.decorations().isEmpty()) {
//            for (TextDecoration value : TextDecoration.values()) {
//                if (style.hasDecoration(value)) {
//                    count += 2;
//                }
//            }
//        }
//        return count;
//    }

    @Override
    public Key getKey() {
        return Key.of(this.id);
    }

}
