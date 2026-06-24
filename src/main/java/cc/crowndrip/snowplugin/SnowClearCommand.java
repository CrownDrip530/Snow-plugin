package cc.crowndrip.snowplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SnowClearCommand implements CommandExecutor {

    private final SnowPlugin plugin;
    private final SnowZoneManager zoneManager;

    public SnowClearCommand(SnowPlugin plugin, SnowZoneManager zoneManager) {
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

        boolean removed = zoneManager.removeZoneAt(player.getLocation());
        if (removed) {
            player.sendMessage("§6✦ §aSnow zone removed.");
        } else {
            player.sendMessage("§6✦ §cYou are not standing in a snow zone.");
        }
        return true;
    }
}
