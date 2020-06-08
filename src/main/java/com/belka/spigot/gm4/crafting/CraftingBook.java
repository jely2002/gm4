package com.belka.spigot.gm4.crafting;

import api.*;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingBook implements Listener {

	private MainClass mc;
	private InventoryCreator ic;
	public CraftingBook(MainClass mc, InventoryCreator ic) {
		this.mc = mc;
		this.ic = ic;
	}

	private Map<Player, String> active = new HashMap<>();
	private Map<Player, String> previous = new HashMap<>();
	private Map<Player, List<Recipe>> selectedRecipes = new HashMap<>();

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
//		if (!mc.getStorage().config().getBoolean("SoulProbes.enabled")) return;
		Player p = event.getPlayer();
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
			ItemStack hand = p.getInventory().getItemInMainHand();
			hand = hand.clone();
			hand.setAmount(1);
			if (hand.equals(CustomItems.CRAFTING_RECIPE_BOOK(1))) {
				p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				previous.put(p, null);
				active.put(p, "recipeBookMenu");
				p.openInventory(recipeBookMenu());
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		HumanEntity entity = e.getWhoClicked();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Gamemode 4 Crafting Recipes")) {
				e.setCancelled(true);
				ItemStack clicked = e.getCurrentItem();
				if (clicked != null) {
					if (clicked.getType() == Material.BARRIER) {//Full close
						p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
						active.remove(p);
						previous.remove(p);
						selectedRecipes.remove(p);
						p.closeInventory();
					}
					else {
						p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
						String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
						previous.put(p, "recipeBookMenu");
						active.put(p, "recipeGroup:" + name);
						p.openInventory(recipeGroup(p, name, 1));
					}
				}
			}
			else {
				if (active.containsKey(p) && active.get(p).startsWith("recipeGroup")) {
					e.setCancelled(true);
					ItemStack clicked = e.getCurrentItem();
					if (clicked != null) {
						ItemMeta clickedMeta = clicked.getItemMeta();
						if (clicked.getType() == Material.BARRIER && ChatColor.stripColor(clickedMeta.getDisplayName()).equalsIgnoreCase("Go back")) {
							p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
							previous.put(p, active.get(p));
							active.put(p, "recipeBookMenu");
							selectedRecipes.remove(p);
							p.openInventory(recipeBookMenu());
						}
						else {
							if (selectedRecipes.containsKey(p)) {
								String name = Helper.getItemName(clicked);
								Recipe recipe = selectedRecipes.get(p).stream().filter(o -> {
									String itemName = Helper.getItemName(o.getResult());
									if (itemName.equalsIgnoreCase(name)) {
										if (itemName.equalsIgnoreCase("Heart Canister")) {
											return ChatColor.stripColor(clickedMeta.getLore().get(0)).equalsIgnoreCase(ChatColor.stripColor(o.getResult().getItemMeta().getLore().get(0)));
										}
										if (clickedMeta.getLore() != null) {
											boolean matches = true;
											for (String loreItem : clickedMeta.getLore()) {
												loreItem = ChatColor.stripColor(loreItem);
												String recipeItem = loreItem.substring(3);
												Bukkit.broadcastMessage(recipeItem);
												if (o instanceof ShapedRecipe) {
													if (((ShapedRecipe) o).getIngredientMap().values().stream().noneMatch(a -> {
														if (a == null) return false;
														return Helper.getItemName(a).equalsIgnoreCase(recipeItem);
													})) matches = false;
												}
												else if (o instanceof ShapelessRecipe) {
													if (((ShapelessRecipe) o).getIngredientList().stream().noneMatch(a -> Helper.getItemName(a).equalsIgnoreCase(recipeItem))) matches = false;
												}
											}
											return matches;
										}
									}
									return itemName.equalsIgnoreCase(name);
								}).findFirst().orElse(null);
								if (recipe != null) {
									p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
									previous.put(p, active.get(p));
									active.put(p, "recipeMenu:" + name);
									p.openInventory(recipeMenu(recipe));
								}
								else p.playSound(p.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
							}
						}
					}
				}
				else if (active.containsKey(p) && active.get(p).startsWith("recipeMenu")) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (active.containsKey(p)) {
			if (active.get(p).startsWith("recipeMenu") && e.getView().getTitle().equals("Press \"E\" to go back.")) {
				if (previous.get(p).startsWith("recipeGroup")) {
					Bukkit.getScheduler().runTaskLater(mc, () -> {
						p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
						String prev = previous.get(p);
						previous.put(p, active.get(p));
						active.put(p, prev);
						p.openInventory(recipeGroup(p, prev.replace("recipeGroup:",""), 1));
					}, 1);
				}
			}
			else if (active.get(p).startsWith("recipeGroup") && (previous.get(p).startsWith("recipeMenu") || ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase(active.get(p).replace("recipeGroup:","")))) {
				Bukkit.getScheduler().runTaskLater(mc, () -> {
					previous.put(p, active.get(p));
					active.put(p, "recipeBookMenu");
					selectedRecipes.remove(p);
					p.openInventory(recipeBookMenu());
				}, 1);
			}
			else if (active.get(p).equalsIgnoreCase("recipeBookMenu")) {//Full close
				p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
				active.remove(p);
				previous.remove(p);
				selectedRecipes.remove(p);
			}
		}
	}

	private Inventory recipeBookMenu() {
		Inventory inv = Bukkit.createInventory(null, 9*3, ChatColor.DARK_AQUA + "Gamemode 4 " + ChatColor.GRAY +  "Crafting Recipes");

		ChatColor color = ChatColor.DARK_AQUA;
		inv.addItem(
				ic.createGuiItem(Material.CRAFTING_TABLE, color, "Standard Crafting", false, "Custom Crafter"),
				ic.createGuiItem(Material.MUSIC_DISC_13, color, "Record Crafting", false, "Custom Crafter"),
				ic.createGuiItem(CustomItems.HEART_CANISTER_TIER_1(1), color, "Heart Canisters", false, "Custom Crafter"),
				ic.createGuiItem(CustomItems.OAK_TRAPPED_SIGN(1), color, "Trapped Signs", false, "Custom Crafter"),
				ic.createGuiItem(CustomItems.LIGHTNING_ROD(1), color, "Lightning Rods", false, "Custom Crafter"),
				ic.createGuiItem(CustomItems.EMPTY_SPAWN_EGG(1), color, "Soul Probes", false, "Custom Crafter"),
				ic.createGuiItem(Material.PISTON, color, "Master Crafting", false, "Master Crafter"),
				ic.createGuiItem(Material.BLAST_FURNACE, color, "Blast Furnace", false, "Blast Furnace"),
				ic.createGuiItem(Material.TNT, color, "Disassembler", false, "Disassembler"),
				ic.createGuiItem(CustomItems.PHILOSOPHERS_STONE(1), color, "Equivalent Exchange", false, "Alchemical Crafter")
		);

		inv.setItem(22, ic.createGuiItem(Material.BARRIER, ChatColor.RED, "Close the menu", false, "Click to close the recipe menu"));

		return inv;
	}

	private Inventory recipeGroup(Player p, String name, int page) {
		List<ItemStack> items = new ArrayList<>();
		if (name.equalsIgnoreCase("Standard Crafting")) {
			items = getItems(p, CustomRecipes.standardCrafting);
			selectedRecipes.put(p, CustomRecipes.standardCrafting);
		}
		else if (name.equalsIgnoreCase("Record Crafting")) {
			items = getItems(p, CustomRecipes.recordCrafting);
			selectedRecipes.put(p, CustomRecipes.recordCrafting);
		}
		else if (name.equalsIgnoreCase("Heart Canisters")) {
			items = getItems(p, CustomRecipes.heartCanisters);
			selectedRecipes.put(p, CustomRecipes.heartCanisters);
		}
		else if (name.equalsIgnoreCase("Trapped Signs")) {
			items = getItems(p, CustomRecipes.trappedSigns);
			selectedRecipes.put(p, CustomRecipes.trappedSigns);
		}
		else if (name.equalsIgnoreCase("Lightning Rods")) {
			items = getItems(p, CustomRecipes.lightningRods);
			selectedRecipes.put(p, CustomRecipes.lightningRods);
		}
		else if (name.equalsIgnoreCase("Soul Probes")) {
			items = getItems(p, CustomRecipes.soulProbes);
			selectedRecipes.put(p, CustomRecipes.soulProbes);
		}
		else if (name.equalsIgnoreCase("Master Crafting")) {
			items = getItems(p, CustomRecipes.masterCrafting);
			selectedRecipes.put(p, CustomRecipes.masterCrafting);
		}
		else if (name.equalsIgnoreCase("Blast Furnace")) {
			items = getItems(p, CustomRecipes.blastFurnace);
			selectedRecipes.put(p, CustomRecipes.blastFurnace);
		}
		else if (name.equalsIgnoreCase("Disassembler")) {
			items = getItems(p, CustomRecipes.disassembler);
			selectedRecipes.put(p, CustomRecipes.disassembler);
		}
		else if (name.equalsIgnoreCase("Equivalent Exchange")) {
			items = getItems(p, CustomRecipes.equivalentExchange);
			selectedRecipes.put(p, CustomRecipes.equivalentExchange);
		}

		double x = (double) items.size() / 45;
		int pages = (int) Math.ceil(x);

		String pagesStr = "";
		if (items.size() > 45) pagesStr = " (" + page + "/" + pages + ")";

		if (page >= 2) {
			if (45 * (page - 1) > 0) items.subList(0, 45 * (page - 1)).clear();
		}
		int rows = (int) Math.ceil(items.size() / 9.0);
		if (rows > 5) rows = 5;

		Inventory inv = Bukkit.createInventory(null, 9*(rows+1), ChatColor.DARK_AQUA + name + ChatColor.GRAY + pagesStr);

		if (page > 1)
			inv.setItem(9*rows + 2, InventoryCreator.createGuiItem(Material.SUNFLOWER, "Previous page", "Go back to page " + (page - 1)));
		if (page < pages && items.size() > 45)
			inv.setItem(9*rows + 6, InventoryCreator.createGuiItem(Material.SUNFLOWER, "Next page", "Go to page " + (page + 1)));

		inv.setItem(9*rows + 4, ic.createGuiItem(Material.BARRIER, ChatColor.RED, "Go back", false, "Click to go back to the recipe menu"));

		for(int i = 0; i < items.size(); i++) {
			if (i == 45) return inv;
			inv.setItem(i, items.get(i));
		}

		return inv;
	}

	private Inventory recipeMenu(Recipe recipe) {
		Inventory inv = Bukkit.createInventory(null, InventoryType.WORKBENCH, "Press \"E\" to go back.");
		inv.setItem(0, recipe.getResult());
		int i = 1;
		if (recipe instanceof ShapedRecipe) {
			ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
			for (String chars : shapedRecipe.getShape()) {
				for (String character : chars.split("(?!^)")) {
					char c = character.charAt(0);
					if (shapedRecipe.getIngredientMap().get(c) != null) inv.setItem(i, shapedRecipe.getIngredientMap().get(c));
					i++;
				}
			}
		}
		else if (recipe instanceof ShapelessRecipe) {
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
			for (ItemStack item : shapelessRecipe.getIngredientList()) {
				inv.setItem(i, item);
				i++;
			}
		}
		return inv;
	}

	private List<ItemStack> getItems(Player p, @NotNull List<Recipe> shaped) {
		List<ItemStack> items = new ArrayList<>();
		for (Recipe r : shaped) {
			if (r instanceof ShapedRecipe) {
				ShapedRecipe recipe = (ShapedRecipe) r;
				ItemStack item = recipe.getResult().clone();
				item.setAmount(1);
				ItemMeta meta = item.getItemMeta();
				List<String> loreArr = new ArrayList<>();
				if (ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Heart Canister"))
					loreArr = meta.getLore();
				for (Map.Entry<Character, ItemStack> set : recipe.getIngredientMap().entrySet()) {
					if (set.getValue() != null) {
						ItemStack ing = set.getValue();
						if (ing.getItemMeta() != null) {
							String itemName = Helper.getItemName(ing);
							int count = 0;
							for (String chars : recipe.getShape()) {
								for (String character : chars.split("(?!^)")) {
									char c = character.charAt(0);
									if (set.getKey() == c) count++;
								}
							}
							if (p.getInventory().containsAtLeast(ing, count))
								loreArr.add(ChatColor.DARK_GREEN + "" + count + "x " + ChatColor.stripColor(itemName));
							else loreArr.add(ChatColor.GRAY + "" + count + "x " + itemName);
						}
					}
				}
				meta.setLore(loreArr);
				item.setItemMeta(meta);
				items.add(item);
			}
			else if (r instanceof ShapelessRecipe) {
				ShapelessRecipe recipe = (ShapelessRecipe) r;
				ItemStack item = recipe.getResult().clone();
				item.setAmount(1);
				ItemMeta meta = item.getItemMeta();
				List<String> loreArr = new ArrayList<>();
				for(ItemStack ing : recipe.getIngredientList()) {
					if (ing != null) {
						if (ing.getItemMeta() != null) {
							String itemName = Helper.getItemName(ing);
							if (p.getInventory().contains(ing, ing.getAmount())) loreArr.add(ChatColor.GREEN + "" + ing.getAmount() + "x " + ChatColor.stripColor(itemName));
							else loreArr.add(ChatColor.GRAY + "" + ing.getAmount() + "x " + itemName);
						}
					}
				}
				meta.setLore(loreArr);
				item.setItemMeta(meta);
				items.add(item);
			}
		}
		return items;
	}
}
