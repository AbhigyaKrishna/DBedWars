package org.zibble.dbedwars.game.arena.view.upgrade;

import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.configuration.configurable.ConfigurableUpgrade;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpgradeView {

    private final Team team;
    private final ConfigurableUpgrade cfgUpgrade;
    private final String defaultPage;
    private final Map<String, UpgradePage> upgradePages;

    public UpgradeView(Team team) {
        this.team = team;
        this.cfgUpgrade = DBedwars.getInstance().getConfigHandler().getUpgrade();
        this.defaultPage = this.cfgUpgrade.getDefaultPage();
        this.upgradePages = new LinkedHashMap<>();
    }

    public void load() {
        this.cfgUpgrade
                .getPages()
                .forEach(
                        (s, page) -> {
                            UpgradePage upgradePage = new UpgradePage(s, this.team, page);
                            upgradePage.load();
                            this.upgradePages.put(s, upgradePage);
                        });
    }

    public Team getTeam() {
        return this.team;
    }

    public String getDefaultPage() {
        return this.defaultPage;
    }

    public Map<String, UpgradePage> getUpgradePages() {
        return upgradePages;
    }
}
