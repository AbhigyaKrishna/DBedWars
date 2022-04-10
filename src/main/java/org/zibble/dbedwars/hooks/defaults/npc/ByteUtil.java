package org.zibble.dbedwars.hooks.defaults.npc;

import org.zibble.dbedwars.api.hooks.npc.NPCData;
import org.zibble.dbedwars.api.hooks.npc.SkinData;

public class ByteUtil {

    public static byte buildNPCDataByte(NPCData data) {
        byte a = (byte) 0x00;
        if (data.isOnFire()) a = (byte) (a | (byte) 0x01);
        if (data.isCrouched()) a = (byte) (a | (byte) 0x02);
        return a;
    }

    public static byte buildSkinDataByte(SkinData data) {
        byte a = (byte) 0x00;
        if (data.isCapeEnabled()) a = (byte) (a | (byte) 0x01);
        if (data.isJacketEnabled()) a = (byte) (a | (byte) 0x02);
        if (data.isLeftSleeveEnabled()) a = (byte) (a | (byte) 0x04);
        if (data.isRightSleeveEnabled()) a = (byte) (a | (byte) 0x08);
        if (data.isLeftPantsLegEnabled()) a = (byte) (a | (byte) 0x10);
        if (data.isRightPantsLegEnabled()) a = (byte) (a | (byte) 0x20);
        if (data.isHatEnabled()) a = (byte) (a | (byte) 0x40);
        return a;
    }

}
