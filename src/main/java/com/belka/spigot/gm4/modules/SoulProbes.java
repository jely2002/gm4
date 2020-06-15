package com.belka.spigot.gm4.modules;

import api.InventoryCreator;
import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoulProbes implements Module, Listener {

	private MainClass mc;
	private InventoryCreator ic;
	private HashMap<EntityType, Integer> hostileOverworld = new HashMap<>();
    private HashMap<EntityType, Integer> hostileNether = new HashMap<>();
    private HashMap<EntityType, Integer> passive = new HashMap<>();
	private HashMap<EntityType, Integer> mobs = new HashMap<>();

	public SoulProbes(MainClass mc, InventoryCreator ic) {
		this.mc = mc;
		this.ic = ic;
	}

	@Override
	public Setting getSetting() { return new Setting("Soul Probes", Material.GHAST_SPAWN_EGG); }

	@Override
    public void init(MainClass mc) {//TODO add to config
		hostileOverworld.put(EntityType.CREEPER, 100);
		hostileOverworld.put(EntityType.SKELETON, 100);
		hostileOverworld.put(EntityType.SPIDER, 100);
		hostileOverworld.put(EntityType.ZOMBIE, 100);
		hostileOverworld.put(EntityType.SLIME, 100);
		hostileOverworld.put(EntityType.ENDERMAN, 100);
		hostileOverworld.put(EntityType.WITCH, 50);
		hostileOverworld.put(EntityType.GUARDIAN, 50);
		hostileOverworld.put(EntityType.CAVE_SPIDER, 100);
		hostileOverworld.put(EntityType.SILVERFISH, 100);
		hostileOverworld.put(EntityType.ENDERMITE, 25);

		hostileNether.put(EntityType.GHAST, 25);
		hostileNether.put(EntityType.MAGMA_CUBE, 50);
		hostileNether.put(EntityType.PIG_ZOMBIE, 100);
		hostileNether.put(EntityType.BLAZE, 100);

		passive.put(EntityType.COW, 100);
		passive.put(EntityType.MUSHROOM_COW, 25);
		passive.put(EntityType.SHEEP, 100);
		passive.put(EntityType.PIG, 100);
		passive.put(EntityType.CHICKEN, 100);
		passive.put(EntityType.RABBIT, 25);
		passive.put(EntityType.HORSE, 25);
		passive.put(EntityType.OCELOT, 25);
		passive.put(EntityType.WOLF, 25);
		passive.put(EntityType.SQUID, 50);
		passive.put(EntityType.VILLAGER, 25);

		mobs.putAll(hostileOverworld);
		mobs.putAll(hostileNether);
		mobs.putAll(passive);
	}

	private Inventory soulProbesMenu() {
		Inventory inv = Bukkit.createInventory(null, 9*3, ChatColor.GOLD + "Soul Probes Menu");

		inv.setItem(2, ic.createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "Hostile", true, "Overworld"));
		inv.setItem(4, ic.createGuiItem(Material.RED_STAINED_GLASS_PANE, ChatColor.RED, "Hostile", true, "Nether"));
		inv.setItem(6, ic.createGuiItem(Material.LIME_STAINED_GLASS_PANE, ChatColor.DARK_GREEN, "Passive", true));

		inv.setItem(22, ic.createGuiItem(Material.BARRIER, ChatColor.RED, "Go back", false, "Click to close the main menu"));

//		for(int i = 0; i < mobs.size(); i++) {
//			if(i == 45) {
//				return inv;
//			}
//			String achievement = mobs.get(i);
//			Material mat = Material.LIME_TERRACOTTA;
//			ChatColor override = null;
//			if(!has.contains(achievement)) {
//				mat = Material.CYAN_TERRACOTTA;
//				override = ChatColor.GRAY;
//			}
//			String name = mc.getStorage().config().getString("achievements." + achievement + ".name");
//			String lore = mc.getStorage().config().getString("achievements." + achievement + ".lore");
//			String reward = "Reward: " + mc.getStorage().config().getString("achievements." + achievement + ".reward");
//			inv.setItem(i, ic.createGuiItem(mat, override, name, lore, reward));
//		}
		return inv;
	}

	private Inventory mobMenu(Player p, String type) {
		HashMap<EntityType, Integer> mobs = new HashMap<>();
		String name = "";

		switch (type) {
			case "ho":
				name += ChatColor.RED + "" + ChatColor.BOLD + "Hostile" + ChatColor.GRAY + " - Overworld";
				mobs = hostileOverworld;
				break;
			case "hn":
				name += ChatColor.RED + "" + ChatColor.BOLD + "Hostile" + ChatColor.GRAY + " - Nether";
				mobs = hostileNether;
				break;
			case "p":
				name += ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Passive";
				mobs = passive;
				break;
		}

		Inventory inv = Bukkit.createInventory(null, 9*3, name);

		inv.setItem(22, ic.createGuiItem(Material.BARRIER, ChatColor.RED, "Go back", false, "Click to go back to the main menu"));

		for (Map.Entry<EntityType, Integer> mob : mobs.entrySet()) {
			inv.addItem(ic.createSPGuiSkull(mob.getKey(), mob.getValue(), p));
		}
		return inv;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
    	if (!mc.getStorage().config().getBoolean("SoulProbes.enabled")) return;
		Player p = event.getPlayer();
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
			ItemStack hand = p.getInventory().getItemInMainHand();
			if (hand != null) {
				hand = hand.clone();
				hand.setAmount(1);
				if (hand.equals(CustomItems.SOUL_PROBES_BOOK(1))) {
					p.openInventory(soulProbesMenu());
					event.setCancelled(true);
				}
				else if (hand.equals(CustomItems.EMPTY_SPAWN_EGG(1))) {
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!mc.getStorage().config().getBoolean("SoulProbes.enabled")) return;
		HumanEntity entity = e.getWhoClicked();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			String invName = ChatColor.stripColor(e.getView().getTitle().toLowerCase());
			if (invName.contains("soul probes")) {
				e.setCancelled(true);
				ItemStack clicked = e.getCurrentItem();
				if (clicked != null) {
					if (clicked.getType() == Material.BARRIER) {
						p.closeInventory();
						p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
					}
					else if (clicked.getType() == Material.RED_STAINED_GLASS_PANE) {
						p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
						String lore = ChatColor.stripColor(clicked.getItemMeta().getLore().get(0).toLowerCase());
						if (lore.contains("overworld")) {
							p.openInventory(mobMenu(p, "ho"));
						}
						else if (lore.contains("nether")) {
							p.openInventory(mobMenu(p, "hn"));
						}
					}
					else if (clicked.getType() == Material.LIME_STAINED_GLASS_PANE) {
						p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
						p.openInventory(mobMenu(p, "p"));

					}
				}
			}
			else if (invName.contains("hostile - overworld") || invName.contains("hostile - nether") || invName.contains("passive")) {
				e.setCancelled(true);
				ItemStack clicked = e.getCurrentItem();
				if (clicked != null) {
					if (clicked.getType() == Material.BARRIER) {
						p.openInventory(soulProbesMenu());
						p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
					}
					else if (clicked.getType() == Material.PLAYER_HEAD) {
						String amount = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
						int x = Integer.parseInt(amount.split("/")[0]);
						int y = Integer.parseInt(amount.split("/")[1]);
						if (x >= y) {
							if (hasEgg(p)) {
								String eName = ChatColor.stripColor(clicked.getItemMeta().getLore().get(0)).toUpperCase().replace(' ', '_');
								p.getInventory().addItem(new ItemStack(Material.getMaterial(eName + "_SPAWN_EGG")));
								removeEgg(p);
								p.closeInventory();
								p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);

								String loc = "SoulProbes." + p.getUniqueId() + "." + eName.toUpperCase();
								mc.getStorage().data().set(loc, x - y);
								mc.getStorage().saveData();
							}
							else p.sendMessage(ChatColor.RED + "You don't have any empty spawn eggs!");
						}
						else p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
					}
				}
			}
		}
	}

	private boolean hasEgg(Player p) {
		ArrayList<ItemStack> inv = new ArrayList<>();
		for (ItemStack is : p.getInventory().getContents()) {
			if (is != null)  {
				ItemStack item = is.clone();
				item.setAmount(1);
				inv.add(item);
			}
		}
		return inv.contains(CustomItems.EMPTY_SPAWN_EGG(1));
	}

	private void removeEgg(Player p) {
		for (ItemStack is : p.getInventory().getContents()) {
			if (is != null)  {
				ItemStack item = is.clone();
				item.setAmount(1);
				if (item.equals(CustomItems.EMPTY_SPAWN_EGG(1))) {
					is.setAmount(is.getAmount() - 1);
				}
			}
		}
	}

	@EventHandler
	public void onKill(EntityDeathEvent e) {
		if (!mc.getStorage().config().getBoolean("SoulProbes.enabled")) return;
		Entity dead = e.getEntity();
		Player p = e.getEntity().getKiller();
		if (p != null) {
			String loc = "SoulProbes." + p.getUniqueId() + "." + dead.getType().name();
			mc.getStorage().data().set(loc, mc.getStorage().data().getInt(loc, 0) + 1);
			mc.getStorage().saveData();
		}
	}
}
