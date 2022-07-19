package org.zibble.dbedwars.hooks.defaults.scoreboard;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.hooks.scoreboard.UpdatingScoreboard;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.api.task.CancellableWorkload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UpdatingScoreBoardImpl extends ScoreboardImpl implements UpdatingScoreboard {

    private final CancellableWorkload task;
    private int titleCursor;
    private List<Integer> lineCursor;
    private Duration delay;

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

                for (int i = 0; i < UpdatingScoreBoardImpl.this.getLines().size(); i++) {
                    if (UpdatingScoreBoardImpl.this.lineCursor.get(i) + 1 >= UpdatingScoreBoardImpl.this.getLines().get(i).getLines().size()) {
                        UpdatingScoreBoardImpl.this.lineCursor.set(i, 0);
                    } else {
                        UpdatingScoreBoardImpl.this.lineCursor.set(i, UpdatingScoreBoardImpl.this.lineCursor.get(i) + 1);
                    }
                }
                UpdatingScoreBoardImpl.this.update();
            }

            @Override
            public boolean shouldExecute() {
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
                this.title.asComponentWithPAPI(this.getViewer())[this.titleCursor].compact(),
                WrapperPlayServerScoreboardObjective.RenderType.INTEGER);
        PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
    }

    @Override
    protected void sendTeamPacket(int score, WrapperPlayServerTeams.TeamMode mode) {
        if (mode == WrapperPlayServerTeams.TeamMode.ADD_ENTITIES || mode == WrapperPlayServerTeams.TeamMode.REMOVE_ENTITIES) {
            throw new UnsupportedOperationException();
        }

        if (mode == WrapperPlayServerTeams.TeamMode.REMOVE) {
            WrapperPlayServerTeams packet = new WrapperPlayServerTeams(id, mode, Optional.empty());
            PacketEvents.getAPI().getPlayerManager().sendPacket(this.getViewer(), packet);
            return;
        }

        String line = AdventureUtils.toVanillaString(this.elements.get(this.elements.size() - 1 - score).asComponentWithPAPI(this.getViewer())[this.lineCursor.get(this.elements.size() - 1 - score)]);
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
    }

}
