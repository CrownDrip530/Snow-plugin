package cc.crowndrip.snowplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SnowCommand implements CommandExecutor {

    private final SnowPlugin plugin;
    private final SnowZoneManager zoneManager;

    public SnowCommand(SnowPlugin plugin, SnowZoneManager zoneManager) {
        this.plugin = plugin;
        this.zoneManager = zoneManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (!player.hasPermission("snowplugin.use")) {
            player.sendMessage("§6✦ §cYou don't have permission to do that.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§6✦ §eUsage: §f/snow <radius>");
            return true;
        }

        int radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("§6✦ §cInvalid radius. Please enter a number.");
            return true;
        }

        if (radius < 1 || radius > 200) {
            player.sendMessage("§6✦ §cRadius must be between 1 and 200.");
            return true;
        }

        zoneManager.addZone(player.getLocation(), radius);
        player.sendMessage("§6✦ §aSnow zone created with radius §f" + radius + " §ablock(s).");
        player.sendMessage("§6✦ §7Leaves in the zone will gradually turn white.");
        return true;
    }
}
