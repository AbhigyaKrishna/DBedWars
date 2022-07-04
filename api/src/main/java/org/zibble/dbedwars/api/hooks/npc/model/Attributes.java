package org.zibble.dbedwars.api.hooks.npc.model;

import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.NPCData;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.SkinData;
import org.zibble.dbedwars.api.objects.profile.Skin;

public interface Attributes<T> {

    Attributes<Skin> SKIN = (npc, value) -> ((PlayerNPC) npc).setSkin((Skin) value);
    Attributes<SkinData> SKIN_DATA = (npc, value) -> {
        if (!(npc instanceof PlayerNPC)) return;
        ((PlayerNPC) npc).hideSkinParts(SkinData.SkinPart.values());
        ((PlayerNPC) npc).showSkinParts(((SkinData) value).getShownSkinParts());
    };
    Attributes<NPCData> NPC_DATA = (npc, value) -> {
        npc.setCrouching(((NPCData) value).isCrouched());
        npc.setOnFire(((NPCData) value).isOnFire());
    };

    void apply(BedwarsNPC npc, Object value);

}
