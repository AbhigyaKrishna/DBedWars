package org.zibble.dbedwars.api.hooks.npc;

import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.objects.profile.Skin;

public interface PlayerNPC extends BedwarsNPC {

    ActionFuture<PlayerNPC> setSkin(Skin skin);

    ActionFuture<PlayerNPC> setSkin(ActionFuture<Skin> skin);

    ActionFuture<PlayerNPC> hideNameTag();

    ActionFuture<PlayerNPC> showNameTag();

    ActionFuture<PlayerNPC> showInTab(Player... players);

    ActionFuture<PlayerNPC> hideFromTab(Player... players);

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
                case HAT: {
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
                case HAT: {
                    this.getSkinData().setHatEnabled(false);
                }
            }
        }
        return this.updateSkinData();
    }

    ActionFuture<PlayerNPC> updateSkinData();

}
