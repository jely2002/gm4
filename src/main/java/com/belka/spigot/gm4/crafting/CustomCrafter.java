package com.belka.spigot.gm4.crafting;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CustomCrafter implements Listener {

	private MainClass mc;
	private RecipeHandler crafting;

	public CustomCrafter(MainClass mc, RecipeHandler crafting) {
		this.mc = mc;
		this.crafting = crafting;
	}

	@EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
		Item i = e.getItemDrop();
		Location loc = i.getLocation();
		if(i.getItemStack().getType() == Material.CRAFTING_TABLE) {
			Block b = loc.subtract(0.0, 1.0, 0.0).getBlock();
			if(b.getBlockData().getMaterial() == Material.DROPPER && b instanceof Dropper) {
				Dropper dr = (Dropper) b.getState();
				List<String> active = mc.storage().data().getStringList("CustomCrafter.customCrafters");
				if(!active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName())) {
					if(crafting.checkRecipe(dr)) {
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
						mc.storage().data().set("CustomCrafter.customCrafters", active);
						mc.storage().saveData();
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		List<String> active = mc.storage().data().getStringList("CustomCrafter.customCrafters");
		if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName())) {
			b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.COBBLESTONE, 7));
			b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.REDSTONE, 1));
			b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.CRAFTING_TABLE, 1));
			active.remove("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + b.getWorld().getName());
			mc.storage().data().set("CustomCrafter.customCrafters", active);
			mc.storage().saveData();
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

}
