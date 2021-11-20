package com.pepedevs.dbedwars.messaging;

import com.pepedevs.dbedwars.DBedwars;
import com.pepedevs.dbedwars.api.messaging.MessageParser;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MiniMessageParser implements MessageParser {

    private MiniMessage instance;
    private final DBedwars plugin;

    public MiniMessageParser(DBedwars plugin, MiniMessage instance) {
        this.instance = instance;
        this.plugin = plugin;
    }

    /*public Component parse(String message){

    }

    public Component parseWPAPI(String message){
        pl
    }*/

    public void setInstance(MiniMessage instance) {
        this.instance = instance;
    }
}
