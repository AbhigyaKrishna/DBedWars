package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.hologram.HologramPage;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.model.Attributes;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.configuration.ConfigMessage;
import org.zibble.dbedwars.listeners.ConverseReplyHandler;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class NPCCreatorGui extends GuiComponent<ChestMenu, NPCCreatorGui> {

    public static NPCCreatorGui creator(NPCModel model) {
        return new NPCCreatorGui(model);
    }

    private final NPCModel model;
    private Optional<BedwarsNPC> npc;

    protected NPCCreatorGui(NPCModel model) {
        super(new ChestMenu(6, Component.text("NPC Creator")));
        this.model = model;
        this.npc = Optional.empty();
    }

    protected NPCCreatorGui(NPCModel model, BedwarsNPC npc) {
        this(model);
        this.npc = Optional.ofNullable(npc);
    }

    public NPCCreatorGui setupForPlayerNpc() {
        this.mask("XXXXTXXXX", "XABCOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XXXXZXXXX");

        this.item('X', BwItemStack.builder()
                .material(XMaterial.BLACK_STAINED_GLASS_PANE)
                .displayName(AdventureMessage.blank())
                .build());
        this.item('Z', BwItemStack.builder()
                .material(XMaterial.ARROW)
                .displayName(AdventureMessage.from("<aqua>Back!"))
                .build(), (protocolPlayer, clickType) ->
                NPCCreatorTypeGui.show((Player) protocolPlayer.handle()));

        this.item('A', BwItemStack.builder()
                .material(XMaterial.PAPER)
                .displayName(AdventureMessage.from("<aqua>Skin"))
                .lore(AdventureMessage.from("<gray>Click to change NPC skin!"))
                .build(), (protocolPlayer, clickType) ->
                new NPCSkinGui(this.model, (PlayerNPC) this.npc.orElse(null)).open((Player) protocolPlayer.handle()));
        this.item('B', BwItemStack.builder()
                .material(XMaterial.LEATHER_CHESTPLATE)
                .displayName(AdventureMessage.from("<aqua>Skin Parts"))
                .lore(AdventureMessage.from("<gray>Click to toggle skin parts!"))
                .build(), (protocolPlayer, clickType) ->
                new NPCSkinPartsGui(this.model, (PlayerNPC) this.npc.orElse(null)).open((Player) protocolPlayer.handle()));
        this.item('C', BwItemStack.builder()
                .material(XMaterial.NAME_TAG)
                .displayName(AdventureMessage.from("<aqua>Name"))
                .lore(AdventureMessage.from("<gray>Click to change NPC name!"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            this.close();
            Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Enter the name of the NPC!"));
            ConverseReplyHandler.INSTANCE.listenForReply(p, new Function<AsyncPlayerChatEvent, Boolean>() {
                List<String> lines = new ArrayList<>(1);

                @Override
                public Boolean apply(AsyncPlayerChatEvent reply) {
                    if (reply.getMessage().equalsIgnoreCase("exit")) {
                        NPCCreatorGui.this.model.setName(ConfigMessage.from(lines));
                        NPCCreatorGui.this.npc.ifPresent(npc -> {
                            HologramPage page = npc.getNameHologram().getHologramPages().get(0);
                            page.clearLines();
                            for (String line : lines) {
                                page.addNewTextLine(ConfigMessage.from(line));
                            }
                        });
                        NPCCreatorGui.this.open(p);
                        return true;
                    }

                    lines.add(reply.getMessage());
                    return false;
                }
            });
        });

        this.updatePlayerHead();
        return this;
    }

    void updatePlayerHead() {
        this.item('T', BwItemStack.builder()
                .material(XMaterial.PLAYER_HEAD)
                .displayName(AdventureMessage.from("<aqua>Visualize!"))
                .lore(AdventureMessage.from("<gray>Click to spawn a NPC to visualize!"))
                .meta(meta -> {
                    this.model.getAttribute(Attributes.SKIN).ifPresent(skin -> {
                        DBedwars.getInstance().getNMSAdaptor().setSkullProfile((SkullMeta) meta, PlayerGameProfile.builder()
                                .name("Bw Head")
                                .property(skin)
                                .build());
                    });
                })
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            if (this.npc.isPresent()) {
                p.teleport(this.npc.get().getLocation());
                this.npc.get().refresh(p);
                return;
            }
            this.npc = Optional.of(this.model.create(p.getLocation()));
            this.npc.get().show(player);
            this.npc.get().addClickAction((player, __) -> new NPCCreatorGui(this.model, this.npc.orElse(null)).setupForPlayerNpc().open(player));
        });
    }

    public NPCCreatorGui setupForEntityNPC() {

        return this;
    }

}
