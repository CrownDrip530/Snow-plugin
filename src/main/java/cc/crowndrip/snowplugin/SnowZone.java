package cc.crowndrip.snowplugin;

import org.bukkit.Location;

public class SnowZone {
    private final Location center;
    private final int radius;

    public SnowZone(Location center, int radius) {
        this.center = center.clone();
        this.radius = radius;
    }

    public Location getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    public boolean contains(Location loc) {
        if (!loc.getWorld().equals(center.getWorld())) return false;
        return loc.distanceSquared(center) <= (double) radius * radius;
    }
}
