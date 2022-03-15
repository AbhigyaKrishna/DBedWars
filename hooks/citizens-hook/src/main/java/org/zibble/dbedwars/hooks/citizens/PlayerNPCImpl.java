package org.zibble.dbedwars.hooks.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.npc.NPCData;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.SkinData;
import org.zibble.dbedwars.api.objects.profile.Skin;

public class PlayerNPCImpl extends BedwarsNPCImpl implements PlayerNPC {

    private final SkinData skinData;

    public PlayerNPCImpl(Location location, NPCData npcData, SkinData skinData, Component name) {
        super(location, npcData, name);
        this.skinData = skinData;
    }

    @Override
    public ActionFuture<PlayerNPC> setSkin(Skin skin) {
        this.getCitizensNPC().getOrAddTrait(CitizensAPI.getTraitFactory().getTrait("skintrait").getClass());

        return null;
    }

    @Override
    public ActionFuture<PlayerNPC> hideNameTag() {
        //TODO
        return null;
    }

    @Override
    public ActionFuture<PlayerNPC> showNameTag() {
        //TODO
        return null;
    }

    @Override
    public ActionFuture<PlayerNPC> showInTab() {
        //TODO
        return null;
    }

    @Override
    public ActionFuture<PlayerNPC> hideFromTab() {
        //TODO
        return null;
    }

    @Override
    public SkinData getSkinData() {
        return this.skinData;
    }

    @Override
    public ActionFuture<PlayerNPC> updateSkinData() {
        //TODO
        return null;
    }

    @Override
    public NPC createNPC() {
        return CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "");
    }
}
