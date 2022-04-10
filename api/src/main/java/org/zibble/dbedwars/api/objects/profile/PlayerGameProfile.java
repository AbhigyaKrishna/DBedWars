package org.zibble.dbedwars.api.objects.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerGameProfile {

    private final List<Property> textureProperties = new ArrayList<>();
    private UUID uuid;
    private String name;

    public PlayerGameProfile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Property> getProperties() {
        return textureProperties;
    }

    public static class Builder implements org.zibble.dbedwars.api.util.mixin.Builder<PlayerGameProfile> {

        private UUID uuid;
        private String name;
        private List<Property> properties = new ArrayList<>();

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder property(Property property) {
            this.properties.add(property);
            return this;
        }

        public Builder properties(List<Property> properties) {
            this.properties.addAll(properties);
            return this;
        }

        @Override
        public PlayerGameProfile build() {
            PlayerGameProfile profile = new PlayerGameProfile(uuid, name);
            profile.textureProperties.addAll(properties);
            return profile;
        }

    }

}
