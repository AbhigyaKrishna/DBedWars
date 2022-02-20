package org.zibble.dbedwars.hooks.citizens.npc;

import com.pepedevs.dbedwars.api.hooks.npc.NPCData;

public class NPCDataImpl extends NPCData {

    private boolean isOnFire;
    private boolean isCrouched;

    protected NPCDataImpl() {
        this.isOnFire = false;
        this.isCrouched = false;
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

