package org.zibble.dbedwars.script.action.impl.hologram;

import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.script.action.Action;

import java.util.function.Consumer;

public class HologramAction implements Action<Hologram> {

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

    @Override
    public Hologram getHandle() {
        return this.hologram;
    }

}
