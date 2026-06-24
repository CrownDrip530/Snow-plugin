package cc.crowndrip.snowplugin;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WeatherListener implements Listener {

    private final SnowZoneManager zoneManager;

    public WeatherListener(SnowZoneManager zoneManager) {
        this.zoneManager = zoneManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        if (zoneManager.isInSnowZone(loc)) {
            // Spawn snowflake particles around the player to simulate snow
            loc.getWorld().spawnParticle(
                Particle.SNOWFLAKE,
                loc.clone().add(0, 3, 0),
                15,   // count
                3, 0.5, 3,  // spread x, y, z
                0.01  // speed
            );
        }
    }
}
