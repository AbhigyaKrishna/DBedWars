package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

public class NPCCreatorTypeGui extends GuiComponent<ChestMenu, NPCCreatorTypeGui> {

    public static NPCCreatorTypeGui show(Player player) {
        return new NPCCreatorTypeGui().open(player);
    }

    protected NPCCreatorTypeGui() {
        super(new ChestMenu(3, Component.text("NPC Creator")));

        this.mask("AAAAOBBBB", "AAXAOBYBB", "AAAAOBBBB");

        this.item('A', BwItemStack.builder()
                .material(XMaterial.ORANGE_STAINED_GLASS_PANE)
                .displayName(AdventureMessage.blank())
                .build());
        this.item('B', BwItemStack.builder()
                .material(XMaterial.PURPLE_STAINED_GLASS_PANE)
                .displayName(AdventureMessage.blank())
                .build());
        this.item('O', BwItemStack.builder()
                .material(XMaterial.LIME_STAINED_GLASS_PANE)
                .displayName(AdventureMessage.blank())
                .build());
        this.item('X', BwItemStack.builder()
                .material(XMaterial.PLAYER_HEAD)
                .displayName(AdventureMessage.from("<yellow>Player npc"))
                .lore(AdventureMessage.from("<gray>Click to create a player npc"))
                .build(), (protocolPlayer, clickType) -> NPCCreatorGui.creator(new NPCModel()).setupForPlayerNpc().open((Player) protocolPlayer.handle()));
        this.item('Y', BwItemStack.builder()
                .material(XMaterial.ZOMBIE_HEAD)
                .displayName(AdventureMessage.from("<yellow>Entity npc"))
                .lore(AdventureMessage.from("<gray>Click to create a entity npc"))
                .build(), (protocolPlayer, clickType) -> NPCEntityTypeGui.show((Player) protocolPlayer.handle()));
    }

}
