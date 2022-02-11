package com.pepedevs.dbedwars.api.npc;

public abstract class NPCData {

    public abstract boolean isOnFire();

    protected abstract void setOnFire(boolean onFire);

    public abstract boolean isCrouched();

    protected abstract void setCrouched(boolean isCrouched);

    protected abstract byte buildByte();

}
