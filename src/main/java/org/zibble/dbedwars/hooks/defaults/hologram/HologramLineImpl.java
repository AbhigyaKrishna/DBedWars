package org.zibble.dbedwars.hooks.defaults.hologram;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;

import java.util.UUID;

public class HologramLineImpl<C> implements HologramLine<C> {

    private final HologramPageImpl parent;
    private final int[] entityIds = new int[]{PacketUtils.getFreeEntityId(), PacketUtils.getFreeEntityId()};
    private C content;

    private final float height;

    public HologramLineImpl(HologramPageImpl parent, C content, float height) {
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
        for (UUID uuid : this.parent.getParent().getViewerPages().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            HologramManager.getInstance().updateContent((HologramImpl) this.getParent(), player);
        }
        this.content = content;
    }

    @Override
    public float getHeight() {
        return height;
    }

}
