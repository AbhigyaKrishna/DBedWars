package com.pepedevs.dbedwars.api.util;

import com.pepedevs.dbedwars.api.exceptions.OverrideException;

public interface Overridable {

    void override(Overridable override) throws OverrideException;
}
