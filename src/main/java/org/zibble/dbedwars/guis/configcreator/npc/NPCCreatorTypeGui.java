package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.guis.MenuPlayer;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.guis.reflection.Argument;
import org.zibble.dbedwars.guis.reflection.ClickAction;
import org.zibble.dbedwars.guis.reflection.Item;
import org.zibble.dbedwars.guis.reflection.ReflectiveGui;
import org.zibble.inventoryframework.ClickType;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

public class NPCCreatorTypeGui extends ReflectiveGui {

    public static final NPCCreatorTypeGui INSTANCE = new NPCCreatorTypeGui();

    @Item('A')
    private final BwItemStack orange_bg = BwItemStack.builder()
            .material(XMaterial.ORANGE_STAINED_GLASS_PANE)
            .displayName(AdventureMessage.blank())
            .build();

    @Item('B')
    private final BwItemStack purple_bg = BwItemStack.builder()
            .material(XMaterial.PURPLE_STAINED_GLASS_PANE)
            .displayName(AdventureMessage.blank())
            .build();

    @Item('O')
    private final BwItemStack lime_bg = BwItemStack.builder()
            .material(XMaterial.LIME_STAINED_GLASS_PANE)
            .displayName(AdventureMessage.blank())
            .build();

    @Item('X')
    private final BwItemStack player_npc = BwItemStack.builder()
            .material(XMaterial.PLAYER_HEAD)
            .displayName(AdventureMessage.from("<yellow>Player npc"))
            .lore(AdventureMessage.from("<gray>Click to create a player npc"))
            .build();

    @Item('Y')
    private final BwItemStack entityNpc = BwItemStack.builder()
            .material(XMaterial.ZOMBIE_HEAD)
            .displayName(AdventureMessage.from("<yellow>Entity npc"))
            .lore(AdventureMessage.from("<gray>Click to create a entity npc"))
            .build();

    public static GuiComponent<ChestMenu, GuiComponent> show(Player player) {
        return INSTANCE.get().open(player);
    }

    @Override
    protected GuiComponent<ChestMenu, GuiComponent> provide(Argument<?>... args) {
        ChestMenu menu = new ChestMenu(3, Component.text("NPC Creator"));
        GuiComponent<ChestMenu, GuiComponent> component = GuiComponent.creator(menu);
        component.mask("AAAAOBBBB", "AAXAOBYBB", "AAAAOBBBB");
        return component;
    }

    @ClickAction('X')
    void handlePlayerNpc(MenuPlayer player, ClickType clickType) {
        NPCCreatorGui.creator(new NPCModel()).setupForPlayerNpc().open(player.handle());
    }

    @ClickAction('Y')
    void handleEntityNpc(MenuPlayer player, ClickType clickType) {
        NPCEntityTypeGui.show(player.handle());
    }

}
