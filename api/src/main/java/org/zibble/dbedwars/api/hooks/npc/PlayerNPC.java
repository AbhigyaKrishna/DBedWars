package org.zibble.dbedwars.api.hooks.npc;

import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.util.Skin;

import java.util.ArrayList;
import java.util.List;

public interface PlayerNPC extends BedwarsNPC{

    ActionFuture<PlayerNPC> setSkin(Skin skin);

    ActionFuture<PlayerNPC> hideNameTag();

    ActionFuture<PlayerNPC> showNameTag();

    ActionFuture<PlayerNPC> showInTab();

    ActionFuture<PlayerNPC> hideFromTab();

    SkinData getSkinData();

    default ActionFuture<PlayerNPC> showSkinParts(SkinData.SkinPart... skinParts) {
        for (SkinData.SkinPart part : skinParts) {
            switch (part) {
                case CAPE: {
                    this.getSkinData().setCapeEnabled(true);
                    continue;
                }
                case JACKET: {
                    this.getSkinData().setJacketEnabled(true);
                    continue;
                }
                case LEFT_SLEEVE: {
                    this.getSkinData().setLeftSleeveEnabled(true);
                    continue;
                }
                case RIGHT_SLEEVE: {
                    this.getSkinData().setRightSleeveEnabled(true);
                    continue;
                }
                case LEFT_PANTS: {
                    this.getSkinData().setLeftPantsLegEnabled(true);
                    continue;
                }
                case RIGHT_PANTS: {
                    this.getSkinData().setRightPantsLegEnabled(true);
                    continue;
                }
                case HAT_ENABLED: {
                    this.getSkinData().setHatEnabled(true);
                }
            }
        }
        return this.updateSkinData();
    }

    default ActionFuture<PlayerNPC> hideSkinParts(SkinData.SkinPart... skinParts) {
        for (SkinData.SkinPart part : skinParts) {
            switch (part) {
                case CAPE: {
                    this.getSkinData().setCapeEnabled(false);
                    continue;
                }
                case JACKET: {
                    this.getSkinData().setJacketEnabled(false);
                    continue;
                }
                case LEFT_SLEEVE: {
                    this.getSkinData().setLeftSleeveEnabled(false);
                    continue;
                }
                case RIGHT_SLEEVE: {
                    this.getSkinData().setRightSleeveEnabled(false);
                    continue;
                }
                case LEFT_PANTS: {
                    this.getSkinData().setLeftPantsLegEnabled(false);
                    continue;
                }
                case RIGHT_PANTS: {
                    this.getSkinData().setRightPantsLegEnabled(false);
                    continue;
                }
                case HAT_ENABLED: {
                    this.getSkinData().setHatEnabled(false);
                }
            }
        }
        return this.updateSkinData();
    }

    default SkinData.SkinPart[] getShownSkinParts() {
        List<SkinData.SkinPart> shownSkinParts = new ArrayList<>();
        if (this.getSkinData().isCapeEnabled()) shownSkinParts.add(SkinData.SkinPart.CAPE);
        if (this.getSkinData().isJacketEnabled()) shownSkinParts.add(SkinData.SkinPart.JACKET);
        if (this.getSkinData().isLeftSleeveEnabled()) shownSkinParts.add(SkinData.SkinPart.LEFT_SLEEVE);
        if (this.getSkinData().isRightSleeveEnabled()) shownSkinParts.add(SkinData.SkinPart.RIGHT_SLEEVE);
        if (this.getSkinData().isLeftPantsLegEnabled()) shownSkinParts.add(SkinData.SkinPart.LEFT_PANTS);
        if (this.getSkinData().isRightPantsLegEnabled()) shownSkinParts.add(SkinData.SkinPart.RIGHT_PANTS);
        if (this.getSkinData().isHatEnabled()) shownSkinParts.add(SkinData.SkinPart.HAT_ENABLED);
        return shownSkinParts.toArray(new SkinData.SkinPart[0]);
    }

    ActionFuture<PlayerNPC> updateSkinData();

}
