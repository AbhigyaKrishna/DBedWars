package org.zibble.dbedwars.hooks.citizens;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.SkinData;
import org.zibble.dbedwars.api.objects.profile.Skin;

public class PlayerNPCImpl extends BedWarsNPCImpl implements PlayerNPC {

    private final SkinData skinData;

    public PlayerNPCImpl(CitizensHook hook, Location location) {
        super(hook, location);
        this.skinData = new SkinData();
    }

    @Override
    public ActionFuture<PlayerNPC> setSkin(Skin skin) {
        this.getCitizensNPC().getOrAddTrait(CitizensAPI.getTraitFactory().getTrait("skintrait").getClass());

        return null;
    }

    @Override
    public ActionFuture<PlayerNPC> setSkin(ActionFuture<Skin> skin) {
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
    public ActionFuture<PlayerNPC> showInTab(Player... players) {
        //TODO
        return null;
    }

    @Override
    public ActionFuture<PlayerNPC> hideFromTab(Player... players) {
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
