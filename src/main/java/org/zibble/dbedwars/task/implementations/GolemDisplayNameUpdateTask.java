package org.zibble.dbedwars.task.implementations;

import com.pepedevs.radium.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.IronGolem;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;

import java.text.DecimalFormat;

public class GolemDisplayNameUpdateTask implements Workload {

    private final IronGolem golem;
    private final ConfigurableCustomItems.ConfigurableDreamDefender cfgGolem;
    private final ChatColor teamColor;
    private final DecimalFormat formatter;
    private long timestamp = System.currentTimeMillis();
    private int tick = 0;

    public GolemDisplayNameUpdateTask(
            IronGolem golem,
            Team team,
            ConfigurableCustomItems.ConfigurableDreamDefender cfgGolem) {
        this.golem = golem;
        this.cfgGolem = cfgGolem;
        this.teamColor = team.getColor().getChatColor();
        golem.setCustomNameVisible(true);
        this.formatter = new DecimalFormat("###");
        this.formatter.setMinimumFractionDigits(1);
    }

    @Override
    public void compute() {
        if (tick >= cfgGolem.getTicksUntilDespawn()) {
            golem.setHealth(0);
            return;
        }
        golem.setCustomName(displayNameParser());
    }

    private String getTimeLeft() {
        return formatter.format((cfgGolem.getTicksUntilDespawn() - tick) / 20.0);
    }

    private String getHealthLeftString() {
        return (StringUtils.getProgressBar(
                golem.getHealth(),
                golem.getMaxHealth(),
                cfgGolem.getHealthIndicatorCount(),
                cfgGolem.getHealthSymbol(),
                ChatColor.getByChar(
                        cfgGolem.getHealthColorCodes()
                                .split(":")[0]
                                .replaceAll("%team_color%", String.valueOf(teamColor.getChar()))),
                ChatColor.getByChar(
                        cfgGolem.getHealthColorCodes()
                                .split(":")[1]
                                .replaceAll("%team_color%", String.valueOf(teamColor.getChar())))));
    }

    private String displayNameParser() {
        String s = cfgGolem.getGolemDisplayName();
        s =
                s.replaceAll("%team_color%", "&" + teamColor.toString())
                        .replaceAll("%time_left%", getTimeLeft() + "s")
                        .replaceAll("%health_bar%", getHealthLeftString());
        return StringUtils.translateAlternateColorCodes(s);
    }

    @Override
    public boolean reSchedule() {
        return golem != null && !golem.isDead();
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis() - timestamp < 50) return false;
        timestamp = System.currentTimeMillis();
        tick++;
        return golem != null && !golem.isDead();
    }

}
