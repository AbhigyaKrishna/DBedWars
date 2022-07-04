package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.SkinData;
import org.zibble.dbedwars.api.hooks.npc.model.Attributes;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Optional;

public class NPCSkinPartsGui extends GuiComponent<ChestMenu, NPCSkinPartsGui> {

    private final NPCModel model;
    private final Optional<PlayerNPC> npc;

    protected NPCSkinPartsGui(NPCModel model, PlayerNPC npc) {
        super(new ChestMenu(3, Component.text("NPC Skin Parts")));
        this.model = model;
        this.npc = Optional.ofNullable(npc);
        this.setUp();
    }

    public void setUp() {
        this.mask("XXXXXXXXX", "XABCDEFGX", "XXXXZXXXX");

        this.item('X', BwItemStack.builder()
                .material(XMaterial.BLACK_STAINED_GLASS_PANE)
                .displayName(AdventureMessage.blank())
                .build());
        this.item('Z', BwItemStack.builder()
                .material(XMaterial.ARROW)
                .displayName(AdventureMessage.from("<aqua>Back"))
                .lore(AdventureMessage.from("<gray>Click to go back!"))
                .build(), (protocolPlayer, clickType) -> new NPCCreator(this.model, this.npc.orElse(null)).setupForPlayerNpc().open((Player) protocolPlayer.handle()));
        this.updateButtons();
    }

    public void updateButtons() {
        SkinData data = model.getAttribute(Attributes.SKIN_DATA).orElseGet(() -> {
            SkinData value = new SkinData();
            model.addAttribute(Attributes.SKIN_DATA, value);
            return value;
        });
        this.item('A', BwItemStack.builder()
                .material(data.isCapeEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Cape"))
                .lore(AdventureMessage.from("<gray>Click to toggle cape!"))
                .build(), (protocolPlayer, clickType) -> {
            data.setCapeEnabled(!data.isCapeEnabled());
            this.npc.ifPresent(npc -> {
                npc.getSkinData().setCapeEnabled(data.isCapeEnabled());
                npc.updateNPCData();
            });
            this.updateButtons();
            this.update();
        });
        this.item('B', BwItemStack.builder()
                .material(data.isHatEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Hat"))
                .lore(AdventureMessage.from("<gray>Click to toggle hat!"))
                .build(), (protocolPlayer, clickType) -> {
            data.setHatEnabled(!data.isHatEnabled());
            this.npc.ifPresent(npc -> {
                npc.getSkinData().setHatEnabled(data.isHatEnabled());
                npc.updateNPCData();
            });
            this.updateButtons();
            this.update();
        });
        this.item('C', BwItemStack.builder()
                .material(data.isJacketEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Jacket"))
                .lore(AdventureMessage.from("<gray>Click to toggle jacket!"))
                .build(), (protocolPlayer, clickType) -> {
            data.setJacketEnabled(!data.isJacketEnabled());
            this.npc.ifPresent(npc -> {
                npc.getSkinData().setJacketEnabled(data.isJacketEnabled());
                npc.updateNPCData();
            });
            this.updateButtons();
            this.update();
        });
        this.item('D', BwItemStack.builder()
                .material(data.isLeftSleeveEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Left Sleeve"))
                .lore(AdventureMessage.from("<gray>Click to toggle left sleeve!"))
                .build(), (protocolPlayer, clickType) -> {
            data.setLeftSleeveEnabled(!data.isLeftSleeveEnabled());
            this.npc.ifPresent(npc -> {
                npc.getSkinData().setLeftSleeveEnabled(data.isLeftSleeveEnabled());
                npc.updateNPCData();
            });
            this.updateButtons();
            this.update();
        });
        this.item('E', BwItemStack.builder()
                .material(data.isRightSleeveEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Right Sleeve"))
                .lore(AdventureMessage.from("<gray>Click to toggle right sleeve!"))
                .build(), (protocolPlayer, clickType) -> {
            data.setRightSleeveEnabled(!data.isRightSleeveEnabled());
            this.npc.ifPresent(npc -> {
                npc.getSkinData().setRightSleeveEnabled(data.isRightSleeveEnabled());
                npc.updateNPCData();
            });
            this.updateButtons();
            this.update();
        });
        this.item('F', BwItemStack.builder()
                .material(data.isLeftPantsLegEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Left Pants Leg"))
                .lore(AdventureMessage.from("<gray>Click to toggle left pants leg!"))
                .build(), (protocolPlayer, clickType) -> {
            data.setLeftPantsLegEnabled(!data.isLeftPantsLegEnabled());
            this.npc.ifPresent(npc -> {
                npc.getSkinData().setLeftPantsLegEnabled(data.isLeftPantsLegEnabled());
                npc.updateNPCData();
            });
            this.updateButtons();
            this.update();
        });
        this.item('G', BwItemStack.builder()
                .material(data.isRightPantsLegEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Right Pants Leg"))
                .lore(AdventureMessage.from("<gray>Click to toggle right pants leg!"))
                .build(), (protocolPlayer, clickType) -> {
            data.setRightPantsLegEnabled(!data.isRightPantsLegEnabled());
            this.npc.ifPresent(npc -> {
                npc.getSkinData().setRightPantsLegEnabled(data.isRightPantsLegEnabled());
                npc.updateNPCData();
            });
            this.updateButtons();
            this.update();
        });
    }

}
