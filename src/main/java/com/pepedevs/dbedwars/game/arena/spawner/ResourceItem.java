package com.pepedevs.dbedwars.game.arena.spawner;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.util.BwItemStack;
import com.pepedevs.dbedwars.api.util.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Nullable;

public class ResourceItem implements com.pepedevs.dbedwars.api.game.spawner.ResourceItem {

    private final BwItemStack item;
    private boolean mergeable = true;
    private boolean splittable;

    private Item itemEntity;

    public static Builder builder() {
        return new Builder();
    }

    public ResourceItem(BwItemStack item) {
        this.item = item;
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
            SchedulerUtils.runTask(new Runnable() {
                @Override
                public void run() {
                    ResourceItem.this.itemEntity = location.getWorld().dropItem(location, item.toItemStack());
                    ResourceItem.this.itemEntity.setMetadata("merge", new FixedMetadataValue(DBedwars.getInstance(), mergeable));
                    ResourceItem.this.itemEntity.setMetadata("split", new FixedMetadataValue(DBedwars.getInstance(), splittable));
                }
            });
        } else {
            this.itemEntity = location.getWorld().dropItem(location, item.toItemStack());
            this.itemEntity.setMetadata("merge", new FixedMetadataValue(DBedwars.getInstance(), mergeable));
            this.itemEntity.setMetadata("split", new FixedMetadataValue(DBedwars.getInstance(), splittable));
        }
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

    public static class Builder implements com.pepedevs.dbedwars.api.util.Builder<ResourceItem> {

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
        public ResourceItem build() {
            ResourceItem resourceItem = new ResourceItem(item);
            resourceItem.setMergeable(mergeable);
            resourceItem.setSplittable(splittable);
            return resourceItem;
        }

    }

}
