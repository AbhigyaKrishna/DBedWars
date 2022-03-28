package org.zibble.dbedwars.configuration.configurable;

import org.bukkit.configuration.ConfigurationSection;
import org.zibble.dbedwars.configuration.framework.Loadable;
import org.zibble.dbedwars.configuration.framework.annotations.ConfigPath;

import java.util.List;

public class ConfigurableNpc implements Loadable {

    @ConfigPath
    private List<String> name;

    @ConfigPath
    private String type;

    @ConfigPath
    private ConfigurableTexture texture;

    @ConfigPath
    private String shop;

    @ConfigPath
    private List<String> actions;

    @Override
    public void load(ConfigurationSection section) {
        this.loadEntries(section);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public List<String> getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public ConfigurableTexture getTexture() {
        return texture;
    }

    public String getShop() {
        return shop;
    }

    public List<String> getActions() {
        return actions;
    }

    public static class ConfigurableTexture implements Loadable {

        @ConfigPath
        private String owner;

        @ConfigPath
        private String value;

        @ConfigPath
        private String signature;

        @ConfigPath
        private String mineskin;

        @Override
        public void load(ConfigurationSection section) {
            this.loadEntries(section);
        }

        @Override
        public boolean isValid() {
            return true;
        }

        public String getOwner() {
            return owner;
        }

        public String getValue() {
            return value;
        }

        public String getSignature() {
            return signature;
        }

        public String getMineskin() {
            return mineskin;
        }

    }

}
