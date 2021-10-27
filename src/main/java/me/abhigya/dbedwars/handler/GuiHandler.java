package me.abhigya.dbedwars.handler;

import me.Abhigya.core.menu.inventory.ItemMenu;
import me.Abhigya.core.util.reflection.general.ConstructorReflection;
import me.abhigya.dbedwars.DBedwars;
import me.abhigya.dbedwars.api.util.gui.IAnvilMenu;
import me.abhigya.dbedwars.api.util.gui.IMenu;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class GuiHandler implements me.abhigya.dbedwars.api.handler.GuiHandler {

  private static final String GUI_PACKAGE = "me.abhigya.dbedwars.guis";

  private final DBedwars plugin;
  private final Map<String, IMenu> guis;
  private final Map<String, IAnvilMenu> anvilGuis;

  public GuiHandler(DBedwars plugin) {
    this.plugin = plugin;
    this.guis = new ConcurrentHashMap<>();
    this.anvilGuis = new ConcurrentHashMap<>();
  }

  public void loadMenus() {
    try {
      Reflections ref = new Reflections(GUI_PACKAGE);
      Set<Class<? extends IMenu>> classes = ref.getSubTypesOf(IMenu.class);
      for (Class<? extends IMenu> clazz : classes) {
        IMenu menu =
            ConstructorReflection.newInstance(clazz, new Class<?>[] {DBedwars.class}, this.plugin);
        this.registerGui(menu.getIdentifier(), menu);
      }

    } catch (NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void loadAnvilMenus() {
    try {
      Reflections ref = new Reflections(GUI_PACKAGE);
      Set<Class<? extends IAnvilMenu>> classes = ref.getSubTypesOf(IAnvilMenu.class);
      for (Class<? extends IAnvilMenu> clazz : classes) {
        IAnvilMenu menu =
            ConstructorReflection.newInstance(clazz, new Class<?>[] {DBedwars.class}, this.plugin);
        this.registerAnvilGui(menu.getIdentifier(), menu);
      }
    } catch (InvocationTargetException
        | NoSuchMethodException
        | InstantiationException
        | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void registerGui(String name, IMenu<? extends ItemMenu> menu) {
    this.guis.put(name, menu);
    menu.getMenu().registerListener(this.plugin);
  }

  @Override
  public void unRegisterGui(String name) {
    IMenu menu = this.guis.remove(name);
    if (menu != null) menu.getMenu().registerListener(this.plugin);
  }

  @Nullable
  @Override
  public IMenu getGui(String name) {
    return this.guis.getOrDefault(name, null);
  }

  @Override
  public void registerAnvilGui(String name, IAnvilMenu menu) {
    this.anvilGuis.put(name, menu);
    menu.getMenu().registerListener(this.plugin);
  }

  @Override
  public void unRegisterAnvilGui(String name) {
    IAnvilMenu menu = this.anvilGuis.remove(name);
    if (menu != null) menu.getMenu().registerListener(this.plugin);
  }

  @Nullable
  @Override
  public IAnvilMenu getAnvilGui(String name) {
    return this.anvilGuis.getOrDefault(name, null);
  }

  @Override
  public Map<String, IMenu> getGuis() {
    return Collections.unmodifiableMap(this.guis);
  }

  @Override
  public Map<String, IAnvilMenu> getAnvilGuis() {
    return Collections.unmodifiableMap(this.anvilGuis);
  }
}
