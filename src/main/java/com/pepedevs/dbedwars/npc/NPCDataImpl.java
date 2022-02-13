package com.pepedevs.dbedwars.npc;

import com.pepedevs.dbedwars.api.npc.NPCData;

public class NPCDataImpl extends NPCData {

    private boolean isOnFire;
    private boolean isCrouched;

    protected NPCDataImpl() {
        this.isOnFire = false;
        this.isCrouched = false;
    }

    @Override
    public byte buildByte() {
        byte a = (byte) 0x00;
        if (isOnFire) a = (byte) (a | (byte) 0x01);
        if (isCrouched) a = (byte) (a | (byte) 0x02);
        return a;
    }

    @Override
    public boolean isOnFire() {
        return isOnFire;
    }

    @Override
    protected void setOnFire(boolean onFire) {
        isOnFire = onFire;
    }

    @Override
    public boolean isCrouched() {
        return isCrouched;
    }

    @Override
    protected void setCrouched(boolean crouched) {
        isCrouched = crouched;
    }

}
