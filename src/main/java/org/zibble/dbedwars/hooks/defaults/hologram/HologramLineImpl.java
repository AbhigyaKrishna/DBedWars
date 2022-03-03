package org.zibble.dbedwars.hooks.defaults.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;

import java.util.UUID;

public abstract class HologramLineImpl<C> implements HologramLine<C> {

    private final HologramPageImpl parent;
    private final int[] entityIds = new int[]{PacketUtils.getFreeEntityId(), PacketUtils.getFreeEntityId()};
    private C content;

    private final float height;

    private HologramLineImpl(HologramPageImpl parent, C content, float height) {
        this.parent = parent;
        this.content = content;
        this.height = height;
    }

    private HologramLineImpl(HologramPageImpl parent, C content, Height height) {
        this.parent = parent;
        this.content = content;
        this.height = height.getVal();
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
            HologramFactoryImpl.getInstance().updateContent((HologramImpl) this.getParent(), player);
        }
        this.content = content;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public enum Height {
        TEXT(0.5F),
        HEAD(2.0F),
        SMALL_HEAD(1.1875F),
        ICON(0.55F),
        ;

        private final float height;

        Height(float height) {
            this.height = height;
        }

        public float getVal() {
            return height;
        }
    }

    public static class Head extends HologramLineImpl<ItemStack> implements HologramLine.Head {
        public Head(HologramPageImpl parent, ItemStack content) {
            super(parent, content, Height.TEXT);
        }
    }

    public static class Text extends HologramLineImpl<Component> implements HologramLine.Text {
        public Text(HologramPageImpl parent, Component content) {
            super(parent, content, Height.TEXT);
        }
    }

    public static class SmallHead extends HologramLineImpl<ItemStack> implements HologramLine.SmallHead {
        public SmallHead(HologramPageImpl parent, ItemStack content) {
            super(parent, content, Height.SMALL_HEAD);
        }
    }

    public static class Icon extends HologramLineImpl<ItemStack> implements HologramLine.Icon {
        public Icon(HologramPageImpl parent, ItemStack content) {
            super(parent, content, Height.ICON);
        }
    }

    public static class Entity extends HologramLineImpl<HologramEntityType> implements HologramLine.Entity {
        public Entity(HologramPageImpl parent, HologramEntityType content) {
            super(parent, content, content.getHeight());
        }
    }
}
