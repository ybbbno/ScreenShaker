package me.ybbbno.screenshaker;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScreenShakerAPI {
    private static ScreenShakerAPI instance;

    private final ScreenShakerManager manager;

    private ScreenShakerAPI(ScreenShaker plugin) {
        this.manager = plugin.getManager();
    }

    /**
     * Gets the singleton instance of the API.
     * @return The API instance, or null if the plugin is not loaded/enabled.
     */
    @Nullable
    public static ScreenShakerAPI getAPI() {
        if (instance != null) {
            return instance;
        }

        Plugin p = Bukkit.getPluginManager().getPlugin("ScreenShaker");
        if (p instanceof ScreenShaker screenShaker) {
            instance = new ScreenShakerAPI(screenShaker);
            return instance;
        }
        return null;
    }

    public static void invalidate() {
        instance = null;
    }

    public ScreenShakerManager getManager() {
        return manager;
    }

    /**
     * Applies the default screen shake to a player (adds to existing duration).
     */
    public void shake(@NotNull Player player, int ticks) {
        manager.add(player, ticks);
    }

    /**
     * Applies a custom screen shake to a player (replaces existing shake).
     */
    public void shake(@NotNull Player player, float minYaw, float maxYaw, float minPitch, float maxPitch, int ticks) {
        manager.set(player, minYaw, maxYaw, minPitch, maxPitch, ticks);
    }

    /**
     * Immediately stops the screen shake for a player.
     */
    public void stopShake(@NotNull Player player) {
        manager.remove(player);
    }

    /**
     * Checks if a player is currently being shaken.
     */
    public boolean isShaking(@NotNull Player player) {
        return manager.getTicks(player) != null;
    }
}
