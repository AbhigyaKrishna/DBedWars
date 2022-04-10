package org.zibble.dbedwars.api.objects.profile;

public class Property {

    private String name;
    private String value;
    private String signature;

    public Property(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static class Builder implements org.zibble.dbedwars.api.util.mixin.Builder<Property> {

        private String name;
        private String value;
        private String signature;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder signature(String signature) {
            this.signature = signature;
            return this;
        }

        @Override
        public Property build() {
            return new Property(this.name, this.value, this.signature);
        }

    }

}
