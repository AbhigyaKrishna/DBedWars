package me.abhigya.dbedwars.handler;

import me.Abhigya.core.item.ActionItemHandler;
import me.Abhigya.core.util.reflection.general.ConstructorReflection;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.gui.IAnvilMenu;
import me.abhigya.dbedwars.api.util.gui.IMenu;
import me.abhigya.dbedwars.item.CustomItems;
import me.abhigya.dbedwars.item.FireballItem;
import me.abhigya.dbedwars.listeners.FireBallListener;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class GuiHandler {

    private static final String GUI_PACKAGE = "me.abhigya.dbedwars.guis";

    private final DBedwars plugin;
    private final Map<String, IMenu> guis;
    private final Map<String, IAnvilMenu> anvilGuis;

    public GuiHandler(DBedwars plugin) {
        this.plugin = plugin;
        this.guis = new HashMap<>();
        this.anvilGuis = new HashMap<>();
    }

    public void loadMenus() {
        try {
            Reflections ref = new Reflections(GUI_PACKAGE);
            Set<Class<? extends IMenu>> classes = ref.getSubTypesOf(IMenu.class);
            for (Class<? extends IMenu> clazz : classes) {
                IMenu menu = ConstructorReflection.newInstance(clazz, new Class<?>[]{DBedwars.class}, this.plugin);
                guis.put(menu.getIdentifier(), menu);
            }

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void loadAnvilMenus() {
        try {
            Reflections ref = new Reflections(GUI_PACKAGE);
            Set<Class<? extends IAnvilMenu>> classes = ref.getSubTypesOf(IAnvilMenu.class);
            for (Class<? extends IAnvilMenu> clazz : classes) {
                IAnvilMenu menu = ConstructorReflection.newInstance(clazz, new Class<?>[]{DBedwars.class}, this.plugin);
                anvilGuis.put(menu.getIdentifier(), menu);
            }
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void loadItems() {
        for (CustomItems item : CustomItems.values()) {
            ActionItemHandler.register(item.getItem());
        }
        new FireBallListener(plugin).register();
    }

    public Map<String, IMenu> getGuis() {
        return Collections.unmodifiableMap(guis);
    }

    public Map<String, IAnvilMenu> getAnvilGuis() {
        return anvilGuis;
    }
}
