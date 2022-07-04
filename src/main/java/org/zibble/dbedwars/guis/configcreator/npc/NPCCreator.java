package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Optional;

public class NPCCreator extends GuiComponent<ChestMenu, NPCCreator> {

    public static NPCCreator creator(NPCModel model) {
        return new NPCCreator(model);
    }

    private final NPCModel model;
    private Optional<BedwarsNPC> npc;

    protected NPCCreator(NPCModel model) {
        super(new ChestMenu(6, Component.text("NPC Creator")));
        this.model = model;
        this.npc = Optional.empty();
    }

    protected NPCCreator(NPCModel model, BedwarsNPC npc) {
        this(model);
        this.npc = Optional.ofNullable(npc);
    }

    public NPCCreator setupForPlayerNpc() {
        this.mask("XXXXTXXXX", "XABCOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XXXXZXXXX");

        this.item('X', BwItemStack.builder()
                .material(XMaterial.BLACK_STAINED_GLASS_PANE)
                .displayName(AdventureMessage.blank())
                .build());
        this.item('Z', BwItemStack.builder()
                .material(XMaterial.ARROW)
                .displayName(AdventureMessage.from("<aqua>Back!"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            NPCCreatorTypeGui.show(p);
        });

        this.item('T', BwItemStack.builder()
                .material(XMaterial.PLAYER_HEAD)
                .displayName(AdventureMessage.from("<aqua>Visualize!"))
                .lore(AdventureMessage.from("<gray>Click to spawn a NPC to visualize!"))
                .meta(meta -> {/*TODO set skin*/})
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            if (this.npc.isPresent()) {
                p.teleport(this.npc.get().getLocation());
                this.npc.get().hide(p);
                this.npc.get().show(p);
                return;
            }
            this.npc = Optional.of(this.model.create(p.getLocation()));
            this.npc.get().show(player);
            this.npc.get().addClickAction((player, __) -> new NPCCreator(this.model, this.npc.orElse(null)).setupForPlayerNpc().open(player));
        });
        this.item('A', BwItemStack.builder()
                .material(XMaterial.END_PORTAL_FRAME)
                .displayName(AdventureMessage.from("<aqua>Teleport!"))
                .lore(AdventureMessage.from("<gray>Click to teleport to the NPC!"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            this.npc.ifPresent(npc -> p.teleport(npc.getLocation()));
        });
        this.item('B', BwItemStack.builder()
                .material(XMaterial.PAPER)
                .displayName(AdventureMessage.from("<aqua>Skin"))
                .lore(AdventureMessage.from("<gray>Click to change NPC skin!"))
                .build(), (protocolPlayer, clickType) -> {
            // TODO: 02-07-2022  
        });
        this.item('C', BwItemStack.builder()
                .material(XMaterial.LEATHER_CHESTPLATE)
                .displayName(AdventureMessage.from("<aqua>Skin Parts"))
                .lore(AdventureMessage.from("<gray>Click to toggle skin parts!"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            new NPCSkinPartsGui(this.model, (PlayerNPC) this.npc.orElse(null)).open(p);
        });

        return this;
    }

    public NPCCreator setupForEntityNPC() {

        return this;
    }

}
