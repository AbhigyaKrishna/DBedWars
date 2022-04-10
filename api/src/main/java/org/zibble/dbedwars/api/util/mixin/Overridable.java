package org.zibble.dbedwars.api.util.mixin;

import org.zibble.dbedwars.api.exceptions.OverrideException;

public interface Overridable {

    void override(Overridable override) throws OverrideException;

}
