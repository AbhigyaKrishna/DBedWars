package org.zibble.dbedwars.api.objects.hologram;

import org.bukkit.Location;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramEntityType;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.util.mixin.ClickAction;
import org.zibble.dbedwars.api.objects.serializable.Duration;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HologramModel implements Cloneable {

    protected Collection<ModelLine> lines;
    protected Duration updateInterval;
    protected Hologram hologram;
    protected final Set<ClickAction> clickActions;

    public HologramModel(Collection<ModelLine> lines, Duration updateInterval) {
        this.lines = lines;
        this.updateInterval = updateInterval;
        this.clickActions = new HashSet<>();
    }

    public Collection<ModelLine> getLines() {
        return lines;
    }

    public Duration getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Duration updateInterval) {
        this.updateInterval = updateInterval;
    }

    public Set<ClickAction> getClickActions() {
        return clickActions;
    }

    public Hologram createHologram(Location location) {
        this.hologram = DBedWarsAPI.getApi().getHookManager().getHologramFactory().createHologram(location);
        this.hologram.setUpdateInterval(this.updateInterval);
        HologramPage page = hologram.addPage();
        for (ModelLine line : this.lines) {
            line.addTo(page);
        }
        this.hologram.setClickRegistered(!this.clickActions.isEmpty());
        for (ClickAction clickAction : this.clickActions) {
            this.hologram.addClickAction(clickAction);
        }
        return hologram;
    }

    public Hologram getHologram() {
        return this.hologram;
    }

    public boolean exist() {
        return this.hologram != null;
    }

    public Location getLocation() {
        return this.hologram.getLocation();
    }

    @Override
    public HologramModel clone() {
        HologramModel hologramModel = new HologramModel(this.lines, this.updateInterval);
        hologramModel.getClickActions().addAll(this.clickActions);
        return hologramModel;
    }

    @Override
    public String toString() {
        return "HologramModel{" +
                "lines=" + lines +
                ", updateInterval=" + updateInterval +
                ", hologram=" + hologram +
                '}';
    }

    public static abstract class ModelLine {

        public static ModelLine ofText(Message message) {
            return new ModelLine() {
                @Override
                public void addTo(HologramPage page) {
                    page.addNewTextLine(message);
                }
            };
        }

        public static ModelLine ofHead(BwItemStack item) {
            return new ModelLine() {
                @Override
                public void addTo(HologramPage page) {
                    page.addNewHeadLine(item);
                }
            };
        }

        public static ModelLine ofSmallHead(BwItemStack item) {
            return new ModelLine() {
                @Override
                public void addTo(HologramPage page) {
                    page.addNewSmallHeadLine(item);
                }
            };
        }

        public static ModelLine ofItem(BwItemStack item) {
            return new ModelLine() {
                @Override
                public void addTo(HologramPage page) {
                    page.addNewIconLine(item);
                }
            };
        }

        public static ModelLine ofEntity(HologramEntityType entityType) {
            return new ModelLine() {
                @Override
                public void addTo(HologramPage page) {
                    page.addNewEntityLine(entityType);
                }
            };
        }

        public abstract void addTo(HologramPage page);

    }

}
