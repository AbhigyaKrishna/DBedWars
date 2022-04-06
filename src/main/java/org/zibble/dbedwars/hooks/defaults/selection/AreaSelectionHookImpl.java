package org.zibble.dbedwars.hooks.defaults.selection;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.zibble.dbedwars.api.hooks.selection.AreaSelectionHook;
import org.zibble.dbedwars.api.objects.math.BoundingBox;
import org.zibble.dbedwars.api.objects.serializable.LocationXYZ;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AreaSelectionHookImpl implements AreaSelectionHook {

    private final Map<UUID, Box> selections = Collections.synchronizedMap(new HashMap<>());

    @Override
    public LocationXYZ getFirstLocation(Player player) {
        return this.selections.getOrDefault(player.getUniqueId(), new Box()).getFirst();
    }

    @Override
    public void setFirstLocation(Player player, LocationXYZ location) {
        if (selections.containsKey(player.getUniqueId())) {
            selections.get(player.getUniqueId()).setFirst(location);
        } else {
            selections.put(player.getUniqueId(), new Box(location, null));
        }
    }

    @Override
    public LocationXYZ getSecondLocation(Player player) {
        return this.selections.getOrDefault(player.getUniqueId(), new Box()).getSecond();
    }

    @Override
    public void setSecondLocation(Player player, LocationXYZ location) {
        if (selections.containsKey(player.getUniqueId())) {
            selections.get(player.getUniqueId()).setSecond(location);
        } else {
            selections.put(player.getUniqueId(), new Box(null, location));
        }
    }

    @Override
    public @Nullable BoundingBox getSelection(Player player) {
        Box box = this.selections.getOrDefault(player.getUniqueId(), new Box());
        if (box.hasArea())
            return box.toBoundingBox();
        return null;
    }

    @Override
    public void clearSelection(Player player) {
        this.selections.remove(player.getUniqueId());
    }

    private static class Box {

        private LocationXYZ first;
        private LocationXYZ second;

        public Box(LocationXYZ first, LocationXYZ second) {
            this.first = first;
            this.second = second;
        }

        public Box() {
        }

        public LocationXYZ getFirst() {
            return first;
        }

        public void setFirst(LocationXYZ first) {
            this.first = first;
        }

        public LocationXYZ getSecond() {
            return second;
        }

        public void setSecond(LocationXYZ second) {
            this.second = second;
        }

        public boolean hasArea() {
            return first != null && second != null;
        }

        public BoundingBox toBoundingBox() {
            return new BoundingBox(first.toVector(), second.toVector());
        }

    }

}
