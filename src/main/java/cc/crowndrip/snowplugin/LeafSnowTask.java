package cc.crowndrip.snowplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;

import java.util.Random;
import java.util.Set;

public class LeafSnowTask implements Runnable {

    private final SnowZoneManager zoneManager;
    private final Random random = new Random();

    // Blocks that snow should NOT be placed on top of
    private static final Set<Material> BLACKLIST = Set.of(
        Material.AIR,
        Material.WATER,
        Material.LAVA,
        Material.FIRE,
        Material.SNOW,
        Material.POWDER_SNOW,
        Material.TORCH,
        Material.WALL_TORCH,
        Material.REDSTONE_TORCH,
        Material.REDSTONE_WALL_TORCH,
        Material.SOUL_TORCH,
        Material.SOUL_WALL_TORCH,
        Material.GLASS,
        Material.GLASS_PANE,
        Material.ICE,
        Material.PACKED_ICE,
        Material.BLUE_ICE,
        Material.BARRIER,
        Material.STRUCTURE_VOID
    );

    private static final int MAX_SNOW_LAYER = 3; // max snow layer level (1-8, we cap at 3)

    public LeafSnowTask(SnowZoneManager zoneManager) {
        this.zoneManager = zoneManager;
    }

    @Override
    public void run() {
        for (SnowZone zone : zoneManager.getZones()) {
            Location center = zone.getCenter();
            int radius = zone.getRadius();

            // Check 30 random blocks per zone per tick
            for (int i = 0; i < 30; i++) {
                int dx = random.nextInt(radius * 2 + 1) - radius;
                int dz = random.nextInt(radius * 2 + 1) - radius;
                // Full vertical range of the world
                int dy = random.nextInt(radius * 2 + 1) - radius;

                Location checkLoc = center.clone().add(dx, dy, dz);

                // Skip if outside radius (sphere check)
                if (checkLoc.distanceSquared(center) > (double) radius * radius) continue;

                // Skip if out of world bounds
                if (checkLoc.getBlockY() < center.getWorld().getMinHeight() ||
                    checkLoc.getBlockY() > center.getWorld().getMaxHeight() - 2) continue;

                Block block = checkLoc.getBlock();

                // Skip blacklisted blocks and air
                if (block.getType() == Material.AIR || BLACKLIST.contains(block.getType())) continue;

                Block above = block.getRelative(0, 1, 0);

                // If above is air, place snow layer
                if (above.getType() == Material.AIR) {
                    if (random.nextFloat() < 0.25f) {
                        above.setType(Material.SNOW);
                        Snow snowData = (Snow) above.getBlockData();
                        snowData.setLayers(1);
                        above.setBlockData(snowData);
                    }
                }
                // If above already has snow, try to increase layer up to max
                else if (above.getType() == Material.SNOW) {
                    Snow snowData = (Snow) above.getBlockData();
                    if (snowData.getLayers() < MAX_SNOW_LAYER) {
                        if (random.nextFloat() < 0.1f) {
                            snowData.setLayers(snowData.getLayers() + 1);
                            above.setBlockData(snowData);
                        }
                    }
                }
            }
        }
    }
}
