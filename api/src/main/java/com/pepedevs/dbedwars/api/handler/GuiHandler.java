package com.pepedevs.dbedwars.api.handler;

import com.pepedevs.radium.gui.inventory.ItemMenu;
import com.pepedevs.dbedwars.api.util.gui.IAnvilMenu;
import com.pepedevs.dbedwars.api.util.gui.IMenu;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface GuiHandler {

    void registerGui(String name, IMenu<? extends ItemMenu> menu);

    void unRegisterGui(String name);

    @Nullable
    IMenu getGui(String name);

    void registerAnvilGui(String name, IAnvilMenu menu);

    void unRegisterAnvilGui(String name);

    @Nullable
    IAnvilMenu getAnvilGui(String name);

    Map<String, IMenu> getGuis();

    Map<String, IAnvilMenu> getAnvilGuis();
}
