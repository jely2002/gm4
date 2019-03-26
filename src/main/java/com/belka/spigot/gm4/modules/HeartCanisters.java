package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HeartCanisters implements Listener, Initializable {

	private MainClass mc;
	private boolean enabled = true;

	public HeartCanisters(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		if(!mc.getConfig().getBoolean("HeartCanisters.enabled")) enabled = false;
		if(!mc.getConfig().getBoolean("CustomCrafter.enabled")) {
			System.out.println(ConsoleColor.RED + "Enable CustomCrafter in order for HeartCanisters to work!");
			enabled = false;
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		updateCanisters(e.getPlayer());
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof Player) {
			Player p = (Player) e.getInventory().getHolder();
			updateCanisters(p);
		}
	}
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getInventory().getHolder() instanceof Player) {
			Player p = (Player) e.getInventory().getHolder();
			updateCanisters(p);
		}
	}

	private void updateCanisters(Player p) {
		if (!enabled) return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(mc, new Runnable() {
			boolean hasCansiter = false;
			int levels = 0;
			public void run() {
				ItemStack[] inv = p.getInventory().getContents();

				ItemStack tier1 = getCanisterT1(inv);
				if (tier1 != null) {
					Bukkit.broadcastMessage("HC1");
					hasCansiter = true;
					int amount = tier1.getAmount();
					if (amount >= 5) amount = 5;
					levels = amount;

					ItemStack tier2 = getCanisterT2(inv);
					if (tier2 != null) {
						Bukkit.broadcastMessage("HC2");
						int amount2 = tier2.getAmount();
						if (amount2 >= 5) amount2 = 5;
						levels = levels + amount2;
					}
				}

				if (hasCansiter) {
					Bukkit.broadcastMessage(levels + "");
					int level = levels - 1;
					p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, level, true, false));
				}
				else p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
			}
		}, 1L);
	}

	private boolean isCanister(ItemStack item) {
		if (item == null) return false;
		ItemStack is = item.clone();
		is.setAmount(1);
		return (is.equals(CustomItems.HEART_CANISTER_TIER_1(1)) || is.equals(CustomItems.HEART_CANISTER_TIER_2(1)));
	}
	private ItemStack getCanisterT1(ItemStack[] inv) {
		for (ItemStack item : inv) {
			if (item == null) continue;
			Bukkit.broadcastMessage(item.toString());
			ItemStack is = item.clone();
			is.setAmount(1);
			if (is.equals(CustomItems.HEART_CANISTER_TIER_1(1))) {
				return item;
			}
		}
		return null;
	}
	private ItemStack getCanisterT2(ItemStack[] inv) {
		for (ItemStack item : inv) {
			if (item == null) continue;
			ItemStack is = item.clone();
			is.setAmount(1);
			if (is.equals(CustomItems.HEART_CANISTER_TIER_2(1))) {
				return item;
			}
		}
		return null;
	}
}
