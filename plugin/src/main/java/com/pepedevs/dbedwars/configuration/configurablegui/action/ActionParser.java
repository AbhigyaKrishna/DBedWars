package com.pepedevs.dbedwars.configuration.configurablegui.action;

import com.pepedevs.radium.gui.inventory.ItemMenu;
import com.pepedevs.radium.gui.inventory.action.ItemClickAction;
import com.pepedevs.radium.gui.inventory.item.action.ItemAction;
import com.pepedevs.radium.gui.inventory.item.action.ItemActionPriority;
import com.pepedevs.radium.utils.StringUtils;
import com.pepedevs.dbedwars.api.util.SoundVP;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

public class ActionParser {

    private ActionType type;
    private String action;
    private ItemActionPriority priority;
    private ClickType clickType;

    public ActionParser() {
        /*uninitialized constructor*/
    }

    public ActionParser(
            ActionType type,
            String action,
            ItemActionPriority priority,
            @Nullable ClickType clickType) {
        this.type = type;
        this.action = action;
        this.priority = priority;
        this.clickType = clickType;
    }

    public ItemAction toAction() {
        return new ItemAction() {
            @Override
            public ItemActionPriority getPriority() {
                return priority;
            }

            @Override
            public void onClick(ItemClickAction action) {
                if (clickType != null) {
                    if (clickType.equals(action.getClickType()))
                        ActionParser.this.perform(action.getMenu(), action.getPlayer());
                } else {
                    ActionParser.this.perform(action.getMenu(), action.getPlayer());
                }
            }
        };
    }

    private void perform(ItemMenu menu, Player player) {
        switch (this.type) {
            case CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.action);
                break;
            case PLAYER:
                player.performCommand(this.action);
                break;
            case MESSAGE:
                player.sendMessage(StringUtils.translateAlternateColorCodes(this.action));
                break;
            case BROADCAST:
                Bukkit.broadcastMessage(StringUtils.translateAlternateColorCodes(this.action));
                break;
            case CHAT:
                player.chat(this.action);
                break;
            case OPEN_GUI_MENU:
            case OPEN_MENU:
                // TODO
                break;
            case REFRESH:
                menu.update(player);
                break;
            case BROADCAST_SOUND:
            case PLAY_SOUND:
                SoundVP sound = SoundVP.valueOf(this.action);
                if (sound == null) return;

                if (this.type == ActionType.BROADCAST_SOUND) {
                    sound.play(player.getLocation());
                    break;
                }
                sound.play(player);
                break;

            case JSON_MESSAGE:
                player.spigot().sendMessage(ComponentSerializer.parse(this.action));
                break;
        }
    }

    public ActionType getType() {
        return type;
    }

    public ActionParser setType(ActionType type) {
        this.type = type;
        return this;
    }

    public String getAction() {
        return action;
    }

    public ActionParser setAction(String action) {
        this.action = action;
        return this;
    }

    public ItemActionPriority getPriority() {
        return priority;
    }

    public ActionParser setPriority(ItemActionPriority priority) {
        this.priority = priority;
        return this;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public ActionParser setClickType(@Nullable ClickType clickType) {
        this.clickType = clickType;
        return this;
    }
}
