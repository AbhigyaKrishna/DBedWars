package com.pepedevs.dbedwars.api.game.view;

import java.util.Map;

public interface Attribute {

    AttributeType getType();

    Map<String, Object> getKeyEntry();
}
