package org.zibble.dbedwars.hooks.citizens.npc;

import com.pepedevs.dbedwars.api.future.ActionFuture;
import com.pepedevs.dbedwars.api.hooks.npc.NPCData;
import com.pepedevs.dbedwars.api.hooks.npc.PlayerNPC;
import com.pepedevs.dbedwars.api.hooks.npc.SkinData;
import com.pepedevs.dbedwars.api.util.Skin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class PlayerNPCImpl extends BedwarsNPCImpl implements PlayerNPC {

    private final SkinData skinData;

    public PlayerNPCImpl(Location location, NPCData npcData, SkinData skinData) {
        super(location, npcData);
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
