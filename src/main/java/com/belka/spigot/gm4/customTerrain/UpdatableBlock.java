package com.belka.spigot.gm4.customTerrain;

import org.bukkit.Location;
import org.bukkit.Material;

public class UpdatableBlock {
    private Location location;
    private Material material;
    private Runnable runnable;
    private BiomeGroup biomeGroup;

    public UpdatableBlock(Location location, Material material) {
        this.location = location;
        this.material = material;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setBiomeGroup(BiomeGroup biomeGroup) {
        this.biomeGroup = biomeGroup;
    }

    public BiomeGroup getBiomeGroup() {
        return biomeGroup;
    }
}
