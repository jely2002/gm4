package com.belka.spigot.gm4.crafting;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class CustomCrafter implements Listener, Initializable {

	private MainClass mc;
	private RecipeHandler rh;
	private ArrayList<String> asNames = new ArrayList<>();

	public CustomCrafter(MainClass mc, RecipeHandler rh) {
		this.mc = mc;
		this.rh = rh;
	}

	public void init(MainClass mc) {
		asNames.add("CustomCrafter");
		if (mc.getStorage().config().getBoolean("CustomCrafter.MasterCrafting")) asNames.add("MasterCrafter");
		if (mc.getStorage().config().getBoolean("CustomCrafter.BlastFurnace")) asNames.add("BlastFurnace");
		if (mc.getStorage().config().getBoolean("CustomCrafter.Disassembler")) asNames.add("Disassembler");
		if (mc.getStorage().config().getBoolean("CustomCrafter.EquivalentExchange")) asNames.add("AlchemicalCrafter");
	}

	@EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
		if (!mc.getStorage().config().getBoolean("CustomCrafter.enabled")) return;
		Item i = e.getItemDrop();
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
            if (i.isOnGround()) {
                Bukkit.getScheduler().cancelTask(task[0]);
                Location loc = i.getLocation();
                if (i.getItemStack().getType() == Material.CRAFTING_TABLE) {
                    Block b = loc.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                    if (b.getBlockData().getMaterial() == Material.DROPPER) {
                        Dropper dr = (Dropper) b.getState();
                        List<String> active = mc.getStorage().data().getStringList("CustomCrafter.customCrafters");
                        if (!active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName())) {
                            if (rh.equalsRecipe(dr, CustomRecipes.create()) && dr.getInventory().getItem(0).getAmount() == 1) {
                                Location asLoc = dr.getLocation().add(0.5, 0.075, 0.5);
                                ArmorStand as = (ArmorStand) dr.getWorld().spawnEntity(asLoc, EntityType.ARMOR_STAND);
                                as.setSmall(true);
                                as.setGravity(false);
                                as.setVisible(false);
                                as.setCanPickupItems(false);
                                as.setCustomNameVisible(false);
                                as.setRemoveWhenFarAway(false);
                                as.setFireTicks(Integer.MAX_VALUE);
                                as.setMarker(true);
                                as.setCustomName("CustomCrafter");
                                as.setHelmet(new ItemStack(Material.CRAFTING_TABLE));
                                as.addScoreboardTag("gm4");

								dr.setCustomName("Custom Crafter");
								dr.update();
								dr.getInventory().clear();

                                BlockData blockData = b.getBlockData();
                                ((Directional) blockData).setFacing(BlockFace.DOWN);
                                b.setBlockData(blockData);

                                e.getItemDrop().remove();
                                active.add("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName());
                                mc.getStorage().data().set("CustomCrafter.customCrafters", active);
                                mc.getStorage().saveData();
								Advancements.grantAdvancement("clever_crafting", e.getPlayer());
                            }
                        }
                    }
                }
			}
		}, 0, 1L);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { // Remove Custom Crafter
		Block b = event.getBlock();
		List<String> active = mc.getStorage().data().getStringList("CustomCrafter.customCrafters");
		World w = b.getWorld();
		if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName())) {
			Location loc = b.getLocation().add(0.5, 0.5, 0.5);
			w.dropItem(loc, new ItemStack(Material.CRAFTING_TABLE, 1));
			dropRecipe(loc, "");
			if (b.getType() == Material.DROPPER) {
				Dropper dr = (Dropper) b.getState();
				dropRecipe(loc, dr.getCustomName());
			}
			else if (b.getType() == Material.HOPPER) {
				dropRecipe(loc, "Blast Furnace Output");
			}
			active.remove("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName());
			mc.getStorage().data().set("CustomCrafter.customCrafters", active);
			for(Entity e : Helper.getNearbyEntities(b.getLocation(), 1)) {
				if(e instanceof ArmorStand) {
					if(asNames.contains(e.getCustomName())) {
						if (b.getType() == Material.HOPPER)
							mc.getStorage().data().set("BlastFurnaces." + e.getUniqueId().toString(), null);
						e.remove();
					}
				}
			}
			mc.getStorage().saveData();
		}
	}

	private void dropRecipe(Location loc, String name) { // Drop creation items
		if (name.equalsIgnoreCase("Custom Crafter")) return;
		ShapedRecipe recipe = CustomRecipes.create();
		switch (name) {
			case "Master Crafter":
				recipe = CustomRecipes.master_crafter();
				break;
			case "Disassembler":
				recipe = CustomRecipes.disassembler();
				break;
			case "Blast Furnace Output":
				recipe = CustomRecipes.blast_furnace();
				break;
			case "Alchemical Crafter":
				recipe = null;
				loc.getWorld().dropItem(loc, new ItemStack(Material.CRAFTING_TABLE));
				loc.getWorld().dropItem(loc, CustomItems.MINIUM_DUST(8));
				break;
		}
		if (recipe != null)
			for (String chars : recipe.getShape())
				for (String character : chars.split("(?!^)")) {
					char c = character.charAt(0);
					ItemStack item = recipe.getIngredientMap().get(c);
					if (item != null) loc.getWorld().dropItem(loc, item);
				}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		updateInv(e.getInventory());
	}
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		updateInv(e.getInventory());
	}
	private void updateInv(Inventory inv) {
		if (inv.getType().equals(InventoryType.DROPPER)) {
			Block b = inv.getLocation().getBlock();
			List<String> active = mc.getStorage().data().getStringList("CustomCrafter.customCrafters");
			if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName())) {
				for (Entity e : Helper.getNearbyEntities(b.getLocation(), 1)) {
					if (e instanceof ArmorStand && e.getScoreboardTags().contains("gm4")) {
						Bukkit.broadcastMessage("Lit");
						e.setFireTicks(Integer.MAX_VALUE);
					}
				}
				mc.getServer().getScheduler().runTaskLater(mc, () -> {
					Dropper dropper = (Dropper) b.getState();
					rh.craft(dropper, (Player) inv.getViewers().get(0));
				}, 1L);
			}
		}
		else if (inv.getType().equals(InventoryType.HOPPER)) {

		}
	}

	@EventHandler
	public void onInteract(PlayerArmorStandManipulateEvent e) { // Disable item removal armor stand
		if (e.getRightClicked().getCustomName() != null && asNames.contains(e.getRightClicked().getCustomName())) {
			e.setCancelled(true);
		}
	}
}
