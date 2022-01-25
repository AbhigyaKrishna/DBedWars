package com.pepedevs.dbedwars.api.npc;

import com.pepedevs.dbedwars.api.util.BedwarsAction;

import java.util.function.Consumer;

public abstract class NPCBedwarsAction implements BedwarsAction<BedwarsNPC> {

    @Override
    public BedwarsAction<BedwarsNPC> then(BedwarsAction<BedwarsNPC> action) {
        return null;
    }

    @Override
    public void executeAsync() {

    }

    @Override
    public void executeAsync(Consumer<? super BedwarsNPC> success) {

    }

    @Override
    public BedwarsAction<BedwarsNPC> delay(int ticks) {
        return null;
    }
}
