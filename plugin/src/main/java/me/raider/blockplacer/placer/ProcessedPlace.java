package me.raider.blockplacer.placer;

import org.bukkit.Location;
import org.bukkit.Material;

public class ProcessedPlace {

    private final Location location;
    private final Material material;

    public ProcessedPlace(Location location, Material material) {
        this.location = location;
        this.material = material;
    }

    public Location getLocation() {
        return this.location;
    }

    public Material getMaterial() {
        return this.material;
    }

}
