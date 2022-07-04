package org.zibble.dbedwars.api.hooks.npc.model;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.DBedWarsAPI;
import org.zibble.dbedwars.api.hooks.hologram.Hologram;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.messaging.message.Message;
import org.zibble.dbedwars.api.util.mixin.ClickAction;

import java.util.*;

public class NPCModel {

    private final Type type;
    private EntityType entityType;
    private Message name;
    private final Map<Attributes<?>, Object> attributes = new HashMap<>();
    private final Set<ClickAction> clickActions = new HashSet<>();

    public NPCModel() {
        this.type = Type.PLAYER;
    }

    public NPCModel(EntityType entityType) {
        this.type = Type.ENTITY;
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public Message getName() {
        return name;
    }

    public void setName(Message name) {
        this.name = name;
    }

    public <T> Optional<T> getAttribute(Attributes<T> attribute) {
        return Optional.ofNullable(attributes.get(attribute) == null ? null : (T) attributes.get(attribute));
    }

    public <T> void addAttribute(Attributes<T> attribute, T value) {
        this.attributes.put(attribute, value);
    }

    public void addClickAction(ClickAction clickAction) {
        this.clickActions.add(clickAction);
    }

    public BedwarsNPC create(Location location) {
        BedwarsNPC npc;
        if (this.type == Type.PLAYER) {
            npc = DBedWarsAPI.getApi().getHookManager().getNpcFactory().createPlayerNPC(location);
        } else {
            npc = DBedWarsAPI.getApi().getHookManager().getNpcFactory().createEntityNPC(location, this.entityType);
        }
        this.applyTo(npc);
        return npc;
    }

    public void applyTo(BedwarsNPC npc) {
        Hologram holo = npc.getNameHologram();
        HologramPage hologramPage = holo.addPage();
        for (Message message : this.name.splitToLineMessage()) {
            hologramPage.addNewTextLine(message);
        }
        for (Map.Entry<Attributes<?>, Object> entry : this.attributes.entrySet()) {
            entry.getKey().apply(npc, entry.getValue());
        }
        for (ClickAction clickAction : this.clickActions) {
            npc.addClickAction(clickAction);
        }
    }

    public enum Type {

        PLAYER,
        ENTITY,
        ;

    }

}
