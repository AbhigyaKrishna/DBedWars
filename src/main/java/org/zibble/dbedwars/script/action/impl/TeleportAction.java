package org.zibble.dbedwars.script.action.impl;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.zibble.dbedwars.api.script.ScriptVariable;
import org.zibble.dbedwars.api.script.action.ActionTranslator;
import org.zibble.dbedwars.api.util.key.Key;

public class TeleportAction implements ActionTranslator<TeleportAction.Action> {

    @Override
    public Action serialize(String untranslated, ScriptVariable<?>... variables) {
        return null;
    }

    @Override
    public String deserialize(Action action) {
        return null;
    }

    @Override
    public Key getKey() {
        return Key.of("TELEPORT");
    }

    public static class Action implements org.zibble.dbedwars.api.script.action.Action {

        private final Entity entity;
        private final Location location;

        public Action(Location location, Entity entity) {
            this.entity = entity;
            this.location = location;
        }

        @Override
        public void execute() {
            this.getEntity().teleport(this.location);
        }

        public Entity getEntity() {
            return this.entity;
        }

        public Location getLocation() {
            return this.location;
        }

    }

}
