package com.pepedevs.dbedwars.task;

import com.pepedevs.dbedwars.api.messaging.PlaceholderEntry;
import com.pepedevs.dbedwars.api.util.Color;
import com.pepedevs.radium.placeholders.Placeholder;
import com.pepedevs.radium.task.Workload;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Silverfish;

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
                Color.from()
                ChatColor.getByChar(cfgBedBug
                        .getHealthFilledColorCode()
                        .replace("%team_color%", String.valueOf(teamColor.getChar()))),
                ChatColor.getByChar(cfgBedBug
                        .getHealthColorCodes()
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
