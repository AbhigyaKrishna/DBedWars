package org.zibble.dbedwars.game.arena;

import org.zibble.dbedwars.api.game.Team;
import org.zibble.dbedwars.game.arena.view.upgrade.UpgradeItem;

import java.util.Set;
import java.util.function.Consumer;

public class Upgrade implements org.zibble.dbedwars.api.game.Upgrade {

    private String id;
    private String tier;
    private Team team;
    private Set<Consumer<Team>> permanentActions;

    public Upgrade(String id, Team team, UpgradeItem.UpgradeTier tier) {
        this.id = id;
        this.team = team;
        this.tier = tier.getKey();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getTier() {
        return this.tier;
    }

    @Override
    public Team getTeam() {
        return this.team;
    }

    @Override
    public Set<Consumer<Team>> getPermanentActions() {
        return this.permanentActions;
    }

    @Override
    public String toString() {
        return "Upgrade{" +
                "id='" + id + '\'' +
                ", tier='" + tier + '\'' +
                ", team=" + team +
                ", permanentActions=" + permanentActions +
                '}';
    }
}
