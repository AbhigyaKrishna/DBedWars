package org.zibble.dbedwars.api.game.view.attributes;

import org.zibble.dbedwars.api.util.Color;

public interface Colorable {

    boolean isColorable();

    void setColorable(boolean flag);

    void color(Color color);
}
