package org.zibble.dbedwars.game.arena.spawner;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.game.spawner.ResourceItem;
import org.zibble.dbedwars.api.util.BwItemStack;
import org.zibble.dbedwars.api.util.SchedulerUtils;

public class ResourceItemImpl implements ResourceItem {

    private final BwItemStack item;
    private boolean mergeable = true;
    private boolean splittable;

    private Item itemEntity;

    public ResourceItemImpl(BwItemStack item) {
        this.item = item;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public BwItemStack getItem() {
        return item.clone();
    }

    @Override
    public void drop(Location location) {
        if (this.isDropped()) {
            throw new IllegalStateException("Tried to drop item which is already dropped on location `" + this.itemEntity.getLocation() + "` !");
        }

        if (!Bukkit.isPrimaryThread()) {
            SchedulerUtils.runTask(() -> {
                this.dropItem(location);
            });
        } else {
            this.dropItem(location);
        }
    }

    private void dropItem(Location location) {
        this.itemEntity = location.getWorld().dropItem(location, item.asItemStack());
        this.itemEntity.setMetadata("resource", new FixedMetadataValue(DBedwars.getInstance(), this));
        this.itemEntity.setMetadata("merge", new FixedMetadataValue(DBedwars.getInstance(), mergeable));
        this.itemEntity.setMetadata("split", new FixedMetadataValue(DBedwars.getInstance(), splittable));
    }

    @Override
    public void remove() {
        this.itemEntity.remove();
    }

    @Override
    @Nullable
    public Item getItemEntity() {
        return this.itemEntity;
    }

    @Override
    public boolean isDropped() {
        return this.itemEntity != null;
    }

    @Override
    public boolean isMergeable() {
        return this.mergeable;
    }

    @Override
    public void setMergeable(boolean mergeable) {
        this.mergeable = mergeable;
    }

    @Override
    public boolean isSplittable() {
        return this.splittable;
    }

    @Override
    public void setSplittable(boolean splittable) {
        this.splittable = splittable;
    }

    public static class Builder implements org.zibble.dbedwars.api.util.mixin.Builder<ResourceItemImpl> {

        private BwItemStack item;
        private boolean mergeable = true;
        private boolean splittable;

        protected Builder() {
        }

        public Builder item(ItemStack item) {
            this.item = new BwItemStack(item);
            return this;
        }

        public Builder item(BwItemStack item) {
            this.item = item;
            return this;
        }

        public Builder mergeable(boolean mergeable) {
            this.mergeable = mergeable;
            return this;
        }

        public Builder splittable(boolean splittable) {
            this.splittable = splittable;
            return this;
        }

        @Override
        public ResourceItemImpl build() {
            ResourceItemImpl resourceItem = new ResourceItemImpl(item);
            resourceItem.setMergeable(mergeable);
            resourceItem.setSplittable(splittable);
            return resourceItem;
        }

    }

}
