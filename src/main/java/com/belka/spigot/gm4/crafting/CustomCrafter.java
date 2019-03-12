package com.belka.spigot.gm4.crafting;

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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CustomCrafter implements Listener {

	private MainClass mc;

	public CustomCrafter(MainClass mc) {
		this.mc = mc;
	}

//	public void init(MainClass mc) {
//		new BukkitRunnable() {
//			public void run() {
//				for(World world : Bukkit.getServer().getWorlds()) {
//					for(Entity e : world.getEntities()) {
//						if(e instanceof ArmorStand) {
//							ArmorStand ar = (ArmorStand) e;
//							if(ar.getCustomName().equalsIgnoreCase("CustomCrafter")) {
//
//							}
//						}
//					}
//				}
//			}
//		}.runTaskTimer(mc, 0L, (long) 0.5 * 20);
//	}

	@EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
		Item i = e.getItemDrop();
		Location loc = i.getLocation();
		if(i.getItemStack().getType() == Material.CRAFTING_TABLE) {
			Block b = loc.subtract(0.0, 1.0, 0.0).getBlock();
			if(b.getBlockData().getMaterial() == Material.DROPPER && b instanceof Dropper) {
				Dropper dr = (Dropper) b.getState();
				List<String> active = mc.getConfig().getStringList("options.CustomCrafter.active");
				if(!active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName())) {
//					if(crafting.checkRecipe(dr)) {
						Location asLoc = dr.getLocation().add(0.5, 0.075, 0.5);
						ArmorStand as = (ArmorStand) dr.getWorld().spawnEntity(asLoc, EntityType.ARMOR_STAND);
						as.setSmall(true);
						as.setGravity(false);
						as.setVisible(false);
						as.setCanPickupItems(false);
						as.setCustomNameVisible(false);
						as.setRemoveWhenFarAway(false);
						as.setCustomName("CustomCrafter");
						as.setHelmet(new ItemStack(Material.CRAFTING_TABLE, 1));

						BlockData blockData = b.getBlockData();
						((Directional) blockData).setFacing(BlockFace.DOWN);
						b.setBlockData(blockData);

						dr.setCustomName("Custom Crafter");
						dr.getInventory().clear();

						e.getItemDrop().remove();
						active.add("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName());
						mc.getConfig().set("options.CustomCrafter.active", active);
						mc.saveConfig();
//					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		Block b = event.getBlock();
		List<String> active = mc.getConfig().getStringList("options.CustomCrafter.active");
		if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName())) {
			b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.COBBLESTONE, 7));
			b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.REDSTONE, 1));
			b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.CRAFTING_TABLE, 1));
			active.remove("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName());
			mc.getConfig().set("options.CustomCrafter.active", active);
			mc.saveConfig();
			for(Entity e : mc.getNearbyEntities(b.getLocation(), 1)) {
				if(e instanceof ArmorStand) {
					ArmorStand as = (ArmorStand) e;
					if(as.getCustomName().equalsIgnoreCase("CustomCrafter")) {
						as.remove();
					}
				}
			}
		}
	}

//	@EventHandler
//	public void onInventoryOpenEvent(InventoryOpenEvent e) {
//		if(e.getInventory().getHolder() instanceof Dropper) {
//			Inventory i = e.getInventory();
//			Inventory cc = Bukkit.createInventory(null, InventoryType.DROPPER, "Custom Crafter");
//			cc.setContents(i.getContents());
//			cc.setItem(2, new ItemStack(Material.REDSTONE, 1));
//			e.getPlayer().closeInventory();
////            e.getPlayer().openInventory(cc);
//		}
//	}

}
