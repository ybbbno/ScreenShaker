package me.ybbbno.screenshaker;

import me.deadybbb.ybmj.PluginProvider;

public final class ScreenShaker extends PluginProvider {

    private ScreenShakerManager manager;

    @Override
    public void onLoad() {
        manager = new ScreenShakerManager(this);
    }

    @Override
    public void onEnable() {
        manager.init();

        registerCommand("shake", new ScreenShakerCommand(manager));
    }

    @Override
    public void onDisable() {
        manager.deinit();
    }

    public ScreenShakerManager getManager() {
        return manager;
    }
}
