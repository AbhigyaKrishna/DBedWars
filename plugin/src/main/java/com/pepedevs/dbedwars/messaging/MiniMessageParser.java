package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.messaging.MessageParser;
import me.Abhigya.core.placeholder.PlaceholderUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.entity.Player;

public class MiniMessageParser implements MessageParser {

    private MiniMessage instance;
    private final DBedwars plugin;
    private int replacementCount;

    public MiniMessageParser(DBedwars plugin, MiniMessage instance) {
        this.instance = instance;
        this.plugin = plugin;
    }

    @Override
    public Component parse(String message){
        return instance.parse(message);
    }

    @Override
    public Component parseWithPAPI(String message, Player player){
        String parsedMessage = PlaceholderUtil.getManager().apply(player, message);
        return this.parse(parsedMessage);
    }

    @Override
    public Component parseWithPlaceholder(String message, Player player, String placeholder, String replacement){
        String parsedMessage = this.replacer(message, placeholder, replacement);
        return this.parseWithPAPI(message, player);
    }

    @Override
    public Component parseWithPlaceholder(String message, Player player, Template... placeholders){
        String parsedMessage = PlaceholderUtil.getManager().apply(player, message);
        return instance.parse(parsedMessage, placeholders);
    }

    private String replacer(String message, String placeholder, String replacement){
        String returnMessage = message;
        for (int count = replacementCount; count > 0; count--) {
            returnMessage = returnMessage.replace(placeholder, replacement);
        }
        return returnMessage;
    }

    public void setInstance(MiniMessage instance) {
        this.instance = instance;
    }

    @Override
    public void setReplacementCount(int replacementCount) {
        this.replacementCount = replacementCount;
    }
}