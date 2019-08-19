package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XPStorage implements Listener {

	private MainClass mc;
	private Map<UUID, Integer> timers = new HashMap<>();

	public XPStorage(MainClass mc) {
		this.mc = mc;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(!mc.getConfig().getBoolean("XpStorage.enabled")) return;
		if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
		Block bTo = e.getTo().getBlock();
		Block bFrom = e.getFrom().getBlock();
		Player p = e.getPlayer();
		if (bTo.getType() == Material.ENDER_CHEST) {
			editLevel(p, "ABOVE");
		}
		else if (bTo.getRelative(0,2,0).getType() == Material.ENDER_CHEST) {
			editLevel(p, "BELOW");
		}
		if (bFrom.getType() == Material.ENDER_CHEST || bFrom.getRelative(0,2,0).getType() == Material.ENDER_CHEST) {
			if (timers.containsKey(p.getUniqueId()))
				stopTimer(p);
		}
	}

	private void editLevel(Player p, String place) {
		UUID uuid = p.getUniqueId();
		if (!mc.storage().data().contains("XpStorage." + uuid)) {
			mc.storage().data().set("XpStorage." + uuid, 0);
			mc.storage().saveData();
		}
		final int fStoredLevel = mc.storage().data().getInt("XpStorage." + uuid);

		if (place.equals("ABOVE")) {
			int addXP = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
				int level = p.getLevel();
				int storedLevel = fStoredLevel;
				public void run() {
					if (p.isSneaking() && level >= 50) {
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						storedLevel = storedLevel + 50;
						mc.storage().data().set("XpStorage." + uuid, storedLevel);
						mc.storage().saveData();
						level = level - 50;
						p.setLevel(level);
					}
					else if (level > 0) {
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						storedLevel = storedLevel + 1;
						mc.storage().data().set("XpStorage." + uuid, storedLevel);
						mc.storage().saveData();
						level = level - 1;
						p.setLevel(level);
					}
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "" +  storedLevel));
				}
			}, 0,20L);

			timers.put(uuid, addXP);
		}
		else if (place.equals("BELOW")) {
			int removeXP = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
				int level = p.getLevel();
				int storedLevel = fStoredLevel;
				public void run() {
					if (p.isSneaking() && storedLevel >= 50) {
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						storedLevel = storedLevel - 50;
						mc.storage().data().set("XpStorage." + uuid, storedLevel);
						mc.storage().saveData();
						level = level + 50;
						p.setLevel(level);
					}
					else if (storedLevel > 0) {
						p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						storedLevel = storedLevel - 1;
						mc.storage().data().set("XpStorage." + uuid, storedLevel);
						mc.storage().saveData();
						level = level + 1;
						p.setLevel(level);
					}
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "" +  storedLevel));
				}
			}, 0,20L);

			timers.put(uuid, removeXP);
		}
	}

	private void stopTimer(Player p) {
		int tid = timers.get(p.getUniqueId());
		mc.getServer().getScheduler().cancelTask(tid);
		timers.remove(p.getUniqueId());
	}
}
