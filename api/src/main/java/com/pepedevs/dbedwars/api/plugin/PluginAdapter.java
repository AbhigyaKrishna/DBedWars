package com.pepedevs.dbedwars.api.plugin;

/**
 * An convenience implementation of {@link Plugin}. Derive from this and only override what you
 * need.
 */
public abstract class PluginAdapter extends Plugin {

    @Override
    public PluginDependence[] getDependences() {
        return null;
    }

    @Override
    protected boolean setUpConfig() {
        return true;
    }

    @Override
    protected boolean setUpHandlers() {
        return true;
    }

    @Override
    protected boolean setUpCommands() {
        return true;
    }

    @Override
    protected boolean setUpListeners() {
        return true;
    }
}
