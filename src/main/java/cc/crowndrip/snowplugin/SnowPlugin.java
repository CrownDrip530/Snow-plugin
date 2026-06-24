package cc.crowndrip.snowplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class SnowPlugin extends JavaPlugin {

    private SnowZoneManager zoneManager;

    @Override
    public void onEnable() {
        zoneManager = new SnowZoneManager(this);

        getCommand("snow").setExecutor(new SnowCommand(this, zoneManager));
        getCommand("snowclear").setExecutor(new SnowClearCommand(this, zoneManager));

        getServer().getPluginManager().registerEvents(new WeatherListener(zoneManager), this);

        // Start the leaf-whitening task — runs every 5 seconds
        getServer().getScheduler().runTaskTimer(this, new LeafSnowTask(zoneManager), 0L, 100L);

        getLogger().info("SnowPlugin enabled — CrownDrip Anarchy SMP");
    }

    @Override
    public void onDisable() {
        zoneManager.clearAll();
        getLogger().info("SnowPlugin disabled.");
    }
}
