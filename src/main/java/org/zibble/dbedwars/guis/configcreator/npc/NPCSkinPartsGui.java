package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.guis.MenuPlayer;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.SkinData;
import org.zibble.dbedwars.api.hooks.npc.model.Attributes;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.api.objects.serializable.Duration;
import org.zibble.dbedwars.guis.reflection.*;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Optional;
import java.util.function.BiConsumer;

public class NPCSkinPartsGui extends ReflectiveGui {

    public static final NPCSkinPartsGui INSTANCE = new NPCSkinPartsGui();

    @Item('X')
    private final BwItemStack background = BwItemStack.builder()
            .material(XMaterial.BLACK_STAINED_GLASS_PANE)
            .displayName(AdventureMessage.blank())
            .build();

    @Item('Z')
    private final BwItemStack back = BwItemStack.builder()
            .material(XMaterial.ARROW)
            .displayName(AdventureMessage.from("<aqua>Back"))
            .lore(AdventureMessage.from("<gray>Click to go back!"))
            .build();

    @ClickAction('Z')
    @Dynamic
    void handleBackButton(MenuPlayer player, NPCModel model, PlayerNPC npc) {
        new NPCCreatorGui(model, npc).setupForPlayerNpc().open(player.handle());
    }

    public void postInit(NPCModel model, PlayerNPC npc, GuiComponent<ChestMenu, GuiComponent> menu) {
        this.updateButtons(model, npc, menu);
    }

    public void updateButtons(NPCModel model, PlayerNPC playerNPC, GuiComponent<ChestMenu, GuiComponent> menu) {
        Optional<PlayerNPC> npc = Optional.ofNullable(playerNPC);
        SkinData data = model.getAttribute(Attributes.SKIN_DATA).orElseGet(() -> {
            SkinData value = new SkinData();
            model.addAttribute(Attributes.SKIN_DATA, value);
            return value;
        });
        menu.item('A', BwItemStack.builder()
                .material(data.isCapeEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Cape"))
                .lore(AdventureMessage.from("<gray>Click to toggle cape!"))
                .build(), (protocolPlayer, clickType) ->
                this.updatePart(!data.isCapeEnabled(), SkinData::setCapeEnabled, data, npc, menu, model));
        menu.item('B', BwItemStack.builder()
                .material(data.isHatEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Hat"))
                .lore(AdventureMessage.from("<gray>Click to toggle hat!"))
                .build(), (protocolPlayer, clickType) ->
                this.updatePart(!data.isHatEnabled(), SkinData::setHatEnabled, data, npc, menu, model));
        menu.item('C', BwItemStack.builder()
                .material(data.isJacketEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Jacket"))
                .lore(AdventureMessage.from("<gray>Click to toggle jacket!"))
                .build(), (protocolPlayer, clickType) ->
                this.updatePart(!data.isJacketEnabled(), SkinData::setJacketEnabled, data, npc, menu, model));
        menu.item('D', BwItemStack.builder()
                .material(data.isLeftSleeveEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Left Sleeve"))
                .lore(AdventureMessage.from("<gray>Click to toggle left sleeve!"))
                .build(), (protocolPlayer, clickType) ->
                this.updatePart(!data.isLeftSleeveEnabled(), SkinData::setLeftSleeveEnabled, data, npc, menu, model));
        menu.item('E', BwItemStack.builder()
                .material(data.isRightSleeveEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Right Sleeve"))
                .lore(AdventureMessage.from("<gray>Click to toggle right sleeve!"))
                .build(), (protocolPlayer, clickType) ->
                this.updatePart(!data.isRightSleeveEnabled(), SkinData::setRightSleeveEnabled, data, npc, menu, model));
        menu.item('F', BwItemStack.builder()
                .material(data.isLeftPantsLegEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Left Pants Leg"))
                .lore(AdventureMessage.from("<gray>Click to toggle left pants leg!"))
                .build(), (protocolPlayer, clickType) ->
                this.updatePart(!data.isLeftPantsLegEnabled(), SkinData::setLeftPantsLegEnabled, data, npc, menu, model));
        menu.item('G', BwItemStack.builder()
                .material(data.isRightPantsLegEnabled() ? XMaterial.GREEN_WOOL : XMaterial.RED_WOOL)
                .displayName(AdventureMessage.from("<aqua>Right Pants Leg"))
                .lore(AdventureMessage.from("<gray>Click to toggle right pants leg!"))
                .build(), (protocolPlayer, clickType) ->
                this.updatePart(!data.isRightPantsLegEnabled(), SkinData::setRightPantsLegEnabled, data, npc, menu, model));
    }

    private void updatePart(boolean bool, BiConsumer<SkinData, Boolean> consumer, SkinData data, Optional<PlayerNPC> npc, GuiComponent<ChestMenu, GuiComponent> menu, NPCModel model) {
        consumer.accept(data, bool);
        npc.ifPresent(n -> {
            consumer.accept(n.getSkinData(), bool);
            n.updateNPCData();
        });
        this.updateButtons(model, npc.orElse(null), menu);
        ActionFuture.runAsync(menu::update, Duration.ofTicks(5));
    }

    @Override
    protected GuiComponent<ChestMenu, GuiComponent> provide(Argument<?>... args) {
        ChestMenu menu = new ChestMenu(3, Component.text("NPC Skin Parts"));
        GuiComponent<ChestMenu, GuiComponent> component = GuiComponent.creator(menu);
        component.mask("XXXXXXXXX", "XABCDEFGX", "XXXXZXXXX");
        return component;
    }

}
