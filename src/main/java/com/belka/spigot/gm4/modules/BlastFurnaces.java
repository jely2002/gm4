package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.ConsoleColor;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BlastFurnaces implements Listener, Initializable {

	private static MainClass mc;
	private boolean enabled = true;
	private HashMap<Furnace, ArmorStand> activeBlastFurnaces = new HashMap<>();

	public BlastFurnaces(MainClass mc) {
		BlastFurnaces.mc = mc;
	}

	public void init(MainClass mc) {
		if(!mc.getConfig().getBoolean("BlastFurnaces.enabled")) enabled = false;
		if(!mc.getConfig().getBoolean("CustomCrafter.enabled")) {
			System.out.println(ConsoleColor.RED + "Enable CustomCrafter in order for BlastFurnaces to work!");
			mc.getConfig().set("BlastFurnaces.enabled", false);
			mc.saveConfig();
			enabled = false;
			return;
		}
		if (mc.storage().data().getConfigurationSection("BlastFurnaces") != null) {
			ArrayList<String> blastFurnaces = new ArrayList<>(mc.storage().data().getConfigurationSection("BlastFurnaces").getKeys(false));
			for (String uuid : blastFurnaces) {
				ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(uuid));
				int x = mc.storage().data().getInt("BlastFurnaces." + uuid + ".x");
				int y = mc.storage().data().getInt("BlastFurnaces." + uuid + ".y");
				int z = mc.storage().data().getInt("BlastFurnaces." + uuid + ".z");
				World world = Bukkit.getWorld(mc.storage().data().getString("BlastFurnaces." + uuid + ".world"));
				Block b = new Location(world, x, y, z).getBlock();
				if (b.getState() instanceof Furnace) {
					activeBlastFurnaces.put((Furnace) b.getState(), as);
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) { // Create Blast Furnace
		if (!enabled) return;
		if (!mc.storage().data().contains("BlastFurnaces")) return;
		Block b = event.getBlock();
		Material mat = b.getType();
		if (mat != Material.IRON_BLOCK && mat != Material.FURNACE && mat != Material.GLASS) return;
		for (Entity e : Helper.getNearbyEntities(b.getLocation(), 3)) {
			if (e instanceof ArmorStand) {
				ArmorStand as = (ArmorStand) e;
				if (!as.getCustomName().equalsIgnoreCase("BlastFurnace")) return;
				Block loc = as.getLocation().getBlock();
				Block north = loc.getRelative(BlockFace.NORTH);
				Block northEast = loc.getRelative(BlockFace.NORTH_EAST);
				Block northWest = loc.getRelative(BlockFace.NORTH_WEST);
				Block east = loc.getRelative(BlockFace.EAST);
				Block south = loc.getRelative(BlockFace.SOUTH);
				Block southEast = loc.getRelative(BlockFace.SOUTH_EAST);
				Block southWest = loc.getRelative(BlockFace.SOUTH_WEST);
				Block west = loc.getRelative(BlockFace.WEST);

				Block northUp = north.getRelative(BlockFace.UP);
				Block northEastUp = northEast.getRelative(BlockFace.UP);
				Block northWestUp = northWest.getRelative(BlockFace.UP);
				Block eastUp = east.getRelative(BlockFace.UP);
				Block southUp = south.getRelative(BlockFace.UP);
				Block southEastUp = southEast.getRelative(BlockFace.UP);
				Block southWestUp = southWest.getRelative(BlockFace.UP);
				Block westUp = west.getRelative(BlockFace.UP);

				if (northEast.getType() == Material.IRON_BLOCK && northWest.getType() == Material.IRON_BLOCK && southEast.getType() == Material.IRON_BLOCK && southWest.getType() == Material.IRON_BLOCK &&
				northEastUp.getType() == Material.IRON_BLOCK && northWestUp.getType() == Material.IRON_BLOCK && southEastUp.getType() == Material.IRON_BLOCK && southWestUp.getType() == Material.IRON_BLOCK) {
					if (north.getType() == Material.FURNACE && northUp.getType() == Material.GLASS &&
							east.getType() == Material.IRON_BLOCK && eastUp.getType() == Material.IRON_BLOCK &&
							south.getType() == Material.IRON_BLOCK && southUp.getType() == Material.IRON_BLOCK &&
							west.getType() == Material.IRON_BLOCK && westUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) north.getState();
						activeBlastFurnaces.put(furnace, as);
						checkFurnace(furnace);
						enableBF(furnace, as);
					}
					else if (east.getType() == Material.FURNACE && eastUp.getType() == Material.GLASS &&
							south.getType() == Material.IRON_BLOCK && southUp.getType() == Material.IRON_BLOCK &&
							west.getType() == Material.IRON_BLOCK && westUp.getType() == Material.IRON_BLOCK &&
							north.getType() == Material.IRON_BLOCK && northUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) east.getState();
						activeBlastFurnaces.put(furnace, as);
						checkFurnace(furnace);
						enableBF(furnace, as);
					}
					else if (south.getType() == Material.FURNACE && southUp.getType() == Material.GLASS &&
							west.getType() == Material.IRON_BLOCK && westUp.getType() == Material.IRON_BLOCK &&
							north.getType() == Material.IRON_BLOCK && northUp.getType() == Material.IRON_BLOCK &&
							east.getType() == Material.IRON_BLOCK && eastUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) south.getState();
						activeBlastFurnaces.put(furnace, as);
						checkFurnace(furnace);
						enableBF(furnace, as);
					}
					else if (west.getType() == Material.FURNACE && westUp.getType() == Material.GLASS &&
							north.getType() == Material.IRON_BLOCK && northUp.getType() == Material.IRON_BLOCK &&
							east.getType() == Material.IRON_BLOCK && eastUp.getType() == Material.IRON_BLOCK &&
							south.getType() == Material.IRON_BLOCK && southUp.getType() == Material.IRON_BLOCK) {
						Furnace furnace = (Furnace) west.getState();
						activeBlastFurnaces.put(furnace, as);
						checkFurnace(furnace);
						enableBF(furnace, as);
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { // Disable Blast Furnace
		if (!enabled) return;
		Block b = event.getBlock();
		if (b.getType() != Material.IRON_BLOCK && b.getType() != Material.FURNACE && b.getType() != Material.GLASS) return;
		for (Entity e : Helper.getNearbyEntities(b.getLocation(), 3)) {
			if (e instanceof ArmorStand) {
				ArmorStand as = (ArmorStand) e;
				if (!as.getCustomName().equalsIgnoreCase("BlastFurnace") || !mc.storage().data().contains("BlastFurnaces." + as.getUniqueId())) return;
				disableBF(as);
			}
		}

	}
	private void enableBF(Furnace furnace, ArmorStand as) {
		String uuid = as.getUniqueId().toString();
		mc.storage().data().set("BlastFurnaces." + uuid + ".enabled", true);
		mc.storage().data().set("BlastFurnaces." + uuid + ".x", furnace.getX());
		mc.storage().data().set("BlastFurnaces." + uuid + ".y", furnace.getY());
		mc.storage().data().set("BlastFurnaces." + uuid + ".z", furnace.getZ());
		mc.storage().data().set("BlastFurnaces." + uuid + ".world", furnace.getWorld().getName());
		mc.storage().saveData();
	}
	private void disableBF(ArmorStand as) {
		String uuid = as.getUniqueId().toString();
		mc.storage().data().set("BlastFurnaces." + uuid + ".enabled", false);
		mc.storage().saveData();

		Furnace furnace = (Furnace) new Location(Bukkit.getWorld(mc.storage().data().getString("BlastFurnaces." + uuid + ".world")),
				mc.storage().data().getInt("BlastFurnaces." + uuid + ".x"), mc.storage().data().getInt("BlastFurnaces." + uuid + ".y"), mc.storage().data().getInt("BlastFurnaces." + uuid + ".z")).getBlock().getState();
		activeBlastFurnaces.remove(furnace);
	}

	@EventHandler
	public void onSmelt(FurnaceSmeltEvent e) {
		if (!enabled) return;
		Furnace furnace = (Furnace) e.getBlock().getState();
		if (activeBlastFurnaces.containsKey(furnace)) {
			checkFurnace(furnace);
			ArmorStand as = activeBlastFurnaces.get(furnace);
			Block asBlock = as.getLocation().getBlock();
			if (asBlock.getType() == Material.HOPPER) {
				Hopper hopper = (Hopper) asBlock.getState();
				Material resultType = e.getResult().getType();
				if (resultType == Material.IRON_INGOT || resultType == Material.GOLD_INGOT || resultType == Material.DIAMOND || resultType == Material.EMERALD || resultType == Material.LAPIS_LAZULI || resultType == Material.REDSTONE || resultType == Material.COAL) {
					hopper.getInventory().addItem(e.getResult());
				}
			}
		}
	}
	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent e) {
		if (!enabled) return;
		checkFurnace((Furnace) e.getBlock().getState());
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!enabled) return;
		if (e.getClickedInventory() == null) return;
		if (e.getClickedInventory().getType().equals(InventoryType.FURNACE))
			checkFurnace((Furnace) e.getClickedInventory().getLocation().getBlock().getState());
	}
	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (!enabled) return;
		if (e.getInventory().getType().equals(InventoryType.FURNACE))
			checkFurnace((Furnace) e.getInventory().getLocation().getBlock().getState());
	}
	private void checkFurnace(Furnace furnace) {
		if (activeBlastFurnaces.containsKey(furnace)) {
			ArmorStand as = activeBlastFurnaces.get(furnace);
			Bukkit.getScheduler().scheduleSyncDelayedTask(mc, () -> {
				if (furnace.getInventory().getSmelting() == null) {
					as.setHelmet(new ItemStack(Material.AIR));
				}
				else {
					as.setHelmet(furnace.getInventory().getSmelting());
				}
			}, 1L);
		}

	}
}
