package com.belka.spigot.gm4.customTerrain;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.Arrays;
import java.util.List;

public class CustomTerrain implements Listener {

	private MainClass mc;
	public CustomTerrain(MainClass mc) {
		this.mc = mc;
	}
	int i = 0;
	@EventHandler
	public void onLoad(ChunkLoadEvent e) {
		if (!mc.getConfig().getBoolean("CustomTerrain.enabled") || !mc.getConfig().getBoolean("CustomTerrain.CoolerCaves")) return;

		Chunk c = e.getChunk();
		if (e.getWorld().equals(Bukkit.getWorlds().get(0))) {
			if (mc.getConfig().getBoolean("CustomTerrain.CoolerCaves")) {
//				i++;
//				Bukkit.broadcastMessage(i + ": " + c.getX() + " " + c.getZ());
//				Bukkit.getScheduler().runTaskAsynchronously(mc, () -> {
//					CoolerCaves.loadChunk(c);
//				});
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!mc.getConfig().getBoolean("CustomTerrain.enabled") || !mc.getConfig().getBoolean("CustomTerrain.CoolerCaves")) return;

		Chunk c = e.getPlayer().getLocation().getChunk();
		if (e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0))) {
			if (mc.getConfig().getBoolean("CustomTerrain.CoolerCaves")) {
//				i++;
//				Bukkit.broadcastMessage(i + ": " + c.getX() + " " + c.getZ());
//				Bukkit.getScheduler().runTaskAsynchronously(mc, () -> {
					CoolerCaves.loadChunk(c);
//				});
			}
		}
	}
}
