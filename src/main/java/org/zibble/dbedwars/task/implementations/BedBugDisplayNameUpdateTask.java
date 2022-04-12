package org.zibble.dbedwars.task.implementations;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Silverfish;
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

public class BedBugDisplayNameUpdateTask implements Workload {

    private final Silverfish silverfish;
    private final ConfigurableCustomItems.ConfigurableBedBug cfgBedBug;
    private final Team team;
    private final DecimalFormat formatter;
    private long timestamp = System.currentTimeMillis();
    private int tick = 0;

    public BedBugDisplayNameUpdateTask(Silverfish silverfish, Team team, ConfigurableCustomItems.ConfigurableBedBug cfgBedBug) {
        this.silverfish = silverfish;
        silverfish.setCustomNameVisible(true);
        this.team = team;
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
        silverfish.setCustomName(AdventureUtils.toVanillaString(ConfigMessage.from(cfgBedBug.getBedBugDisplayName(), this.getPlaceholders(true)).asComponent())[0]);
    }

    private Component getHealthLeftString() {
        TextColor[] colors = new TextColor[2];
        String[] split = cfgBedBug.getHealthFilledColorCode().split(":");
        Color c = EnumUtil.matchEnum(Messaging.getInstance().setPlaceholders(split[0], this.getPlaceholders(false)), Color.values());
        colors[0] = c == null ? NamedTextColor.WHITE : c.getColorComponent(); // TODO: 11-04-2022 color serializer
        c = EnumUtil.matchEnum(Messaging.getInstance().setPlaceholders(split[1], this.getPlaceholders(false)), Color.values());
        colors[1] = c == null ? NamedTextColor.WHITE : c.getColorComponent();
        return (Util.getProgressBar(
                silverfish.getHealth(),
                silverfish.getMaxHealth(),
                cfgBedBug.getHealthIndicatorCount(),
                cfgBedBug.getHealthSymbol(),
                colors[0],
                colors[1]));
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
