package cc.crowndrip.snowplugin;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class SnowZoneManager {

    private final List<SnowZone> zones = new ArrayList<>();
    private final SnowPlugin plugin;

    public SnowZoneManager(SnowPlugin plugin) {
        this.plugin = plugin;
    }

    public void addZone(Location center, int radius) {
        zones.add(new SnowZone(center, radius));

        // Force rain so snow falls naturally at high altitudes
        center.getWorld().setStorm(true);
        center.getWorld().setThundering(false);
        center.getWorld().setWeatherDuration(Integer.MAX_VALUE);

        // Change biome in the zone to snowy taiga so leaves turn white
        changeBiome(center, radius, Biome.SNOWY_TAIGA);
    }

    public boolean removeZoneAt(Location loc) {
        SnowZone found = null;
        for (SnowZone zone : zones) {
            if (zone.contains(loc)) {
                found = zone;
                break;
            }
        }
        if (found != null) {
            // Restore original biome when zone is removed
            changeBiome(found.getCenter(), found.getRadius(), Biome.PLAINS);
            zones.remove(found);
            return true;
        }
        return false;
    }

    private void changeBiome(Location center, int radius, Biome biome) {
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        // Biomes are set per 4x4x4 section in 1.18+
        // We iterate in steps of 4 to cover the zone
        for (int x = cx - radius; x <= cx + radius; x += 4) {
            for (int z = cz - radius; z <= cz + radius; z += 4) {
                for (int y = center.getWorld().getMinHeight(); y < center.getWorld().getMaxHeight(); y += 4) {
                    Location checkLoc = new Location(center.getWorld(), x, y, z);
                    if (checkLoc.distanceSquared(new Location(center.getWorld(), cx, cy, cz)) <= (double) radius * radius) {
                        center.getWorld().setBiome(x, y, z, biome);
                    }
                }
            }
        }

        // Refresh chunks so clients see the biome change
        int chunkMinX = (cx - radius) >> 4;
        int chunkMaxX = (cx + radius) >> 4;
        int chunkMinZ = (cz - radius) >> 4;
        int chunkMaxZ = (cz + radius) >> 4;

        for (int chunkX = chunkMinX; chunkX <= chunkMaxX; chunkX++) {
            for (int chunkZ = chunkMinZ; chunkZ <= chunkMaxZ; chunkZ++) {
                center.getWorld().refreshChunk(chunkX, chunkZ);
            }
        }
    }

    public boolean isInSnowZone(Location loc) {
        for (SnowZone zone : zones) {
            if (zone.contains(loc)) return true;
        }
        return false;
    }

    public List<SnowZone> getZones() {
        return zones;
    }

    public void clearAll() {
        zones.clear();
    }
}
