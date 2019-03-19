package com.belka.spigot.gm4.crafting;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomCrafter implements Listener, Initializable {

	private MainClass mc;
	private RecipeHandler rh;
	private ArrayList<String> names = new ArrayList<>();

	public CustomCrafter(MainClass mc, RecipeHandler rh) {
		this.mc = mc;
		this.rh = rh;
	}

	public void init(MainClass mc) {
		names.add("CustomCrafter");
		if (mc.getConfig().getBoolean("CustomCrafter.MasterCrafting")) names.add("MasterCrafter");
		if (mc.getConfig().getBoolean("CustomCrafter.BlastFurnace")) names.add("BlastFurnace");
		if (mc.getConfig().getBoolean("CustomCrafter.Disassembler")) names.add("Disassembler");
		if (mc.getConfig().getBoolean("CustomCrafter.EquivalentExchange")) names.add("AlchemicalCrafter");
	}

	@EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
		if (!mc.getConfig().getBoolean("CustomCrafter.enabled")) return;
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
                        List<String> active = mc.storage().data().getStringList("CustomCrafter.customCrafters");
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

								dr.setCustomName("Custom Crafter");
								dr.update();
								dr.getInventory().clear();

                                BlockData blockData = b.getBlockData();
                                ((Directional) blockData).setFacing(BlockFace.DOWN);
                                b.setBlockData(blockData);

                                e.getItemDrop().remove();
                                active.add("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName());
                                mc.storage().data().set("CustomCrafter.customCrafters", active);
                                mc.storage().saveData();
                            }
                        }
                    }
                }
			}
		}, 0, 1L);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		List<String> active = mc.storage().data().getStringList("CustomCrafter.customCrafters");
		World w = b.getWorld();
		if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName())) {
			Location loc = b.getLocation().add(0.5, 0.5, 0.5);
			w.dropItem(loc, new ItemStack(Material.COBBLESTONE, 7));
			w.dropItem(loc, new ItemStack(Material.REDSTONE, 1));
			w.dropItem(loc, new ItemStack(Material.CRAFTING_TABLE, 1));
			if (b.getType() == Material.DROPPER) {
				Dropper dr = (Dropper) b.getState();
				if (dr.getCustomName().equalsIgnoreCase("Master Crafter")) {
					w.dropItem(loc, new ItemStack(Material.COBBLESTONE, 2));
					w.dropItem(loc, new ItemStack(Material.PISTON, 3));
					w.dropItem(loc, new ItemStack(Material.COMPARATOR, 2));
					w.dropItem(loc, new ItemStack(Material.FURNACE, 1));
				}
				else if (dr.getCustomName().equalsIgnoreCase("Disassembler")) {
					w.dropItem(loc, new ItemStack(Material.COBBLESTONE, 7));
					w.dropItem(loc, new ItemStack(Material.REDSTONE, 1));
					w.dropItem(loc, new ItemStack(Material.TNT, 1));
				}
				else if (dr.getCustomName().equalsIgnoreCase("Alchemical Crafter")) {
					w.dropItem(loc, CustomItems.MINIUM_DUST(8));
					w.dropItem(loc, new ItemStack(Material.CRAFTING_TABLE, 1));
				}
			}
			else if (b.getType() == Material.HOPPER) {
				w.dropItem(loc, new ItemStack(Material.IRON_BARS, 2));
				w.dropItem(loc, new ItemStack(Material.IRON_BLOCK, 1));
				w.dropItem(loc, new ItemStack(Material.PISTON, 1));
				w.dropItem(loc, new ItemStack(Material.COMPARATOR, 2));
				w.dropItem(loc, new ItemStack(Material.REDSTONE_TORCH, 1));
			}
			active.remove("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName());
			mc.storage().data().set("CustomCrafter.customCrafters", active);
			mc.storage().saveData();
			for(Entity e : Helper.getNearbyEntities(b.getLocation(), 1)) {
				if(e instanceof ArmorStand) {
					if(names.contains(e.getCustomName())) {
						e.remove();
					}
				}
			}
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
			List<String> active = mc.storage().data().getStringList("CustomCrafter.customCrafters");
			if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName())) {
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
	public void onInteract(PlayerArmorStandManipulateEvent e) {
		if (names.contains(e.getRightClicked().getCustomName())) {
			e.setCancelled(true);
		}
	}
}
