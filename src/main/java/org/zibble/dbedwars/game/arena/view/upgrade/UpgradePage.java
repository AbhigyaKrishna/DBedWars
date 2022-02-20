package org.zibble.dbedwars.game.arena.view.upgrade;

import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.configuration.configurable.ConfigurableUpgrade;
import org.zibble.dbedwars.utils.ConfigurationUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpgradePage {

    private final String key;
    private Team team;
    private ConfigurableUpgrade.ConfigurableUpgradePage page;
    private String[][] pattern;
    private Map<String, UpgradeItem> items;

    public UpgradePage(String key, Team team, ConfigurableUpgrade.ConfigurableUpgradePage page) {
        this.key = key;
        this.team = team;
        this.page = page;
        this.items = new LinkedHashMap<>();
    }

    public void load() {
        this.pattern = ConfigurationUtils.parseGuiPattern(this.page.getPattern());
        this.page
                .getItems()
                .forEach(
                        (s, i) -> {
                            UpgradeItem item = new UpgradeItem(this.team, i, s, this);
                            this.items.put(s, item);
                        });
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return this.page.getTitle();
    }
}