package org.zibble.dbedwars.guis.reflection;

import org.zibble.dbedwars.api.guis.MenuPlayer;
import org.zibble.dbedwars.api.objects.serializable.BwItemStack;
import org.zibble.dbedwars.utils.Util;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.FieldWrapper;
import org.zibble.dbedwars.utils.reflection.resolver.wrapper.MethodWrapper;
import org.zibble.inventoryframework.ClickType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveGuiProcessor {

    public static final ReflectiveGuiProcessor INSTANCE = new ReflectiveGuiProcessor();

    public void process(ReflectiveGui gui) {
        for (Method method : gui.getClass().getMethods()) {
            MethodWrapper<?> wrapper = new MethodWrapper<>(method);
            if (method.isAnnotationPresent(ClickAction.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                ClickAction annotation = method.getAnnotation(ClickAction.class);
                if (method.isAnnotationPresent(Dynamic.class)) {
                    gui.DYNAMIC_CLICK_ACTION.put(annotation.value(), wrapper);
                } else {
                    if ((parameterTypes.length != 2)
                            || !parameterTypes[0].isAssignableFrom(MenuPlayer.class)
                            || !parameterTypes[1].isAssignableFrom(ClickType.class))
                        throw new IllegalArgumentException(String.format("Parameter of method `%s` must only be of types %s and %s", method.getName(), MenuPlayer.class.getName(), ClickType.class.getName()));

                    gui.CLICK_ACTIONS.put(annotation.value(), (protocolPlayer, clickType) -> wrapper.invoke(gui, protocolPlayer, clickType));
                }
            } else if (method.isAnnotationPresent(Item.class)) {
                if (!method.getReturnType().isAssignableFrom(BwItemStack.class))
                    throw new IllegalArgumentException(String.format("Method %s must have a return type of %s", method.getName(), BwItemStack.class.getName()));

                Class<?>[] parameterTypes = method.getParameterTypes();
                Item annotation = method.getAnnotation(Item.class);
                if (method.isAnnotationPresent(Dynamic.class)) {
                    gui.DYNAMIC_ITEMS.put(annotation.value(), wrapper);
                } else {
                    if (parameterTypes.length != 0)
                        throw new IllegalArgumentException(String.format("Method %s may not contain any parameters.", method.getName()));

                    BwItemStack item = (BwItemStack) wrapper.invoke(gui);
                    if (item != null)
                        gui.ITEMS.put(annotation.value(), () -> item);
                }
            } else if (method.getName().equals("postInit")) {
                gui.postInit = wrapper;
            }
        }

        for (Field field : gui.getClass().getFields()) {
            FieldWrapper wrapper = new FieldWrapper(field);
            if (field.isAnnotationPresent(Item.class)) {
                if (!field.getType().isAssignableFrom(BwItemStack.class))
                    throw new IllegalArgumentException(String.format("Field %s must be of type %s", field.getName(), BwItemStack.class.getName()));

                Item annotation = field.getAnnotation(Item.class);
                BwItemStack item = wrapper.get(gui);
                if (item != null)
                    gui.ITEMS.put(annotation.value(), () -> item);
            }
        }
    }

    public Object[] processArgument(Method method, Argument<?>... args) {
        List<Object> arguments = new ArrayList<>();
        for (Class<?> parameterType : method.getParameterTypes()) {
            Argument<?> matched = null;
            for (Argument<?> arg : args) {
                if (arg.isFor(parameterType)) {
                    matched = arg;
                }
            }

            if (matched != null) {
                arguments.add(matched.getValue());
            } else {
                throw new IllegalArgumentException(String.format("No match for parameter of type %s found!", parameterType.getName()));
            }
        }

        return arguments.toArray(new Object[0]);
    }

    public org.zibble.inventoryframework.ClickAction processClickAction(ReflectiveGui gui, MethodWrapper<?> method, Argument<?>... args) {
        if (method == null || !method.exists())
            return null;
        return (player, clickType) -> method.invoke(gui, this.processArgument(method.getMethod(), Util.mergeArray(new Argument[]{
                Argument.of(MenuPlayer.class, (MenuPlayer) player),
                Argument.of(ClickType.class, clickType)},
                args)));
    }

    public BwItemStack processItem(ReflectiveGui gui, MethodWrapper<?> method, Argument<?>... args) {
        if (method == null || !method.exists())
            return null;

        Object[] arguments = this.processArgument(method.getMethod(), args);
        return (BwItemStack) method.invoke(gui, arguments);
    }

}
