package com.belka.spigot.gm4.modules;

import api.ConsoleColor;
import api.CustomBlock;
import api.CustomBlockType;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlastFurnaces implements Module, Listener {

	private static MainClass mc;
	private boolean enabled = true;
	private List<CustomBlock> active = new ArrayList<>();

	public BlastFurnaces(MainClass mc) {
		BlastFurnaces.mc = mc;
	}

	@Override
	public void init(MainClass mc) {
		reload();
	}

	@Override
	public void reload() {
		//TODO check if furnace was deactivated, disable
		if(!mc.getStorage().config().getBoolean("BlastFurnaces.enabled")) enabled = false;
		if(!mc.getStorage().config().getBoolean("CustomCrafter.enabled")) {
			System.out.println(ConsoleColor.RED + "Enable CustomCrafter in order for BlastFurnaces to work!" + ConsoleColor.RESET);
			mc.getStorage().config().set("BlastFurnaces.enabled", false);
			mc.saveConfig();
			enabled = false;
			return;
		}
		if (!enabled) return;
		if (mc.getStorage().data().contains("CustomBlocks")) {
			List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
			assert customBlockList != null;
			active = customBlockList.stream().filter(o -> o.getType() == CustomBlockType.BLAST_FURNACE && o.hasActive() && o.isActive()).collect(Collectors.toList());
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) { // Create Blast Furnace
		if (!enabled) return;
		Block b = event.getBlock();
		Material mat = b.getType();
		if (mat != Material.IRON_BLOCK && mat != Material.FURNACE && mat != Material.GLASS) return;

		for (CustomBlock cb: findFurnaces(b)) {
			if (cb != null) {
				if (checkStructure(cb)) {
					active.add(cb);
					cb.setActive(true);
					mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> updateFurnace((Furnace) cb.getBlock().getRelative(cb.getDirection()).getState()), 5L);
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {//Disable Blast Furnace
		if (!enabled) return;
		Block b = event.getBlock();
		if (b.getType() != Material.IRON_BLOCK && b.getType() != Material.FURNACE && b.getType() != Material.GLASS) return;
		for (CustomBlock cb: findFurnaces(b)) {
			if (cb != null && active.contains(cb) && cb.isActive()) {
				mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> {
					if (!checkStructure(cb)) {
						Entity entity = Bukkit.getEntity(cb.getUuid());
						if (entity instanceof ArmorStand) {
							ArmorStand as = (ArmorStand) entity;
							Objects.requireNonNull(as.getEquipment()).setHelmet(new ItemStack(Material.AIR));
						}
						active.remove(cb);
						cb.setActive(false);
						mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> updateFurnace((Furnace) cb.getBlock().getRelative(cb.getDirection()).getState()), 5L);
					}
				}, 1L);
			}
		}
	}

	@EventHandler
	public void onSmelt(FurnaceSmeltEvent e) {
		if (!enabled) return;
		Furnace furnace = (Furnace) e.getBlock().getState();
		CustomBlock cb = active.stream().filter(o -> o.hasDirection() && furnace.getBlock().equals(o.getBlock().getRelative(o.getDirection()))).findFirst().orElse(null);
		if (cb != null && cb.hasActive() && cb.isActive()) {
			mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> updateFurnace(furnace), 5L);
			ArmorStand as = (ArmorStand) Bukkit.getEntity(cb.getUuid());
			if (as != null && cb.hasActive() && cb.isActive()) {
				Material resultType = e.getResult().getType();
				if (resultType == Material.IRON_INGOT || resultType == Material.GOLD_INGOT || resultType == Material.DIAMOND || resultType == Material.EMERALD || resultType == Material.LAPIS_LAZULI || resultType == Material.REDSTONE || resultType == Material.COAL || resultType == Material.QUARTZ) {
					Objects.requireNonNull(cb.getLocation().getWorld()).dropItem(cb.getLocation().clone().add(0.5, .5, 0.5), e.getResult());
				}
			}
		}
	}
	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent e) {
		if (!enabled) return;
		mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> updateFurnace((Furnace) e.getBlock().getState()), 5L);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!enabled) return;
		if (e.getClickedInventory() == null) return;
		if (e.getClickedInventory().getType().equals(InventoryType.FURNACE))
			mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> updateFurnace((Furnace) Objects.requireNonNull(e.getClickedInventory().getLocation()).getBlock().getState()), 5L);
	}
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (!enabled) return;
		if (e.getInventory().getType().equals(InventoryType.FURNACE))
			mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> updateFurnace((Furnace) Objects.requireNonNull(e.getInventory().getLocation()).getBlock().getState()), 5L);
	}

	private void updateFurnace(Furnace furnace) {
		ItemStack smelting = furnace.getInventory().getSmelting();
		if (smelting == null || smelting.getType() == Material.AIR || smelting.getType() == Material.COAL_ORE || smelting.getType() == Material.REDSTONE_ORE || smelting.getType() == Material.IRON_ORE || smelting.getType() == Material.GOLD_ORE ||
				smelting.getType() == Material.DIAMOND_ORE || smelting.getType() == Material.EMERALD_ORE || smelting.getType() == Material.LAPIS_ORE || smelting.getType() == Material.NETHER_QUARTZ_ORE) {
			CustomBlock cb = active.stream().filter(o -> o.hasDirection() && furnace.getBlock().equals(o.getBlock().getRelative(o.getDirection()))).findFirst().orElse(null);
			if (cb != null) {
				ArmorStand as = (ArmorStand) Bukkit.getEntity(cb.getUuid());
				if (as != null) {
					EntityEquipment ee = as.getEquipment();
					if (ee != null) {
						if (smelting == null || smelting.getType() == Material.AIR) {
							ee.setHelmet(new ItemStack(Material.AIR));
						}
						else if (ee.getHelmet() != null && ee.getHelmet().getType() != smelting.getType()) {
							ItemStack head = smelting.clone();
							head.setAmount(1);
							ee.setHelmet(head);
						}
					}
				}
			}
		}
	}

	private boolean checkStructure(CustomBlock cb) {
		Block b = cb.getBlock();
		Block north = b.getRelative(BlockFace.NORTH);
		Block northEast = b.getRelative(BlockFace.NORTH_EAST);
		Block northWest = b.getRelative(BlockFace.NORTH_WEST);
		Block east = b.getRelative(BlockFace.EAST);
		Block south = b.getRelative(BlockFace.SOUTH);
		Block southEast = b.getRelative(BlockFace.SOUTH_EAST);
		Block southWest = b.getRelative(BlockFace.SOUTH_WEST);
		Block west = b.getRelative(BlockFace.WEST);

		Block northUp = north.getRelative(BlockFace.UP);
		Block northEastUp = northEast.getRelative(BlockFace.UP);
		Block northWestUp = northWest.getRelative(BlockFace.UP);
		Block eastUp = east.getRelative(BlockFace.UP);
		Block southUp = south.getRelative(BlockFace.UP);
		Block southEastUp = southEast.getRelative(BlockFace.UP);
		Block southWestUp = southWest.getRelative(BlockFace.UP);
		Block westUp = west.getRelative(BlockFace.UP);

		if (areType(Material.IRON_BLOCK, northEast, northEastUp, northWest, northWestUp, southEast, southEastUp, southWest, southWestUp)) {
			if (north.getType() == Material.FURNACE && northUp.getType() == Material.GLASS && areType(Material.IRON_BLOCK, east, eastUp, south, southUp, west, westUp)) {
				if (!cb.hasDirection()) cb.setDirection(BlockFace.NORTH);
				return true;
			}
			else if (east.getType() == Material.FURNACE && eastUp.getType() == Material.GLASS && areType(Material.IRON_BLOCK, south, southUp, west, westUp, north, northUp)) {
				if (!cb.hasDirection()) cb.setDirection(BlockFace.EAST);
				return true;
			}
			else if (south.getType() == Material.FURNACE && southUp.getType() == Material.GLASS && areType(Material.IRON_BLOCK, west, westUp, north, northUp, east, eastUp)) {
				if (!cb.hasDirection()) cb.setDirection(BlockFace.SOUTH);
				return true;
			}
			else if (west.getType() == Material.FURNACE && westUp.getType() == Material.GLASS && areType(Material.IRON_BLOCK, north, northUp, east, eastUp, south, southUp)) {
				if (!cb.hasDirection()) cb.setDirection(BlockFace.WEST);
				return true;
			}
		}
		return false;
	}

	private List<CustomBlock> findFurnaces(Block block) {
		List<CustomBlock> furnaces = new ArrayList<>();
		for (int x = 1; x >= -1; x--) {
			for (int y = 0; y >= -1; y--) {
				for (int z = 1; z >= -1; z--) {
					CustomBlock cb = CustomBlock.get(block.getRelative(x, y, z).getLocation());
					if (cb != null && cb.getType() == CustomBlockType.BLAST_FURNACE) {
						furnaces.add(cb);
					}
				}
			}
		}
		return furnaces;
	}

	private boolean areType(Material mat, Block... blocks) {
		for (Block b: blocks) {
			if (b.getType() != mat) return false;
		}
		return true;
	}
}
