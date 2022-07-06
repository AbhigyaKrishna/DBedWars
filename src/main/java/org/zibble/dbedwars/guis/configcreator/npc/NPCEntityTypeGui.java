package org.zibble.dbedwars.guis.configcreator.npc;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.CaseFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.zibble.dbedwars.DBedwars;
import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.guis.component.PaginatedGuiComponent;
import org.zibble.dbedwars.api.hooks.npc.model.NPCModel;
import org.zibble.dbedwars.api.messaging.message.AdventureMessage;
import org.zibble.dbedwars.api.objects.profile.PlayerGameProfile;
import org.zibble.dbedwars.api.objects.profile.Property;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.cache.Cache;
import org.zibble.inventoryframework.menu.inventory.ChestMenu;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class NPCEntityTypeGui extends PaginatedGuiComponent<Integer, ChestMenu, NPCEntityTypeGui> {

    private static final String SLOTS = "ABCDEFGHIJKLMNOPQRSTUVWYZ123";

    BwItemStack background = BwItemStack.builder()
            .material(XMaterial.BLACK_STAINED_GLASS_PANE)
            .displayName(AdventureMessage.blank())
            .build();

    public static NPCEntityTypeGui show(Player player) {
        return new NPCEntityTypeGui().open(player, 0);
    }

    protected NPCEntityTypeGui() {
        super(Integer::compareTo);

        this.setUp();
    }

    void setUp() {
        int i = 0;
        int l = SLOTS.length();
        GuiComponent gui = this.createDefaultPage();
        this.addPage(0, gui);

        for (EntityType entityType : EntityType.values()) {
            if (!entityType.isSpawnable() || !entityType.isAlive()) continue;

            if (i >= l) {
                i = 0;
                gui = this.createDefaultPage();
                this.addPage(this.pages.size(), gui);
            }

            gui.item(SLOTS.charAt(i++), BwItemStack.builder().material(XMaterial.PLAYER_HEAD)
                    .displayName(AdventureMessage.from("<yellow>" + Arrays.stream(entityType.name().toLowerCase(Locale.ROOT).split("_"))
                            .map(s -> CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, s))
                            .collect(Collectors.joining(" "))
                            .trim()))
                    .meta(meta -> {
                        String value = Cache.getHead(entityType.name());
                        if (value == null) return;
                        DBedwars.getInstance().getNMSAdaptor().setSkullProfile(((SkullMeta) meta), PlayerGameProfile.builder()
                                .name(entityType.name())
                                .property(Property.builder()
                                        .name("textures")
                                        .value(value)
                                        .build())
                                .build());
                    })
                    .build(), (protocolPlayer, clickType) -> NPCCreatorGui.creator(new NPCModel(entityType)).setupForEntityNPC().open((Player) protocolPlayer.handle()));
        }

        for (Map.Entry<Integer, GuiComponent<ChestMenu, ? extends GuiComponent>> entry : this.pages.entrySet()) {
            entry.getValue().item('9', BwItemStack.builder()
                    .material(XMaterial.ARROW)
                    .displayName(AdventureMessage.from("<red>Previous Page"))
                    .build(), (__, ___) -> this.previousPage());
            entry.getValue().item('0', BwItemStack.builder()
                    .material(XMaterial.ARROW)
                    .displayName(AdventureMessage.from("<green>Next Page"))
                    .build(), (__, ___) -> this.nextPage());

            entry.getValue().item('X', background);

            if (entry.getKey() == 0) {
                entry.getValue().item('9', background);
            }
            if (entry.getKey() == this.pages.size() - 1) {
                entry.getValue().item('0', background);
            }
        }
    }

    GuiComponent createDefaultPage() {
        return GuiComponent.creator(new ChestMenu(6, Component.text("NPC Entity Type")))
                .mask("XXXXXXXXX", "XABCDEFGX", "XHIJKLMNX", "XOPQRSTUX", "XVWYZ123X", "XX9XXX0XX");
    }

}
