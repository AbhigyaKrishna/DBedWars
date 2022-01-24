package com.pepedevs.dbedwars.configuration;

import com.pepedevs.radium.utils.configuration.Configurable;

import java.io.File;

public interface ConfigSerializable<T extends Configurable> {

    T getConfig();

    File getFile();

    void saveConfig();

    void loadFromConfig();

}
