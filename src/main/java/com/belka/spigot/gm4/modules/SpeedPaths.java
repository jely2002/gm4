package com.belka.spigot.gm4.modules;

import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpeedPaths implements Module, Listener {

	private MainClass mc;
	private Material pathBlock = Material.GRASS_PATH;
	private int speedFactor = 2;
	private List<Player> speedPlayers = new ArrayList<>();

	public SpeedPaths(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public Setting getSetting() { return new Setting("Speed Paths", Material.GRASS_PATH); }

	@Override
	public void init(MainClass mc) {
		loadValues();
	}

	@Override
	public void reload() {
		loadValues();
	}

	private void loadValues() {
		if (mc.getStorage().config().contains("SpeedPaths.pathBlock")) pathBlock = Material.getMaterial(mc.getStorage().config().getString("SpeedPaths.pathBlock"));
		if (mc.getStorage().config().contains("SpeedPaths.speedFactor")) speedFactor = mc.getStorage().config().getInt("SpeedPaths.speedFactor");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!mc.getStorage().config().getBoolean("SpeedPaths.enabled")) return;
		if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
		Location pLoc = e.getPlayer().getLocation().getBlock().getLocation();
		Location block1 = new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY() - 1, pLoc.getZ());
		Location block2 = new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY() - 2, pLoc.getZ());
		if (pLoc.getBlock().getType() == pathBlock || block1.getBlock().getType() == pathBlock || block2.getBlock().getType() == pathBlock) {
			if(e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) return;
			if(speedPlayers.contains(e.getPlayer())) return;
			speedPlayers.add(e.getPlayer());
			e.getPlayer().setWalkSpeed(e.getPlayer().getWalkSpeed() * speedFactor);
		}
		else if (speedPlayers.contains(e.getPlayer())) {
			speedPlayers.remove(e.getPlayer());
			e.getPlayer().setWalkSpeed(0.2f);
		}
	}
}
