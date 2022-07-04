package org.zibble.dbedwars.script.action.impl.hologram;

import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.script.action.Action;

import java.util.function.Consumer;

public class HologramAction implements Action {

    private final Hologram hologram;
    private final Consumer<Hologram> consumer;

    public HologramAction(Hologram hologram, Consumer<Hologram> consumer) {
        this.hologram = hologram;
        this.consumer = consumer;
    }

    @Override
    public void execute() {
        this.consumer.accept(this.hologram);
    }

    public Hologram getHologram() {
        return this.hologram;
    }

}
