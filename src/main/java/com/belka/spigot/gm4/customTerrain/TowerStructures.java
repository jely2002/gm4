package com.belka.spigot.gm4.customTerrain;

import api.Structure;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TowerStructures {
	private MainClass mc;
	public TowerStructures(MainClass mc) {
		this.mc = mc;
	}

	Structure getStructure(Chunk c, int y) {
		ChunkSnapshot cs = c.getChunkSnapshot(true, true, false);
		List<BiomeGroup> biomeGroupList = BiomeGroup.getBiomeGroups(cs.getBiome(8, 8), BiomeGroup.OCEAN, BiomeGroup.DESERT, BiomeGroup.BADLANDS, BiomeGroup.SNOWY);

		Structure structure = null;
		Bukkit.broadcastMessage("Y " + y);
		if (y >= 60) {
			if (biomeGroupList.contains(BiomeGroup.OCEAN)) {
				Bukkit.broadcastMessage("OCEAN");
				if (c.getBlock(8, y, 8).getType() == Material.WATER) {
					//c.getBlock(8, 40, 8).getType() == Material.WATER && XXX && c.getBlock(8, 3, 8).getType() == Material.BEDROCK && c.getBlock(8, 4, 8).getType() == Material.BEDROCK
					Bukkit.broadcastMessage("ship");
					structure = getStructure("ship", "GM4 Ship", new Vector(-4, -3, -11));//TODO NBT NPE ERROR
				}
			}
			if (y >= 70) {//TODO back to 80
				if (biomeGroupList.contains(BiomeGroup.DESERT)) {
					Bukkit.broadcastMessage("DESERT");
					if (c.getBlock(8, 4, 8).getType() == Material.BEDROCK) {
						if (c.getBlock(8, 3, 8).getType() == Material.BEDROCK) structure = getStructure("spruce_scaffold", "Spruce Scaffold Tower", new Vector(-2, -1, -2));//WORKS
						else structure = getStructure("desert2", "Large Desert Tower", new Vector(-4, -1, -4));//TODO NBT NPE ERROR
					}
					else structure = getStructure("desert", "Tall Desert Tower", new Vector(-3, -1, -3));//TODO NBT NPE ERROR
				}
				else if (biomeGroupList.contains(BiomeGroup.BADLANDS)) {
					Bukkit.broadcastMessage("BADLANDS");
					if (c.getBlock(8, 4, 8).getType() == Material.BEDROCK) {
						if (c.getBlock(8, 3, 8).getType() == Material.BEDROCK) structure = getStructure("badlands3", "Acacia Tower", new Vector(-4, -1, -4));//WORKS
						else structure = getStructure("badlands2", "Large Badlands Tower", new Vector(-4, -1, -4));//TODO NBT NPE ERROR
					}
					else structure = getStructure("badlands", "Tall Badlands Tower", new Vector(-3, -1, -3));//WORKS
				}
				else if (biomeGroupList.contains(BiomeGroup.SNOWY)) {
					Bukkit.broadcastMessage("SNOWY");
					if (c.getBlock(8, 4, 8).getType() == Material.BEDROCK) {
						if (c.getBlock(8, 3, 8).getType() == Material.BEDROCK) structure = getStructure("spruce_scaffold", "Spruce Scaffold Tower", new Vector(-2, -1, -2));//WORKS
						else structure = getStructure("snowy2", "Large Snowy Tower", new Vector(-3, -1, -3));//TODO NBT NPE ERROR
					}
					else structure = getStructure("snowy", "Tall Snowy Tower", new Vector(-5, -1, -5));//WORKS
				}
				else {
					Bukkit.broadcastMessage("DEFAULT");
					if (c.getBlock(8, 4, 8).getType() == Material.BEDROCK) {
						if (c.getBlock(8, 3, 8).getType() == Material.BEDROCK) structure = getStructure("default4", "Stone Tower", new Vector(-4, -1, -4));//TODO NBT NPE ERROR
						else structure = getStructure("default3", "Dark Oak Tower", new Vector(-2, -1, -2));//TODO NBT NPE ERROR
					}
					else {
						if (c.getBlock(8, 3, 8).getType() == Material.BEDROCK) structure = getStructure("default2", "Large Default Tower", new Vector(-4, -1, -4));//WORKS
						else structure = getStructure("default", "Tall Default Tower", new Vector(-3, -1, -3));//TODO NBT NPE ERROR
					}
				}

			}
		}
		return structure;
	}

	private Structure getStructure(String fileName, String name, Vector offset) {
		Bukkit.broadcastMessage(fileName + " " + name);
		try {
			File file = mc.getResourceAsFile("custom_terrain/tower_structures/" + fileName + ".nbt");
			Structure structure = new Structure(file, name);
			structure.setOffset(offset);
			return structure;
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
