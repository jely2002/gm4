package com.belka.spigot.gm4.customTerrain;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class CustomTerrain implements Listener {

	private MainClass mc;
	private CoolerCaves coolerCaves;
	public CustomTerrain(MainClass mc, CoolerCaves coolerCaves) {
		this.mc = mc;
		this.coolerCaves = coolerCaves;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!mc.getConfig().getBoolean("CustomTerrain.enabled") || !mc.getConfig().getBoolean("CustomTerrain.CoolerCaves.enabled")) return;

		if (mc.getConfig().getBoolean("CustomTerrain.CoolerCaves.enabled"))
			if (e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0)))
				coolerCaves.loadChunks(e.getPlayer().getLocation());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!mc.getConfig().getBoolean("CustomTerrain.enabled") || !mc.getConfig().getBoolean("CustomTerrain.CoolerCaves.enabled")) return;

		if (mc.getConfig().getBoolean("CustomTerrain.CoolerCaves.enabled"))
			if (e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0)))
				if (!e.getFrom().getChunk().equals(e.getTo().getChunk())) {
					coolerCaves.loadChunks(e.getTo());
				}
	}
}
