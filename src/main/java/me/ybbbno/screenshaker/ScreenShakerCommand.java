package me.ybbbno.screenshaker;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.deadybbb.ybmj.LegacyTextHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScreenShakerCommand implements BasicCommand {

    private final ScreenShakerManager manager;

    public ScreenShakerCommand(ScreenShakerManager manager) {
        this.manager = manager;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender sender = source.getSender();

        if (args.length < 2) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Usage: /shake <player|@a> <ticks> [minYaw maxYaw minPitch maxPitch]");
            return;
        }

        String targetArg = args[0];

        int ticks;
        try {
            ticks = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>Ticks must be a valid integer!");
            return;
        }

        float minYaw = -1, maxYaw = 1, minPitch = -1, maxPitch = 1;
        boolean hasCustomBounds = false;

        if (args.length >= 6) {
            try {
                minYaw = Float.parseFloat(args[2]);
                maxYaw = Float.parseFloat(args[3]);
                minPitch = Float.parseFloat(args[4]);
                maxPitch = Float.parseFloat(args[5]);
            } catch (NumberFormatException e) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Yaw and Pitch bounds must be valid numbers!");
                return;
            }

            if (minYaw >= maxYaw || minPitch >= maxPitch) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Min bounds must be strictly less than Max bounds!");
                return;
            }
            hasCustomBounds = true;
        } else if (args.length > 2) {
            LegacyTextHandler.sendFormattedMessage(sender, "<red>You must provide all 4 bounds (minYaw maxYaw minPitch maxPitch) or none!");
            return;
        }

        List<Player> targets = new ArrayList<>();

        if (targetArg.equalsIgnoreCase("@a")) {
            targets.addAll(Bukkit.getOnlinePlayers());
        } else {
            Player target = Bukkit.getPlayer(targetArg);
            if (target == null) {
                LegacyTextHandler.sendFormattedMessage(sender, "<red>Player is not found or online!");
                return;
            }
            targets.add(target);
        }

        for (Player player : targets) {
            if (hasCustomBounds) {
                manager.set(player, minYaw, maxYaw, minPitch, maxPitch, ticks);
            } else {
                manager.add(player, ticks);
            }
        }

        LegacyTextHandler.sendFormattedMessage(sender, "<green>Shake is successfully started for " + targets.size() + " players");
    }

    @Override
    public Collection<String> suggest(@NotNull CommandSourceStack source, String[] args) {
        if (args.length == 0) {
            List<String> suggestions = new ArrayList<>();

            suggestions.add("@a");
            for (Player p : Bukkit.getOnlinePlayers()) {
                suggestions.add(p.getName());
            }

            return suggestions;
        }

        if (args.length == 1) {
            String arg = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            suggestions.add("@a");

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(arg)) {
                    suggestions.add(p.getName());
                }
            }
            return suggestions;
        }

        if (args.length == 2) {
            return List.of("20", "40", "60", "100");
        }

        if (args.length == 3) return List.of("-1");
        if (args.length == 4) return List.of("1");
        if (args.length == 5) return List.of("-1");
        if (args.length == 6) return List.of("1");

        return List.of();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("commandshaker.shake");
    }
}
