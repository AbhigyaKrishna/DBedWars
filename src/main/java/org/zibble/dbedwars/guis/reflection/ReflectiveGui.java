package org.zibble.dbedwars.guis.reflection;

import org.zibble.dbedwars.api.guis.component.GuiComponent;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.utils.Debugger;
import org.zibble.dbedwars.utils.Util;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;
import org.zibble.inventoryframework.ClickAction;
import org.zibble.inventoryframework.menu.Menu;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ReflectiveGui {

    private boolean loaded = false;

    final Map<Character, Supplier<BwItemStack>> ITEMS = new HashMap<>();
    final Map<Character, ClickAction> CLICK_ACTIONS = new HashMap<>();
    final Map<Character, MethodWrapper<?>> DYNAMIC_ITEMS = new HashMap<>();
    final Map<Character, MethodWrapper<?>> DYNAMIC_CLICK_ACTION = new HashMap<>();

    MethodWrapper<?> postInit;

    public GuiComponent<? extends Menu, ? extends GuiComponent> get(Argument<?>... args) {
        if (!loaded) {
            ReflectiveGuiProcessor.INSTANCE.process(this);
            loaded = true;
        }
        GuiComponent<? extends Menu, ? extends GuiComponent> menu = this.provide(args);
        this.update(menu, args);
        if (this.postInit != null) {
            this.postInit.invoke(this, ReflectiveGuiProcessor.INSTANCE.processArgument(this.postInit.getMethod(),
                    Util.mergeArray(new Argument[]{Argument.of(GuiComponent.class, menu)}, args)));
        }
        return menu;
    }

    public void update(GuiComponent<? extends Menu, ? extends GuiComponent> menu, Argument<?>... args) {
        for (Map.Entry<Character, Supplier<BwItemStack>> entry : ITEMS.entrySet()) {
            menu.item(entry.getKey(), entry.getValue(), this.getClickAction(entry.getKey(), Util.mergeArray(new Argument<?>[]{Argument.of(GuiComponent.class, menu)}, args)));
        }
        for (Map.Entry<Character, MethodWrapper<?>> entry : DYNAMIC_ITEMS.entrySet()) {
            BwItemStack item = ReflectiveGuiProcessor.INSTANCE.processItem(this, entry.getValue(), Util.mergeArray(new Argument<?>[]{Argument.of(GuiComponent.class, menu)}, args));
            if (item != null) {
                menu.item(entry.getKey(), item, this.getClickAction(entry.getKey(), Util.mergeArray(new Argument<?>[]{Argument.of(GuiComponent.class, menu)}, args)));
            }
        }
    }

    public void updateSlots(GuiComponent<? extends Menu, ? extends GuiComponent> menu, char[] slots, Argument<?>... args) {
        for (Map.Entry<Character, Supplier<BwItemStack>> entry : ITEMS.entrySet()) {
            if (!this.containSlot(slots, entry.getKey())) return;
            menu.item(entry.getKey(), entry.getValue(), this.getClickAction(entry.getKey(), Util.mergeArray(new Argument<?>[]{Argument.of(GuiComponent.class, menu)}, args)));
        }
        for (Map.Entry<Character, MethodWrapper<?>> entry : DYNAMIC_ITEMS.entrySet()) {
            if (!this.containSlot(slots, entry.getKey())) return;
            BwItemStack item = ReflectiveGuiProcessor.INSTANCE.processItem(this, entry.getValue(), Util.mergeArray(new Argument<?>[]{Argument.of(GuiComponent.class, menu)}, args));
            if (item != null) {
                menu.item(entry.getKey(), item, this.getClickAction(entry.getKey(), Util.mergeArray(new Argument<?>[]{Argument.of(GuiComponent.class, menu)}, args)));
            }
        }
    }

    private boolean containSlot(char[] slots, char c) {
        for (char slot : slots) {
            if (slot == c) return true;
        }

        return false;
    }

    private ClickAction getClickAction(char character, Argument<?>... args) {
        return CLICK_ACTIONS.getOrDefault(character,
                ReflectiveGuiProcessor.INSTANCE.processClickAction(this, DYNAMIC_CLICK_ACTION.get(character), args));
    }

    protected abstract GuiComponent<? extends Menu, ? extends GuiComponent> provide(Argument<?>... args);

}
