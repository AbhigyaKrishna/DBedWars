package org.zibble.dbedwars.hooks.defaults.npc;

import org.zibble.dbedwars.api.hooks.npc.SkinData;

public class SkinDataImpl extends SkinData {

    private boolean capeEnabled;
    private boolean jacketEnabled;
    private boolean leftSleeveEnabled;
    private boolean rightSleeveEnabled;
    private boolean leftPantsLegEnabled;
    private boolean rightPantsLegEnabled;
    private boolean hatEnabled;

    protected SkinDataImpl() {
        this.capeEnabled = true;
        this.jacketEnabled = true;
        this.leftSleeveEnabled = true;
        this.rightSleeveEnabled = true;
        this.leftPantsLegEnabled = true;
        this.rightPantsLegEnabled = true;
        this.hatEnabled = true;
    }

    @Override
    public boolean isCapeEnabled() {
        return capeEnabled;
    }

    @Override
    protected void setCapeEnabled(boolean capeEnabled) {
        this.capeEnabled = capeEnabled;
    }

    @Override
    public boolean isJacketEnabled() {
        return jacketEnabled;
    }

    @Override
    protected void setJacketEnabled(boolean jacketEnabled) {
        this.jacketEnabled = jacketEnabled;
    }

    @Override
    public boolean isLeftSleeveEnabled() {
        return leftSleeveEnabled;
    }

    @Override
    protected void setLeftSleeveEnabled(boolean leftSleeveEnabled) {
        this.leftSleeveEnabled = leftSleeveEnabled;
    }

    @Override
    public boolean isRightSleeveEnabled() {
        return rightSleeveEnabled;
    }

    @Override
    protected void setRightSleeveEnabled(boolean rightSleeveEnabled) {
        this.rightSleeveEnabled = rightSleeveEnabled;
    }

    @Override
    public boolean isLeftPantsLegEnabled() {
        return leftPantsLegEnabled;
    }

    @Override
    protected void setLeftPantsLegEnabled(boolean leftPantsLegEnabled) {
        this.leftPantsLegEnabled = leftPantsLegEnabled;
    }

    @Override
    public boolean isRightPantsLegEnabled() {
        return rightPantsLegEnabled;
    }

    @Override
    protected void setRightPantsLegEnabled(boolean rightPantsLegEnabled) {
        this.rightPantsLegEnabled = rightPantsLegEnabled;
    }

    @Override
    public boolean isHatEnabled() {
        return hatEnabled;
    }

    @Override
    protected void setHatEnabled(boolean hatEnabled) {
        this.hatEnabled = hatEnabled;
    }
}

