package com.pepedevs.dbedwars.api.npc;

public abstract class SkinData {

    public abstract boolean isCapeEnabled();

    protected abstract void setCapeEnabled(boolean enabled);

    public abstract boolean isJacketEnabled();

    protected abstract void setJacketEnabled(boolean enabled);

    public abstract boolean isLeftSleeveEnabled();

    protected abstract void setLeftSleeveEnabled(boolean enabled);

    public abstract boolean isRightSleeveEnabled();

    protected abstract void setRightSleeveEnabled(boolean enabled);

    public abstract boolean isLeftPantsLegEnabled();

    protected abstract void setLeftPantsLegEnabled(boolean enabled);

    public abstract boolean isRightPantsLegEnabled();

    protected abstract void setRightPantsLegEnabled(boolean enabled);

    public abstract boolean isHatEnabled();

    protected abstract void setHatEnabled(boolean enabled);

    public abstract byte buildByte();

    public enum SkinPart {
        CAPE,
        JACKET,
        LEFT_SLEEVE,
        RIGHT_SLEEVE,
        LEFT_PANTS,
        RIGHT_PANTS,
        HAT_ENABLED
    }
}
