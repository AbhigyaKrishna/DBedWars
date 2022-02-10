package com.pepedevs.dbedwars.configuration.util;

import com.pepedevs.dbedwars.configuration.util.Loadable;
import com.pepedevs.dbedwars.configuration.util.Saveable;

/**
 * Simple interface that represents Objects that can be loaded by {@link com.pepedevs.dbedwars.configuration.util.Loadable} interface, and
 * saved by {@link com.pepedevs.dbedwars.configuration.util.Saveable} interface.
 */
public interface Configurable extends Loadable, Saveable {}
