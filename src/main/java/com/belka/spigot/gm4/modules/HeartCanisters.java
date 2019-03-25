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
import org.bukkit.inventory.Inventory;
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
		if (isCanister(e.getCurrentItem()) && e.getInventory().getHolder() instanceof Player) {
			Player p = (Player) e.getInventory().getHolder();
			updateCanisters(p);
		}
	}
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (isCanister(e.getNewItems().get(0)) && e.getInventory().getHolder() instanceof Player) {
			Player p = (Player) e.getInventory().getHolder();
			updateCanisters(p);
		}
	}

	private void updateCanisters(Player p) {
		if (!enabled) return;
		boolean hasCansiter = false;
		int levels = 0;
		Inventory inv = p.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (item == null) continue;
			if (isCanisterT1(item)) {
				hasCansiter = true;
				Bukkit.broadcastMessage("HC1");
				int amount = item.getAmount();
				if (amount >= 5) amount = 5;
				levels = amount * 2;
			}
			else if (isCanisterT2(item)) {
				hasCansiter = true;
				Bukkit.broadcastMessage("HC2");
				int amount = item.getAmount();
				if (amount >= 5) amount = 5;
				levels = amount * 2;
			}
		}
		if (hasCansiter) {
			Bukkit.broadcastMessage(levels + "");
			int level = levels/10;
			p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, level, true, false));
		}
		else p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
	}

	private boolean isCanister(ItemStack item) {
		if (item == null) return false;
		ItemStack is = item.clone();
		is.setAmount(1);
		return (is.equals(CustomItems.HEART_CANISTER_TIER_1(1)) || is.equals(CustomItems.HEART_CANISTER_TIER_2(1)));
	}
	private boolean isCanisterT1(ItemStack item) {
		if (item == null) return false;
		ItemStack is = item.clone();
		is.setAmount(1);
		return (is.equals(CustomItems.HEART_CANISTER_TIER_1(1)));
	}
	private boolean isCanisterT2(ItemStack item) {
		if (item == null) return false;
		ItemStack is = item.clone();
		is.setAmount(1);
		return (is.equals(CustomItems.HEART_CANISTER_TIER_2(1)));
	}
}
