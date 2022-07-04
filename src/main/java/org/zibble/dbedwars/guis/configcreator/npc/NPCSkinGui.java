package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Strings;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.model.Attributes;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.profile.Skin;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.io.GameProfileFetcher;
import org.zibble.dbedwars.io.MineSkinAPI;
import org.zibble.dbedwars.io.UUIDFetcher;
import org.zibble.dbedwars.io.UUIDTypeAdaptor;
import org.zibble.dbedwars.listeners.ConverseReplyHandler;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Optional;
import java.util.UUID;

public class NPCSkinGui extends GuiComponent<ChestMenu, NPCSkinGui> {

    private final NPCModel model;
    private final Optional<PlayerNPC> npc;

    protected NPCSkinGui(NPCModel model, PlayerNPC npc) {
        super(new ChestMenu(3, Component.text("NPC Skin")));
        this.model = model;
        this.npc = Optional.ofNullable(npc);

        this.mask("XXXXXXXXX", "XXABXCDXX", "XXXX0XXXX");

        this.item('X', BwItemStack.builder()
                .material(XMaterial.BLACK_STAINED_GLASS_PANE)
                .displayName(AdventureMessage.blank())
                .build());
        this.item('0', BwItemStack.builder()
                .material(XMaterial.ARROW)
                .displayName(AdventureMessage.from("<aqua>Back!"))
                .build(), (protocolPlayer, clickType) ->
                new NPCCreatorGui(model, this.npc.orElse(null)).setupForPlayerNpc().open((Player) protocolPlayer.handle()));

        this.item('A', BwItemStack.builder()
                .material(XMaterial.PAPER)
                .displayName(AdventureMessage.from("<aqua>Name"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            this.close();
            Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Enter the name of the player with skin."));
            ConverseReplyHandler.INSTANCE.listenForReply(p, (reply) -> {
                if (Strings.isNullOrEmpty(reply.getMessage())) {
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>You must enter a name."));
                    return false;
                }
                ActionFuture<UUID> uuid = ActionFuture.supplyAsync(() -> UUIDFetcher.getInstance().getUUID(ChatColor.stripColor(reply.getMessage())));
                uuid.thenAccept(uid -> {
                    if (uid == null) {
                        Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>Could not find player with name: " + reply.getMessage()));
                        new NPCSkinGui(this.model, this.npc.orElse(null)).open(p);
                    } else {
                        PlayerGameProfile profile = GameProfileFetcher.getInstance().fetch(uid);
                        Skin skin = Skin.from(profile.getProperties().get(0));
                        this.model.addAttribute(Attributes.SKIN, skin);
                        this.npc.ifPresent(n -> n.setSkin(skin).thenCompose(BedwarsNPC::refresh));
                        Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Set skin to: " + profile.getName()));
                        new NPCCreatorGui(this.model, this.npc.orElse(null)).setupForPlayerNpc().open(p);
                    }
                });
                return true;
            });
        });

        this.item('B', BwItemStack.builder()
                .material(XMaterial.PAPER)
                .displayName(AdventureMessage.from("<aqua>UUID"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            this.close();
            Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Enter the UUID of the player with skin."));
            ConverseReplyHandler.INSTANCE.listenForReply(p, (reply) -> {
                if (Strings.isNullOrEmpty(reply.getMessage())) {
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>You must enter a name."));
                    return false;
                }

                String uid = ChatColor.stripColor(reply.getMessage());
                UUID uuid;
                if (UUIDTypeAdaptor.UUID_PATTERN.matcher(uid).matches()) {
                    uuid = UUID.fromString(uid);
                } else if (uid.length() == 32) {
                    uuid = UUIDTypeAdaptor.fromString(uid);
                } else {
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>Invalid UUID."));
                    return false;
                }
                ActionFuture.runAsync(() -> {
                    PlayerGameProfile profile = GameProfileFetcher.getInstance().fetch(uuid);
                    Skin skin = Skin.from(profile.getProperties().get(0));
                    this.model.addAttribute(Attributes.SKIN, skin);
                    this.npc.ifPresent(n -> n.setSkin(skin).thenCompose(BedwarsNPC::refresh));
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Set skin to: " + profile.getName()));
                    new NPCCreatorGui(this.model, this.npc.orElse(null)).setupForPlayerNpc().open(p);
                });
                return true;
            });
        });

        this.item('C', BwItemStack.builder()
                .material(XMaterial.PAPER)
                .displayName(AdventureMessage.from("<aqua>MineSkin"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            this.close();
            Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Enter MineSkin Id."));
            ConverseReplyHandler.INSTANCE.listenForReply(p, (reply) -> {
                if (Strings.isNullOrEmpty(reply.getMessage())) {
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>You must enter a MineSkin Id."));
                    return false;
                }
                ActionFuture.runAsync(() -> {
                    Skin skin = MineSkinAPI.getInstance().getSkin(ChatColor.stripColor(reply.getMessage()));

                    if (skin.getValue() == null) {
                        Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>Could not find skin with id: " + reply.getMessage()));
                        new NPCSkinGui(this.model, this.npc.orElse(null)).open(p);
                    } else {
                        this.model.addAttribute(Attributes.SKIN, skin);
                        this.npc.ifPresent(n -> n.setSkin(skin).thenCompose(BedwarsNPC::refresh));
                        Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Set skin to: " + reply.getMessage()));
                        new NPCCreatorGui(this.model, this.npc.orElse(null)).setupForPlayerNpc().open(p);
                    }
                });
                return true;
            });
        });

        this.item('D', BwItemStack.builder()
                .material(XMaterial.PAPER)
                .displayName(AdventureMessage.from("<aqua>Skin Value"))
                .build(), (protocolPlayer, clickType) -> {
            Player p = (Player) protocolPlayer.handle();
            this.close();
            Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Enter Skin Value"));
            ConverseReplyHandler.INSTANCE.listenForReply(p, (reply) -> {
                if (Strings.isNullOrEmpty(reply.getMessage())) {
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>You must enter a Skin Value."));
                    return false;
                }
                ActionFuture.runAsync(() -> {
                    Skin skin = Skin.from(ChatColor.stripColor(reply.getMessage()));
                    this.model.addAttribute(Attributes.SKIN, skin);
                    this.npc.ifPresent(n -> n.setSkin(skin).thenCompose(BedwarsNPC::refresh));
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Set skin to: " + skin.getValue()));
                    new NPCCreatorGui(this.model, this.npc.orElse(null)).setupForPlayerNpc().open(p);
                });
                return true;
            });
        });
    }

}
