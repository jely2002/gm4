package com.belka.spigot.gm4.customTerrain;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.function.Consumer;

public class UpdatableBlock {
    private Location location;
    private Material material;
	private boolean update;
    private Consumer<Block> consumer;

    UpdatableBlock(Location location, Material material, boolean update) {
        this.location = location;
        this.material = material;
        this.update = update;
    }

    public Location getLocation() {
        return location;
    }

	public void setMaterial(Material material) {
		this.material = material;
	}
    public Material getMaterial() {
        return material;
    }

	public void setUpdate(boolean update) {
		this.update = update;
	}
	public boolean isUpdate() {
		return update;
	}

	public void setConsumer(Consumer<Block> consumer) {
        this.consumer = consumer;
    }
    public Consumer<Block> getConsumer() {
        return consumer;
    }
}
