package com.pepedevs.dbedwars.hooks.defaults.hologram;

import com.pepedevs.dbedwars.api.hooks.hologram.HologramLine;
import com.pepedevs.dbedwars.api.hooks.hologram.HologramPage;

public class HologramLineImpl<C> implements HologramLine<C> {

    private final HologramPageImpl parent;
    private final int[] entityIds = new int[]{PacketUtils.getFreeEntityId(), PacketUtils.getFreeEntityId()};
    private C content;

    private int index;
    private final int height;

    public HologramLineImpl(HologramPageImpl parent, C content, int height, int index) {
        this.parent = parent;
        this.content = content;
        this.height = height;
        this.index = index;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
