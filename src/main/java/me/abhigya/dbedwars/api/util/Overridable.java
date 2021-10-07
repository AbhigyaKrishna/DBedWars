package me.abhigya.dbedwars.api.util;

import me.abhigya.dbedwars.api.exceptions.OverrideException;

public interface Overridable {

    void override( Overridable override ) throws OverrideException;

}
