package org.zibble.dbedwars.menus.actions;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.exceptions.DuplicateActionDefinition;
import org.zibble.dbedwars.api.menu.MenuActions;
import org.zibble.dbedwars.handler.MenuHandler;
import org.zibble.dbedwars.menus.actions.defaults.BroadcastActionImpl;
import org.zibble.dbedwars.menus.actions.defaults.SendMessageActionImpl;

import java.util.HashMap;
import java.util.Optional;

public class ActionRegistry {

    private static final HashMap<String, MenuActions> actionRegister;

    static {
        actionRegister = new HashMap<>();
    }

    private final MenuHandler menuHandler;

    public ActionRegistry(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
        doDefaultRegistration();
    }

    public void register(@NotNull MenuActions menuActions) throws DuplicateActionDefinition {
        if(actionRegister.containsKey(menuActions.tag()))
            throw new DuplicateActionDefinition("Tag for action ["+menuActions.tag()+" , "+menuActions.description()+"] is already definied!");

        actionRegister.put(menuActions.tag(), menuActions);
    }

    public void register(@NotNull MenuActions... menuActions) throws DuplicateActionDefinition {
        for(MenuActions actions : menuActions){
            this.register(actions);
        }
    }

    public void unregister(@NotNull String tag){
        actionRegister.remove(tag);
    }

    public void unregister(@NotNull MenuActions actions){
        actionRegister.remove(actions.tag());
    }

    public boolean isRegistered(@NotNull String tag){
        return actionRegister.containsKey(tag);
    }

    public boolean isRegistered(@NotNull MenuActions actions){
        return actionRegister.containsKey(actions.tag());
    }

    private void doDefaultRegistration(){
        try {
            register(
                    new BroadcastActionImpl(),
                    new SendMessageActionImpl()
            );
        } catch (DuplicateActionDefinition e) {
            e.printStackTrace();
        }
    }

    public static Optional<MenuActions> getIfPresent(@NotNull String tag){
        return Optional.ofNullable(actionRegister.get(tag));
    }
}
