package org.zibble.dbedwars.task.implementations;

import com.pepedevs.radium.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Silverfish;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;

import java.text.DecimalFormat;

public class BedBugDisplayNameUpdateTask implements Workload {

    private final Silverfish silverfish;
    private final ConfigurableCustomItems.ConfigurableBedBug cfgBedBug;
    private final ChatColor teamColor;
    private final DecimalFormat formatter;
    private long timestamp = System.currentTimeMillis();
    private int tick = 0;

    public BedBugDisplayNameUpdateTask(
            Silverfish silverfish,
            Team team,
            ConfigurableCustomItems.ConfigurableBedBug cfgBedBug) {
        this.silverfish = silverfish;
        silverfish.setCustomNameVisible(true);
        this.teamColor = team.getColor().getChatColor();
        this.cfgBedBug = cfgBedBug;
        this.formatter = new DecimalFormat("###.#");
        this.formatter.setMinimumFractionDigits(1);
    }

    @Override
    public void compute() {
        if (tick >= cfgBedBug.getTicksUntilDespawn()) {
            silverfish.setHealth(0);
            return;
        }
        silverfish.setCustomName(displayNameParser());
    }

    private String getHealthLeftString() {
        return (StringUtils.getProgressBar(
                silverfish.getHealth(),
                silverfish.getMaxHealth(),
                cfgBedBug.getHealthIndicatorCount(),
                cfgBedBug.getHealthSymbol(),
                ChatColor.getByChar(cfgBedBug
                        .getHealthFilledColorCode()
                        .replace("%team_color%", String.valueOf(teamColor.getChar()))),
                ChatColor.getByChar(cfgBedBug
                        .getHealthMissingColorCode()
                        .split(":")[1]
                        .replace("%team_color%", String.valueOf(teamColor.getChar())))));
    }

    private String displayNameParser() {
        String s = cfgBedBug.getBedBugDisplayName();
        s = s.replace("%team_color%", "&" + teamColor.toString())
                .replace("%time_left%", getTimeLeft() + "s")
                .replace("%health_bar%", getHealthLeftString());
        return StringUtils.translateAlternateColorCodes(s);
    }

    private String getTimeLeft() {
        return formatter.format((cfgBedBug.getTicksUntilDespawn() - tick) / 20.0);
    }

    @Override
    public boolean reSchedule() {
        return silverfish != null && !silverfish.isDead();
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis() - timestamp < 50) return false;
        timestamp = System.currentTimeMillis();
        tick++;
        return silverfish != null && !silverfish.isDead();
    }
}
