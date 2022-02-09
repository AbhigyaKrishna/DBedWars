package com.pepedevs.dbedwars.hologram;

import com.pepedevs.dbedwars.api.hologram.HologramSettings;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class HologramSettingsImpl implements HologramSettings {

    private String defaultText = " ";
    private BiFunction<Component, Player, Component> parser = (component, player) -> component;
    private boolean downOrigin = false;

    private double heightText = 0.3;
    private double heightIcon = 0.6;
    private double heightHead = 0.75;
    private double heightSmallHead = 0.6;

    private int displayRange = 48;
    private int updateRange = 48;
    private int updateInterval = 20;

    @Override
    public String getDefaultText() {
        return defaultText;
    }

    @Override
    public BiFunction<Component, Player, Component> getParser() {
        return parser;
    }

    @Override
    public void setParser(BiFunction<Component, Player, Component> parser) {
        this.parser = parser;
    }

    @Override
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    @Override
    public boolean isDownOrigin() {
        return downOrigin;
    }

    @Override
    public void setDownOrigin(boolean downOrigin) {
        this.downOrigin = downOrigin;
    }

    @Override
    public double getHeightText() {
        return heightText;
    }

    @Override
    public void setHeightText(double heightText) {
        this.heightText = heightText;
    }

    @Override
    public double getHeightIcon() {
        return heightIcon;
    }

    @Override
    public void setHeightIcon(double heightIcon) {
        this.heightIcon = heightIcon;
    }

    @Override
    public double getHeightHead() {
        return heightHead;
    }

    @Override
    public void setHeightHead(double heightHead) {
        this.heightHead = heightHead;
    }

    @Override
    public double getHeightSmallHead() {
        return heightSmallHead;
    }

    @Override
    public void setHeightSmallHead(double heightSmallHead) {
        this.heightSmallHead = heightSmallHead;
    }

    @Override
    public int getDisplayRange() {
        return displayRange;
    }

    @Override
    public void setDisplayRange(int displayRange) {
        this.displayRange = displayRange;
    }

    @Override
    public int getUpdateRange() {
        return updateRange;
    }

    @Override
    public void setUpdateRange(int updateRange) {
        this.updateRange = updateRange;
    }

    @Override
    public int getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

}

