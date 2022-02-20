package org.zibble.dbedwars.utils;

import com.pepedevs.radium.utils.xseries.XMaterial;

public enum ItemConstant {
    SWORD(
            XMaterial.WOODEN_SWORD,
            XMaterial.IRON_SWORD,
            XMaterial.GOLDEN_SWORD,
            XMaterial.DIAMOND_SWORD,
            XMaterial.NETHERITE_SWORD),

    AXE(
            XMaterial.WOODEN_AXE,
            XMaterial.IRON_AXE,
            XMaterial.GOLDEN_AXE,
            XMaterial.DIAMOND_AXE,
            XMaterial.NETHERITE_AXE),

    PICKAXE(
            XMaterial.WOODEN_PICKAXE,
            XMaterial.IRON_PICKAXE,
            XMaterial.GOLDEN_PICKAXE,
            XMaterial.DIAMOND_PICKAXE,
            XMaterial.NETHERITE_PICKAXE),

    SHOVEL(
            XMaterial.WOODEN_SHOVEL,
            XMaterial.IRON_SHOVEL,
            XMaterial.GOLDEN_SHOVEL,
            XMaterial.DIAMOND_SHOVEL,
            XMaterial.NETHERITE_SHOVEL),

    HOE(
            XMaterial.WOODEN_HOE,
            XMaterial.IRON_HOE,
            XMaterial.GOLDEN_HOE,
            XMaterial.DIAMOND_HOE,
            XMaterial.NETHERITE_HOE),

    TOOLS(
            Utils.mergeArray(
                    AXE.getItems(),
                    PICKAXE.getItems(),
                    SHOVEL.getItems(),
                    HOE.getItems(),
                    new XMaterial[] {XMaterial.SHEARS})),

    LEATHER_ARMOR(
            XMaterial.LEATHER_HELMET,
            XMaterial.LEATHER_CHESTPLATE,
            XMaterial.LEATHER_LEGGINGS,
            XMaterial.LEATHER_BOOTS),

    CHAINMAIL_ARMOR(
            XMaterial.CHAINMAIL_HELMET,
            XMaterial.CHAINMAIL_CHESTPLATE,
            XMaterial.CHAINMAIL_LEGGINGS,
            XMaterial.CHAINMAIL_BOOTS),

    IRON_ARMOR(
            XMaterial.IRON_HELMET,
            XMaterial.IRON_CHESTPLATE,
            XMaterial.IRON_LEGGINGS,
            XMaterial.IRON_BOOTS),

    GOLDEN_ARMOR(
            XMaterial.GOLDEN_HELMET,
            XMaterial.GOLDEN_CHESTPLATE,
            XMaterial.GOLDEN_LEGGINGS,
            XMaterial.GOLDEN_BOOTS),

    DIAMOND_ARMOR(
            XMaterial.DIAMOND_HELMET,
            XMaterial.DIAMOND_CHESTPLATE,
            XMaterial.DIAMOND_LEGGINGS,
            XMaterial.DIAMOND_BOOTS),

    NETHERITE_ARMOR(
            XMaterial.NETHERITE_HELMET,
            XMaterial.NETHERITE_CHESTPLATE,
            XMaterial.NETHERITE_LEGGINGS,
            XMaterial.NETHERITE_BOOTS),

    ARMOR(
            Utils.mergeArray(
                    LEATHER_ARMOR.getItems(),
                    CHAINMAIL_ARMOR.getItems(),
                    IRON_ARMOR.getItems(),
                    GOLDEN_ARMOR.getItems(),
                    DIAMOND_ARMOR.getItems(),
                    NETHERITE_ARMOR.getItems())),

    BED(
            XMaterial.RED_BED,
            XMaterial.BLACK_BED,
            XMaterial.BLUE_BED,
            XMaterial.BROWN_BED,
            XMaterial.CYAN_BED,
            XMaterial.GRAY_BED,
            XMaterial.GREEN_BED,
            XMaterial.LIGHT_BLUE_BED,
            XMaterial.LIGHT_GRAY_BED,
            XMaterial.LIME_BED,
            XMaterial.MAGENTA_BED,
            XMaterial.ORANGE_BED,
            XMaterial.PINK_BED,
            XMaterial.PURPLE_BED,
            XMaterial.WHITE_BED,
            XMaterial.YELLOW_BED),
    ;

    private final XMaterial[] items;

    ItemConstant(XMaterial... items) {
        this.items = items;
    }

    public XMaterial[] getItems() {
        return this.items;
    }
}
