package com.pepedevs.dbedwars.hooks.defaults.hologram;

import com.pepedevs.dbedwars.api.hooks.hologram.HologramLine;
import com.pepedevs.dbedwars.api.hooks.hologram.HologramPage;

public class HologramLineImpl<C> implements HologramLine<C> {

    private final HologramPageImpl parent;
    private final int[] entityIds = new int[]{PacketUtils.getFreeEntityId(), PacketUtils.getFreeEntityId()};
    private C content;

    private final int height;

    public HologramLineImpl(HologramPageImpl parent, C content, int height) {
        this.parent = parent;
        this.content = content;
        this.height = height;
    }

    @Override
    public HologramPage getParent() {
        return parent;
    }

    public int[] getEntityIds() {
        return entityIds;
    }

    @Override
    public C getContent() {
        return content;
    }

    @Override
    public void setContent(C content) {
        this.content = content;
    }

    public int getHeight() {
        return height;
    }

}