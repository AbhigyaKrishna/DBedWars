package org.zibble.dbedwars.api.hooks.npc;

import java.util.ArrayList;
import java.util.List;

public class SkinData {

    private boolean capeEnabled;
    private boolean jacketEnabled;
    private boolean leftSleeveEnabled;
    private boolean rightSleeveEnabled;
    private boolean leftPantsLegEnabled;
    private boolean rightPantsLegEnabled;
    private boolean hatEnabled;

    public SkinData() {
        this.capeEnabled = true;
        this.jacketEnabled = true;
        this.leftSleeveEnabled = true;
        this.rightSleeveEnabled = true;
        this.leftPantsLegEnabled = true;
        this.rightPantsLegEnabled = true;
        this.hatEnabled = true;
    }

    public boolean isCapeEnabled() {
        return capeEnabled;
    }

    public void setCapeEnabled(boolean capeEnabled) {
        this.capeEnabled = capeEnabled;
    }

    public boolean isJacketEnabled() {
        return jacketEnabled;
    }

    public void setJacketEnabled(boolean jacketEnabled) {
        this.jacketEnabled = jacketEnabled;
    }

    public boolean isLeftSleeveEnabled() {
        return leftSleeveEnabled;
    }

    public void setLeftSleeveEnabled(boolean leftSleeveEnabled) {
        this.leftSleeveEnabled = leftSleeveEnabled;
    }

    public boolean isRightSleeveEnabled() {
        return rightSleeveEnabled;
    }

    public void setRightSleeveEnabled(boolean rightSleeveEnabled) {
        this.rightSleeveEnabled = rightSleeveEnabled;
    }

    public boolean isLeftPantsLegEnabled() {
        return leftPantsLegEnabled;
    }

    public void setLeftPantsLegEnabled(boolean leftPantsLegEnabled) {
        this.leftPantsLegEnabled = leftPantsLegEnabled;
    }

    public boolean isRightPantsLegEnabled() {
        return rightPantsLegEnabled;
    }

    public void setRightPantsLegEnabled(boolean rightPantsLegEnabled) {
        this.rightPantsLegEnabled = rightPantsLegEnabled;
    }

    public boolean isHatEnabled() {
        return hatEnabled;
    }

    public void setHatEnabled(boolean hatEnabled) {
        this.hatEnabled = hatEnabled;
    }

    public SkinData.SkinPart[] getShownSkinParts() {
        List<SkinPart> shownSkinParts = new ArrayList<>();
        if (this.isCapeEnabled()) shownSkinParts.add(SkinData.SkinPart.CAPE);
        if (this.isJacketEnabled()) shownSkinParts.add(SkinData.SkinPart.JACKET);
        if (this.isLeftSleeveEnabled()) shownSkinParts.add(SkinData.SkinPart.LEFT_SLEEVE);
        if (this.isRightSleeveEnabled()) shownSkinParts.add(SkinData.SkinPart.RIGHT_SLEEVE);
        if (this.isLeftPantsLegEnabled()) shownSkinParts.add(SkinData.SkinPart.LEFT_PANTS);
        if (this.isRightPantsLegEnabled()) shownSkinParts.add(SkinData.SkinPart.RIGHT_PANTS);
        if (this.isHatEnabled()) shownSkinParts.add(SkinData.SkinPart.HAT);
        return shownSkinParts.toArray(new SkinData.SkinPart[0]);
    }

    public enum SkinPart {
        CAPE,
        JACKET,
        LEFT_SLEEVE,
        RIGHT_SLEEVE,
        LEFT_PANTS,
        RIGHT_PANTS,
        HAT
    }

}
