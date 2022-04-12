package org.zibble.dbedwars.task.implementations;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.IronGolem;
import org.zibble.dbedwars.api.adventure.AdventureUtils;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.api.messaging.placeholders.Placeholder;
import org.zibble.dbedwars.api.messaging.placeholders.PlaceholderEntry;
import org.zibble.dbedwars.api.task.Workload;
import org.zibble.dbedwars.api.util.Color;
import org.zibble.dbedwars.api.util.EnumUtil;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.configuration.configurable.ConfigurableCustomItems;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.dbedwars.utils.ConfigurationUtil;
import org.zibble.dbedwars.utils.Util;

import java.text.DecimalFormat;

public class GolemDisplayNameUpdateTask implements Workload {

    private final IronGolem golem;
    private final ConfigurableCustomItems.ConfigurableDreamDefender cfgGolem;
    private final Team team;
    private final DecimalFormat formatter;
    private long timestamp = System.currentTimeMillis();
    private int tick = 0;

    public GolemDisplayNameUpdateTask(IronGolem golem, Team team, ConfigurableCustomItems.ConfigurableDreamDefender cfgGolem) {
        this.golem = golem;
        this.cfgGolem = cfgGolem;
        this.team = team;
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
        golem.setCustomName(AdventureUtils.toVanillaString(ConfigMessage.from(cfgGolem.getGolemDisplayName(), this.getPlaceholders(true)).asComponent())[0]);
    }

    private String getTimeLeft() {
        return formatter.format((cfgGolem.getTicksUntilDespawn() - tick) / 20.0);
    }

    private Component getHealthLeftString() {
        TextColor[] colors = new TextColor[2];
        String[] split = cfgGolem.getHealthColorCodes().split(":");
        Color c = EnumUtil.matchEnum(Messaging.getInstance().setPlaceholders(split[0], this.getPlaceholders(false)), Color.values());
        colors[0] = c == null ? NamedTextColor.WHITE : c.getColorComponent(); // TODO: 11-04-2022 color serializer
        c = EnumUtil.matchEnum(Messaging.getInstance().setPlaceholders(split[1], this.getPlaceholders(false)), Color.values());
        colors[1] = c == null ? NamedTextColor.WHITE : c.getColorComponent();
        return (Util.getProgressBar(
                golem.getHealth(),
                golem.getMaxHealth(),
                cfgGolem.getHealthIndicatorCount(),
                cfgGolem.getHealthSymbol(),
                colors[0],
                colors[1]));
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

    private Placeholder[] getPlaceholders(boolean healthBarPlaceholder) {
        Placeholder[] placeholders = new Placeholder[healthBarPlaceholder ? 3 : 2];
        placeholders[0] = PlaceholderEntry.symbol("team_color", () -> ConfigurationUtil.getConfigCode(this.team.getColor())
                .replace("&", "")
                .replace("<", "")
                .replace(">", ""));
        placeholders[1] = PlaceholderEntry.symbol("time_left", this::getTimeLeft);
        if (healthBarPlaceholder) {
            placeholders[2] = PlaceholderEntry.symbol("health_bar", () -> AdventureUtils.toVanillaString(this.getHealthLeftString().asComponent()));
        }
        return placeholders;
    }

}
