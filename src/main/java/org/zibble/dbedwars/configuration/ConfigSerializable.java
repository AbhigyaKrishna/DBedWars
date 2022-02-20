package org.zibble.dbedwars.configuration;

import org.zibble.dbedwars.configuration.util.Configurable;

import java.io.File;

public interface ConfigSerializable<T extends Configurable> {

    T getConfig();

    File getFile();

    void saveConfig();

    void loadFromConfig();

}
