package com.belka.spigot.gm4.customTerrain;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Objects;

public class DangerousDungeons {

	private MainClass mc;
	private CustomTerrain ct;
	public DangerousDungeons(MainClass mc, CustomTerrain ct) {
		this.mc = mc;
		this.ct = ct;
	}

	void loadChunk(Chunk c) {
		Block corner = c.getBlock(0,6,0);
		if (corner.getType() != Material.GRANITE) return;

		Location loc1 = new Location(c.getWorld(), corner.getX()+1, 10, corner.getZ()+1);
		Location loc2 = new Location(c.getWorld(), corner.getX()-1, 19, corner.getZ()-1);

		for (int i = 0; i < 5; i++) {
			if (Objects.requireNonNull(Helper.getBlocksBetween(loc1, loc2)).stream().anyMatch(b -> b.getType() == Material.AIR)) {
				//use Structure https://www.spigotmc.org/threads/structure-block-place-function.344848/
				return;
			}
			loc1.add(0, 10, 0);
			loc2.add(0, 10, 0);
		}
	}
}
