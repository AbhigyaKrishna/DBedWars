package com.pepedevs.dbedwars.api.hologram;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public interface HologramSettings {

     String getDefaultText();

     BiFunction<Component, Player, Component> getParser();

     void setParser(BiFunction<Component, Player, Component> parser);

     void setDefaultText(String defaultText);

     boolean isDownOrigin();

     void setDownOrigin(boolean downOrigin);

     double getHeightText();

     void setHeightText(double heightText);

     double getHeightIcon();

     void setHeightIcon(double heightIcon);

     double getHeightHead();

     void setHeightHead(double heightHead);

     double getHeightSmallHead();

     void setHeightSmallHead(double heightSmallHead);

     int getDisplayRange();

     void setDisplayRange(int displayRange);

     int getUpdateRange();

     void setUpdateRange(int updateRange);

     int getUpdateInterval();

     void setUpdateInterval(int updateInterval);
}
