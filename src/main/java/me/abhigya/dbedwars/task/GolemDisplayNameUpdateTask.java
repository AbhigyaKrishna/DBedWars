package me.abhigya.dbedwars.task;

import me.Abhigya.core.util.tasks.Workload;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.configuration.configurable.ConfigurableCustomItems;
import me.abhigya.dbedwars.game.arena.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.IronGolem;

public class GolemDisplayNameUpdateTask implements Workload {

    IronGolem golem;
    ConfigurableCustomItems.ConfigurableDreamDefender cfgGolem;
    ChatColor teamColor;

    public GolemDisplayNameUpdateTask(DBedwars plugin, IronGolem golem, Team team){
        this.golem = golem;
        this.cfgGolem = plugin.getConfigHandler().getCustomItems().getDreamDefender();
        this.teamColor = team.getColor().getChatColor();
        golem.setCustomNameVisible(true);
    }

    private long timestamp = System.currentTimeMillis();
    private int tick = 0;

    @Override
    public void compute() {
        golem.setCustomName(displayNameParser());
        if (tick>=cfgGolem.getTicksUntilDespawn())
            golem.setHealth(0);
    }

    private double getTimeLeft(){
        return (cfgGolem.getTicksUntilDespawn()-tick)/20;
    }

    private String getHealthLeftString(String healthSymbol,int healthIndicatorCount,String color1,String color2){
        String returnString = "";
        int k = 0;
        for (int i = 0; i < ((int) (golem.getHealth() / golem.getMaxHealth()))*cfgGolem.getHealthIndicatorCount(); i++) {
            returnString = returnString.concat(color1 + healthSymbol + " ");
            k++;
        }
        for (int i = k; i < healthIndicatorCount; i++) {
            returnString = returnString.concat(color2 + healthSymbol + " ");
        }
        return returnString;
    }

    private String displayNameParser(){
        String s = cfgGolem.getGolemDisplayName();
        s = s
                .replaceAll("%team_color%","&" + teamColor.toString())
                .replaceAll("%time_left%", getTimeLeft() + "s")
                .replaceAll("%health_bar%",
                        getHealthLeftString(cfgGolem.getHealthSymbol(),
                                cfgGolem.getHealthIndicatorCount(),
                                cfgGolem.getHealthColorCodes().split(":")[0],
                                cfgGolem.getHealthColorCodes().split(":")[1]));
        return s;
    }

    @Override
    public boolean reSchedule() {
        return golem != null && !golem.isDead();
    }

    @Override
    public boolean shouldExecute() {
        if (System.currentTimeMillis()-timestamp<50)
            return false;
        timestamp = System.currentTimeMillis();
        tick++;
        return golem != null && !golem.isDead();
    }
}
