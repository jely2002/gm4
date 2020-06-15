package com.belka.spigot.gm4.customTerrain;

import api.Setting;
import api.Structure;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DangerousDungeons implements Module {
	private MainClass mc;

	public DangerousDungeons(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public Setting getSetting() { return new Setting("Dangerous Dungeons", Material.COBWEB).setPath("CustomTerrain."); }

	Structure getStructure(Chunk c) {
		ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
		List<BiomeGroup> biomeGroupList = BiomeGroup.getBiomeGroups(cs.getBiome(8, 8), BiomeGroup.OCEAN, BiomeGroup.DESERT, BiomeGroup.BADLANDS, BiomeGroup.SNOWY);

		Structure structure = null;
		if (biomeGroupList.contains(BiomeGroup.OCEAN)) {
			if (c.getBlock(8, 3, 8).getType() == Material.BEDROCK) structure = getStructure("ocean", "Ocean Dungeon", new Vector(-8, -7, -8));
		}
		else if (biomeGroupList.contains(BiomeGroup.DESERT)) structure = getStructure("desert", "Desert Dungeon", new Vector(-8, -7, -8));
		else if (biomeGroupList.contains(BiomeGroup.BADLANDS)) structure = getStructure("badlands", "Badlands Dungeon", new Vector(-8, -7, -8));
		else if (biomeGroupList.contains(BiomeGroup.SNOWY)) structure = getStructure("snowy", "Snowy Dungeon", new Vector(-8, -7, -8));
		else {
			if (c.getBlock(8, 4, 8).getType() == Material.BEDROCK) structure = getStructure("default2", "Library Dungeon", new Vector(-8, -7, -5));
			else structure = getStructure("default", "Default Dungeon", new Vector(-8, -7, -8));
		}
		return structure;
	}
	
	private Structure getStructure(String fileName, String name, Vector offset) {
		try {
			File file = mc.getResourceAsFile("custom_terrain/dangerous_dungeons/" + fileName + ".nbt");
			Structure structure = new Structure(file, name);
			structure.setOffset(offset);
			return structure;
		}
		catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			return null;
		}
	}
}
