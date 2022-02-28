package org.zibble.dbedwars.menus.actions;

import org.jetbrains.annotations.NotNull;
import org.zibble.dbedwars.api.exceptions.DuplicateActionDefinitionException;
import org.zibble.dbedwars.api.menu.MenuActions;
import org.zibble.dbedwars.handler.MenuHandler;
import org.zibble.dbedwars.menus.actions.defaults.*;

import java.util.HashMap;
import java.util.Optional;

public class ActionRegistry implements org.zibble.dbedwars.api.menu.ActionRegistry {

    private static final HashMap<String, MenuActions> actionRegister;

    static {
        actionRegister = new HashMap<>();
    }

    private final MenuHandler menuHandler;

    public ActionRegistry(MenuHandler menuHandler) {
        this.menuHandler = menuHandler;
        doDefaultRegistration();
    }

    @Override
    public void register(@NotNull MenuActions menuActions) throws DuplicateActionDefinitionException {
        if(actionRegister.containsKey(menuActions.tag()))
            throw new DuplicateActionDefinitionException("Tag for action ["+menuActions.tag()+" , "+menuActions.description()+"] is already definied!");

        actionRegister.put(menuActions.tag(), menuActions);
    }

    @Override
    public void register(@NotNull MenuActions... menuActions) throws DuplicateActionDefinitionException {
        for(MenuActions actions : menuActions){
            this.register(actions);
        }
    }

    @Override
    public void unregister(@NotNull String tag){
        actionRegister.remove(tag);
    }

    @Override
    public void unregister(@NotNull MenuActions actions){
        actionRegister.remove(actions.tag());
    }

    @Override
    public boolean isRegistered(@NotNull String tag){
        return actionRegister.containsKey(tag);
    }

    @Override
    public boolean isRegistered(@NotNull MenuActions actions){
        return actionRegister.containsKey(actions.tag());
    }

    @Override
    public Optional<MenuActions> getOptionalMenu(@NotNull String tag){
        return Optional.ofNullable(actionRegister.get(tag));
    }

    private void doDefaultRegistration(){
        try {
            register(
                    new SendMessageActionImpl(),
                    new CloseInventoryActionImpl(),
                    new UpdateInventoryActionImpl(),
                    new CommandConsoleActionImpl(),
                    new PlayerCommandActionImpl()
            );
        } catch (DuplicateActionDefinitionException e) {
            e.printStackTrace();
        }
    }

    public static Optional<MenuActions> getIfPresent(@NotNull String tag){
        return Optional.ofNullable(actionRegister.get(tag));
    }
}
