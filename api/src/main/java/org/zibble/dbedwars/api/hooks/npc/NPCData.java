package org.zibble.dbedwars.api.hooks.npc;

public class NPCData {

    private boolean isOnFire;
    private boolean isCrouched;

    public NPCData() {
        this.isOnFire = false;
        this.isCrouched = false;
    }

    public boolean isOnFire() {
        return isOnFire;
    }

    protected void setOnFire(boolean onFire) {
        isOnFire = onFire;
    }

    public boolean isCrouched() {
        return isCrouched;
    }

    protected void setCrouched(boolean crouched) {
        isCrouched = crouched;
    }

}
