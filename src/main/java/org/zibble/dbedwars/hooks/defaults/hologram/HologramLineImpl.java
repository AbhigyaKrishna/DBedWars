package org.zibble.dbedwars.hooks.defaults.hologram;

import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramLine;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;

public abstract class HologramLineImpl<C> implements HologramLine<C> {

    private final HologramManager manager;
    private final HologramPageImpl parent;
    private final int[] entityIds = new int[]{PacketUtils.getFreeEntityId(), PacketUtils.getFreeEntityId()};
    private final float height;
    private C content;

    private HologramLineImpl(HologramManager manager, HologramPageImpl parent, C content, float height) {
        this.manager = manager;
        this.parent = parent;
        this.content = content;
        this.height = height;
    }

    private HologramLineImpl(HologramManager manager, HologramPageImpl parent, C content, Height height) {
        this(manager, parent, content, height.getVal());
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
        this.manager.updateContent(this.parent.parent);
    }

    @Override
    public float getHeight() {
        return height;
    }

    public enum Height {
        TEXT(0.25F),
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

    public static class Head extends HologramLineImpl<BwItemStack> implements HologramLine.Head {

        public Head(HologramManager manager, HologramPageImpl parent, BwItemStack content) {
            super(manager, parent, content, Height.TEXT);
        }

    }

    public static class Text extends HologramLineImpl<Message> implements HologramLine.Text {

        public Text(HologramManager manager, HologramPageImpl parent, Message content) {
            super(manager, parent, content, Height.TEXT);
        }

    }

    public static class SmallHead extends HologramLineImpl<BwItemStack> implements HologramLine.SmallHead {

        public SmallHead(HologramManager manager, HologramPageImpl parent, BwItemStack content) {
            super(manager, parent, content, Height.SMALL_HEAD);
        }

    }

    public static class Icon extends HologramLineImpl<BwItemStack> implements HologramLine.Icon {

        public Icon(HologramManager manager, HologramPageImpl parent, BwItemStack content) {
            super(manager, parent, content, Height.ICON);
        }

    }

    public static class Entity extends HologramLineImpl<HologramEntityType> implements HologramLine.Entity {

        public Entity(HologramManager manager, HologramPageImpl parent, HologramEntityType content) {
            super(manager, parent, content, content.getHeight());
        }

    }

}
