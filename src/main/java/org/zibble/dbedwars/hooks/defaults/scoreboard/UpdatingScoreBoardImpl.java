package org.zibble.dbedwars.hooks.defaults.scoreboard;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.task.CancellableWorkload;
import org.zibble.dbedwars.api.util.Duration;
import org.zibble.dbedwars.api.version.Version;

import java.util.*;


public class UpdatingScoreBoardImpl extends ScoreboardImpl implements UpdatingScoreboard {

    private int titleCursor;
    private List<Integer> lineCursor;
    private Duration delay;
    private final CancellableWorkload task;

    public UpdatingScoreBoardImpl(Player player, Message title, Duration delay) {
        super(player, title);
        this.delay = delay;
        this.lineCursor = new ArrayList<>();
        this.task = new CancellableWorkload() {
            long updatedLastAt = System.currentTimeMillis();

            @Override
            public void compute() {
                updatedLastAt = System.currentTimeMillis();
                if (this.isCancelled()) return;

                if (!UpdatingScoreBoardImpl.this.isShown()) return;

                if (++UpdatingScoreBoardImpl.this.titleCursor >= UpdatingScoreBoardImpl.this.title.getLines().size()) {
                    UpdatingScoreBoardImpl.this.titleCursor = 0;
                }

                for (int i = 0; i < UpdatingScoreBoardImpl.this.getLines().size(); i++){
                    if (UpdatingScoreBoardImpl.this.lineCursor.get(i) + 1 >= UpdatingScoreBoardImpl.this.getLines().get(i).getLines().size()) {
                        UpdatingScoreBoardImpl.this.lineCursor.set(i, 0);
                    } else {
                        UpdatingScoreBoardImpl.this.lineCursor.set(i, UpdatingScoreBoardImpl.this.lineCursor.get(i) + 1);
                    }
                }
                UpdatingScoreBoardImpl.this.update();
            }

            @Override
            public boolean shouldExecute(){
                if (isCancelled())
                    return false;

                return System.currentTimeMillis() - this.updatedLastAt >= UpdatingScoreBoardImpl.this.delay.toMillis();
            }
        };
    }

    @Override
    public void setTitle(Message title) {
        super.setTitle(title);
        this.titleCursor = 0;
    }

    @Override
    public void addLines(Collection<Message> lines) {
        super.addLines(lines);
        for (Message ignored : lines) {
            this.lineCursor.add(0);
        }
    }

    @Override
    public void setLine(int index, Message line) {
        super.setLine(index, line);
        this.lineCursor.set(index, 0);
    }

    @Override
    public void removeLine(int index) {
        super.removeLine(index);
        this.lineCursor.remove(index);
    }

    @Override
    public void show() {
        super.show();
        this.task.setCancelled(false);
        this.startUpdate();
    }

    @Override
    public void hide() {
        this.cancelUpdate();
        super.hide();
    }

    @Override
    public void startUpdate() {
        this.task.setCancelled(false);
        DBedwars.getInstance().getThreadHandler().submitAsync(this.task);
    }

    @Override
    public void cancelUpdate() {
        this.task.setCancelled(true);
    }

    @Override
    public Duration getDelay() {
        return this.delay;
    }

    @Override
    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    // --------------------------------------------

    @Override
    protected void sendObjectivePacket(WrapperPlayServerScoreboardObjective.ObjectiveMode mode) {
        WrapperPlayServerScoreboardObjective packet = new WrapperPlayServerScoreboardObjective(this.id, mode,
                Optional.of(AdventureUtils.toVanillaString(this.title.asComponentWithPAPI(this.getViewer())[this.titleCursor])),
                Optional.of(WrapperPlayServerScoreboardObjective.HealthDisplay.INTEGER));
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    @Override
    protected void sendTeamPacket(int score, WrapperPlayServerTeams.TeamMode mode) {
        if (mode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || mode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
            throw new UnsupportedOperationException();
        }

        int maxLength = Version.SERVER_VERSION.isOlder(Version.v1_13_R1) ? 16 : 1024;

        String line = AdventureUtils.toVanillaString(this.elements.get(this.elements.size() - 1 - score).asComponentWithPAPI(this.getViewer())[this.lineCursor.get(this.elements.size() - 1 - score)]);
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

}
