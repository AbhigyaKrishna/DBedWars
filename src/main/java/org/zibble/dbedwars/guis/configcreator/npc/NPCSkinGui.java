package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Strings;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.zibble.dbedwars.api.future.ActionFuture;
import org.zibble.dbedwars.api.guis.MenuPlayer;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.hooks.npc.BedwarsNPC;
import org.zibble.dbedwars.api.hooks.npc.PlayerNPC;
import org.zibble.dbedwars.api.hooks.npc.model.Attributes;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.profile.Skin;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.guis.reflection.*;
import org.zibble.dbedwars.io.GameProfileFetcher;
import org.zibble.dbedwars.io.MineSkinAPI;
import org.zibble.dbedwars.io.UUIDFetcher;
import org.zibble.dbedwars.io.UUIDTypeAdaptor;
import org.zibble.dbedwars.listeners.ConverseReplyHandler;
import org.zibble.dbedwars.messaging.Messaging;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Optional;
import java.util.UUID;

public class NPCSkinGui extends ReflectiveGui {

    public static final NPCSkinGui INSTANCE = new NPCSkinGui();

    @Item('X')
    private final BwItemStack background = BwItemStack.builder()
            .material(XMaterial.BLACK_STAINED_GLASS_PANE)
            .displayName(AdventureMessage.blank())
            .build();

    @Item('0')
    private final BwItemStack back = BwItemStack.builder()
            .material(XMaterial.ARROW)
            .displayName(AdventureMessage.from("<aqua>Back!"))
            .build();

    @Item('A')
    private final BwItemStack name = BwItemStack.builder()
            .material(XMaterial.PAPER)
            .displayName(AdventureMessage.from("<aqua>Name"))
            .build();

    @Item('B')
    private final BwItemStack uuid = BwItemStack.builder()
            .material(XMaterial.PAPER)
            .displayName(AdventureMessage.from("<aqua>UUID"))
            .build();

    @Item('C')
    private final BwItemStack mineskin = BwItemStack.builder()
            .material(XMaterial.PAPER)
            .displayName(AdventureMessage.from("<aqua>MineSkin"))
            .build();

    @Item('D')
    private final BwItemStack skinValue = BwItemStack.builder()
            .material(XMaterial.PAPER)
            .displayName(AdventureMessage.from("<aqua>Skin Value"))
            .build();

    @ClickAction('0')
    @Dynamic
    void handleBackButton(MenuPlayer player, NPCModel model, PlayerNPC npc) {
        new NPCCreatorGui(model, npc).setupForPlayerNpc().open(player.handle());
    }

    @ClickAction('A')
    @Dynamic
    void handleNameButton(GuiComponent<ChestMenu, GuiComponent> menu, MenuPlayer player, NPCModel model, PlayerNPC playerNpc) {
        Player p = player.handle();
        Optional<PlayerNPC> npc = Optional.ofNullable(playerNpc);
        menu.close();
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
                    INSTANCE.get(Argument.of(NPCModel.class, model), Argument.of(PlayerNPC.class, playerNpc)).open(p);
                } else {
                    this.executeGameProfile(model, playerNpc, p, npc, uid);
                }
            });
            return true;
        });
    }

    @ClickAction('B')
    @Dynamic
    void handleUUIDButton(GuiComponent<ChestMenu, GuiComponent> menu, MenuPlayer player, NPCModel model, PlayerNPC playerNpc) {
        Player p = player.handle();
        Optional<PlayerNPC> npc = Optional.ofNullable(playerNpc);
        menu.close();
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
                this.executeGameProfile(model, playerNpc, p, npc, uuid);
            });
            return true;
        });
    }

    @ClickAction('C')
    @Dynamic
    void handleMineSkinButton(GuiComponent<ChestMenu, GuiComponent> menu, MenuPlayer player, NPCModel model, PlayerNPC playerNpc) {
        Player p = player.handle();
        Optional<PlayerNPC> npc = Optional.ofNullable(playerNpc);
        menu.close();
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
                    NPCSkinGui.INSTANCE.get(Argument.of(NPCModel.class, model), Argument.of(PlayerNPC.class, playerNpc)).open(p);
                } else {
                    model.addAttribute(Attributes.SKIN, skin);
                    npc.ifPresent(n -> n.setSkin(skin).thenCompose(BedwarsNPC::refresh));
                    Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Set skin to: " + reply.getMessage()));
                    new NPCCreatorGui(model, playerNpc).setupForPlayerNpc().open(p);
                }
            });
            return true;
        });
    }

    @ClickAction('D')
    @Dynamic
    void handleSkinButton(GuiComponent<ChestMenu, GuiComponent> menu, MenuPlayer player, NPCModel model, PlayerNPC playerNpc) {
        Player p = player.handle();
        Optional<PlayerNPC> npc = Optional.ofNullable(playerNpc);
        menu.close();
        Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Enter Skin Value"));
        ConverseReplyHandler.INSTANCE.listenForReply(p, (reply) -> {
            if (Strings.isNullOrEmpty(reply.getMessage())) {
                Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<red>You must enter a Skin Value."));
                return false;
            }
            if (reply.getMessage().equalsIgnoreCase("exit")) {
                NPCSkinGui.INSTANCE.get(Argument.of(NPCModel.class, model), Argument.of(PlayerNPC.class, playerNpc));
                return true;
            }
            ActionFuture.runAsync(() -> {
                Skin skin = Skin.from(ChatColor.stripColor(reply.getMessage()));
                model.addAttribute(Attributes.SKIN, skin);
                npc.ifPresent(n -> n.setSkin(skin).thenCompose(BedwarsNPC::refresh));
                Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Set skin to: " + skin.getValue()));
                new NPCCreatorGui(model, playerNpc).setupForPlayerNpc().open(p);
            });
            return true;
        });
    }

    private void executeGameProfile(NPCModel model, PlayerNPC playerNpc, Player p, Optional<PlayerNPC> npc, UUID uuid) {
        PlayerGameProfile profile = GameProfileFetcher.getInstance().fetch(uuid);
        Skin skin = Skin.from(profile.getProperties().get(0));
        model.addAttribute(Attributes.SKIN, skin);
        npc.ifPresent(n -> n.setSkin(skin).thenCompose(BedwarsNPC::refresh));
        Messaging.get().getMessagingMember(p).sendMessage(AdventureMessage.from("<green>Set skin to: " + profile.getName()));
        new NPCCreatorGui(model, playerNpc).setupForPlayerNpc().open(p);
    }

    @Override
    protected GuiComponent<ChestMenu, GuiComponent> provide(Argument<?>... args) {
        ChestMenu menu = new ChestMenu(3, Component.text("NPC Skin"));
        GuiComponent<ChestMenu, GuiComponent> component = GuiComponent.creator(menu);
        component.mask("XXXXXXXXX", "XXABXCDXX", "XXXX0XXXX");
        return component;
    }

}
