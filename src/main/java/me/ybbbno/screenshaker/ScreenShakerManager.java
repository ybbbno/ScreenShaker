package me.ybbbno.screenshaker;

import me.deadybbb.ybmj.BasicManagerHandler;
import me.deadybbb.ybmj.PluginProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ScreenShakerManager extends BasicManagerHandler {
    private final Random random = new Random();

    private final Map<UUID, ShakerI> shakers = new HashMap<>();
    private BukkitTask task;

    public ScreenShakerManager(PluginProvider plugin) {
        super(plugin);
    }

    @Override
    protected void onInit() {
        shakers.clear();

        start();
    }

    @Override
    protected void onDeinit() {
        stop();

        shakers.clear();
    }

    public void set(@NotNull Player player, int ticksAmount) {
        set(player, -1, 1, -1, 1, ticksAmount);
    }

    public void set(@NotNull Player player, float minYaw, float maxYaw, float minPitch, float maxPitch, int ticksAmount) {
        UUID uuid = player.getUniqueId();
        ShakerI shaker = new ShakerI(minYaw, maxYaw, minPitch, maxPitch, ticksAmount);
        shakers.put(uuid, shaker);
    }

    public void add(@NotNull Player player, int ticksAmount) {
        UUID uuid = player.getUniqueId();
        ShakerI shaker = shakers.get(uuid);
        if (shaker != null) {
            shaker.add(ticksAmount);
        } else {
            set(player, ticksAmount);
        }
    }

    public void remove(@NotNull Player player) {
        shakers.remove(player.getUniqueId());
    }

    @Nullable
    public Integer getTicks(@NotNull Player player) {
        ShakerI shaker = shakers.get(player.getUniqueId());
        return shaker != null ? shaker.ticks() : null;
    }

    private void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            List<UUID> remove = new ArrayList<>();

            shakers.forEach((u, shaker) -> {
                if (shaker.ticks() <= 0) {
                    remove.add(u);
                    return;
                }

                Player player = Bukkit.getPlayer(u);
                if (player == null || !player.isOnline()) {
                    remove.add(u);
                    return;
                }

                shaker.add(-1);

                float yawOffset = shaker.yaw();
                float pitchOffset = shaker.pitch();

                Location location = player.getLocation();
                location.addRotation(yawOffset, pitchOffset);

                player.setRotation(location.getYaw(), location.getPitch());
            });

            for (UUID u : remove) {
                shakers.remove(u);
            }
        }, 0L, 1L);
    }

    private void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
