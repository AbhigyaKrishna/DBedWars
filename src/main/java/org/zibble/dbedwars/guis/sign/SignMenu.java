package org.zibble.dbedwars.guis.sign;

import java.util.function.Consumer;

public class SignMenu {

    private final String[] text;
    private Consumer<String[]> onUpdate;

    protected SignMenu() {
        this(new String[0]);
    }

    protected SignMenu(String[] text) {
        this.text = text;
    }

    public Consumer<String[]> getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(Consumer<String[]> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public String[] getText() {
        return text;
    }

}
