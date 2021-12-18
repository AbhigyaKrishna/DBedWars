package com.pepedevs.dbedwars.api.game.view.attributes;

import com.pepedevs.dbedwars.api.util.Color;

public interface Colorable {

    boolean isColorable();

    void setColorable(boolean flag);

    void color(Color color);

}
