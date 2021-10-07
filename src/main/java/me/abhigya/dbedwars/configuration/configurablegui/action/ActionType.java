package me.abhigya.dbedwars.configuration.configurablegui.action;

public enum ActionType {

    CONSOLE( "Execute a command from the console" ),
    PLAYER( "Execute a command for the menu viewer" ),
    MESSAGE( "Send a message to the menu viewer" ),
    BROADCAST( "Broadcast a message to the server" ),
    CHAT( "Send a chat message as the player performing the action" ),
    OPEN_GUI_MENU( "Open a GUI menu" ),
    OPEN_MENU( "Open a GUI menu" ),
    REFRESH( "Refresh items in the current menu view" ),
    BROADCAST_SOUND( "Broadcast a sound to the server" ),
    PLAY_SOUND( "Play a sound for a the specific player" ),
    JSON_MESSAGE( "Send a json message to the menu viewer" );

    private final String description;

    ActionType( String description ) {
        this.description = description;
    }

    public String getDescription( ) {
        return this.description;
    }

}
