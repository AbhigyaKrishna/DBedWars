package com.pepedevs.dbedwars.api.messaging;

public interface PlaceholderEntry {

    static PlaceholderEntry of(String placeholder, String replacement) {
        return new PlaceholderEntry() {
            @Override
            public String getPlaceholder() {
                return placeholder;
            }

            @Override
            public String getReplacement() {
                return replacement;
            }
        };
    }

    String getPlaceholder();
    String getReplacement();

}
