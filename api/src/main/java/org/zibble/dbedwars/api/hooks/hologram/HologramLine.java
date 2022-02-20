package org.zibble.dbedwars.api.hooks.hologram;

public interface HologramLine<C> {
    
    HologramPage getParent();

    C getContent();

    void setContent(C content);

    float getHeight();
}
