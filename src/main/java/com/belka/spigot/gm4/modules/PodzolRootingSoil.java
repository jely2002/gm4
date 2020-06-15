package com.belka.spigot.gm4.modules;

import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PodzolRootingSoil implements Module, Listener {

	private MainClass mc;
	public PodzolRootingSoil(MainClass mc) {
		this.mc = mc;
	}

	private boolean enabled = true;
	private final int[] task = new int[]{-1};
	private int last_uuid = 11;
	private Map<Item, Integer> saplings = new HashMap<>();
	private List<Material> air = Arrays.asList(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.GRASS,
			Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.OXEYE_DAISY,
			Material.ORANGE_TULIP, Material.PINK_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.WITHER_ROSE,
			Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY,
			Material.FERN, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.DEAD_BUSH, Material.LARGE_FERN, Material.TALL_GRASS, Material.VINE);

	@Override
	public Setting getSetting() { return new Setting("Podzol Rooting Soil", Material.PODZOL); }

	@Override
	public void init(MainClass mc) {
		enabled = mc.getStorage().config().getBoolean("PodzolRootingSoil.enabled");
		if (enabled) start();
	}

	@Override
	public void reload() {
		if (enabled && !mc.getStorage().config().getBoolean("PodzolRootingSoil.enabled"))//Was enabled before reload
			Bukkit.getScheduler().cancelTask(task[0]);
		else if (!enabled && mc.getStorage().config().getBoolean("PodzolRootingSoil.enabled"))//Was disabled before reload
			start();
		enabled = mc.getStorage().config().getBoolean("PodzolRootingSoil.enabled");
	}

	private void start() {
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			last_uuid += 11;
			last_uuid = last_uuid % 200;
//			Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "(" + saplings.size() + ") last_uuid: " + last_uuid);

			if (saplings.size() > 0)
				for (Iterator<Map.Entry<Item, Integer>> iterator = saplings.entrySet().iterator(); iterator.hasNext();) {
					Map.Entry<Item, Integer> entry = iterator.next();
					Item item = entry.getKey();
//					Bukkit.broadcastMessage(item.getUniqueId().toString());
//					Bukkit.broadcastMessage("compared_value: " + entry.getValue());

					int rts = getRandomTickSpeed(item.getWorld());
					if (entry.getValue() * rts >= last_uuid) {
						plantTree(item);
						iterator.remove();
						last_uuid += entry.getValue();
						last_uuid = last_uuid % 200;
					}
//					Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "--------------------------------");
				}
		}, 0, 400L);
	}

	@EventHandler
	public void itemSpawnEvent(ItemSpawnEvent e) {
		if (!enabled) return;
		Item item = e.getEntity();
		if (isSapling(item.getItemStack())) {
//			Bukkit.broadcastMessage("Drop sapling");
//			final int[] task = new int[]{-1};
//			task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
//				if (item.isOnGround() && !isOnLeaves(item)) {
//					Bukkit.getScheduler().cancelTask(task[0]);
			NBTEntity nbtEntity = new NBTEntity(item);
//			Bukkit.broadcastMessage("nbt: " + nbtEntity.getLong("UUIDMost"));
			int current_uuid = (int) Math.round((nbtEntity.getLong("UUIDMost") * 0.00000000023283064365386962890625) % 100);
			saplings.put(item, current_uuid);
//					Bukkit.broadcastMessage("Added sapling " + item.getUniqueId().toString());
//				}
//			}, 0, 20L);
		}
	}

	@EventHandler
	public void itemMerge(ItemMergeEvent e) {
//		Bukkit.broadcastMessage("ItemMergeEvent");
		saplings.remove(e.getEntity());
//		saplings.remove(e.getTarget());
//		if (isSapling(e.getTarget().getItemStack())) {
//			saplings.add(e.getTarget());
//		}
	}
	@EventHandler
	public void itemDespawn(ItemDespawnEvent e) {
		if (enabled)
			plantTree(e.getEntity());
		saplings.remove(e.getEntity());
	}
	@EventHandler
	public void itemPickup(EntityPickupItemEvent e) {
		saplings.remove(e.getItem());
	}

	private void plantTree(Item item) {
		if (saplings.containsKey(item)) {
			if (item.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.PODZOL && air.contains(item.getLocation().getBlock().getType())) {
				item.getLocation().getBlock().breakNaturally();
				item.getLocation().getBlock().setType(item.getItemStack().getType());
				item.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, item.getLocation(), 10, .3, .3, .3);
				item.getWorld().playSound(item.getLocation(), Sound.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.BLOCKS, .8f, .2f);
				if (item.getItemStack().getAmount() == 1) item.remove();
				else item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
//				Bukkit.broadcastMessage(ChatColor.GREEN + "Planted tree");
//				TreeType treeType = null;
//				switch (item.getItemStack().getType()) {
//					case OAK_SAPLING:
//						treeType = TreeType.TREE;
//						break;
//					case SPRUCE_SAPLING:
//						treeType = TreeType.REDWOOD;
//						break;
//					case BIRCH_SAPLING:
//						treeType = TreeType.BIRCH;
//						break;
//					case JUNGLE_SAPLING:
//						treeType = TreeType.SMALL_JUNGLE;
//						break;
//					case ACACIA_SAPLING:
//						treeType = TreeType.ACACIA;
//						break;
//					case DARK_OAK_SAPLING:
//						treeType = TreeType.DARK_OAK;
//						break;
//				}
//				if (treeType != null) {
//					item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
//					item.getLocation().getBlock().breakNaturally();
//					if (item.getWorld().generateTree(item.getLocation(), treeType))
//					Bukkit.broadcastMessage("Planted tree");
//					else Bukkit.broadcastMessage("generateTree == false");
//				}
//				else Bukkit.broadcastMessage("Tree == null " + item.getItemStack().getType().name());
			}
		}
	}

	private int getRandomTickSpeed(World world) {
		Integer randomTickSpeed = null;
		if (world != null && world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED) != null) randomTickSpeed = world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED);
		if (randomTickSpeed == null) randomTickSpeed = 3;
		return randomTickSpeed;
	}

	private boolean isSapling(ItemStack itemStack) {
		return itemStack.getType() == Material.OAK_SAPLING || itemStack.getType() == Material.SPRUCE_SAPLING ||
				itemStack.getType() == Material.BIRCH_SAPLING || itemStack.getType() == Material.JUNGLE_SAPLING ||
				itemStack.getType() == Material.ACACIA_SAPLING || itemStack.getType() == Material.DARK_OAK_SAPLING;
	}

	private boolean isOnLeaves(Item item) {
		Material material = item.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
		return material == Material.OAK_LEAVES || material == Material.SPRUCE_LEAVES ||
				material == Material.BIRCH_LEAVES || material == Material.JUNGLE_LEAVES ||
				material == Material.ACACIA_LEAVES || material == Material.DARK_OAK_LEAVES;
	}
}
