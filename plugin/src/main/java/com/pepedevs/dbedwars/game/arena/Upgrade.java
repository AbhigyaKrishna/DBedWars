package com.pepedevs.dbedwars.game.arena;

import com.pepedevs.dbedwars.api.game.Team;
import com.pepedevs.dbedwars.game.arena.view.upgrade.UpgradeItem;

import java.util.Set;
import java.util.function.Consumer;

public class Upgrade implements com.pepedevs.dbedwars.api.game.Upgrade {

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
}
