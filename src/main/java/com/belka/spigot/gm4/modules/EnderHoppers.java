package com.belka.spigot.gm4.modules;

import api.CustomBlock;
import api.CustomBlockType;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EnderHoppers implements Module, Listener {

    private MainClass mc;
	private List<CustomBlock> enderHoppers = new ArrayList<>();

    public EnderHoppers(MainClass mc) {
        this.mc = mc;
    }

    @Override
    public void init(MainClass mc) {
		if(!mc.getStorage().config().getBoolean("EnderHoppers.enabled")) return;
		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		assert customBlockList != null;
		enderHoppers = customBlockList.stream().filter(o -> o.getType() == CustomBlockType.ENDER_HOPPER).collect(Collectors.toList());
        mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			for (CustomBlock cb : enderHoppers) {
				Location loc = cb.getLocation();
				Objects.requireNonNull(loc.getWorld()).spawnParticle(Particle.PORTAL, loc.clone().add(0.5, 1, 0.5), 2);
				Location min = loc.clone().subtract(4, 0, 4).add(0, 1, 0);
				Location max = loc.clone().add(5, 4, 5);
				BoundingBox region = new BoundingBox(min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
				for(Entity e : loc.getWorld().getNearbyEntities(region)) {
					if (e instanceof Item) {
						e.teleport(loc.clone().add(0.5, 1, 0.5));
					}
				}
			}
        }, 0, 16L);
    }

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (!mc.getStorage().config().getBoolean("EnderHoppers.enabled")) return;
		Item i = e.getItemDrop();
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			if (i.isOnGround()) {
				Bukkit.getScheduler().cancelTask(task[0]);
				Location loc = i.getLocation();
				if (i.getItemStack().getType() == Material.ENDER_EYE) {
					Block b = loc.getBlock();
					if (b.getBlockData().getMaterial() == Material.HOPPER || b.getRelative(0, -1, 0).getBlockData().getMaterial() == Material.HOPPER) {
						Hopper h;
						if (b.getBlockData().getMaterial() == Material.HOPPER) h = (Hopper) b.getState();
						else h = (Hopper) b.getRelative(0, -1, 0).getState();
						if (CustomBlock.get(h.getLocation()) == null) {
							Inventory inv = h.getInventory();
							if (containsRecipe(inv)) {
								i.remove();
								CustomBlock cb = CustomBlock.create(CustomBlockType.ENDER_HOPPER, h.getLocation());
								enderHoppers.add(cb);
							}
						}
					}
				}
			}
		}, 0, 1L);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {//Remove Ender Hopper
		CustomBlock cb = CustomBlock.get(event.getBlock().getLocation());
		if (cb != null && cb.getType() == CustomBlockType.ENDER_HOPPER) {
			Bukkit.broadcastMessage(enderHoppers.contains(cb) + "");
			enderHoppers.remove(cb);
			cb.destroy(true);
			//TODO particles
		}
	}

	private boolean containsRecipe(Inventory inv) {
    	if (inv.getItem(0) != null && inv.getItem(1) != null && inv.getItem(2) != null && inv.getItem(3) != null && inv.getItem(4) != null)
			return (inv.getItem(0).getAmount() == 2 && inv.getItem(0).getType() == Material.ENDER_PEARL &&
					inv.getItem(1).getAmount() == 2 && inv.getItem(1).getType() == Material.IRON_BLOCK &&
					inv.getItem(2).getAmount() == 1 && inv.getItem(2).getType() == Material.DIAMOND_BLOCK &&
					inv.getItem(3).getAmount() == 2 && inv.getItem(3).getType() == Material.IRON_BLOCK &&
					inv.getItem(4).getAmount() == 2 && inv.getItem(4).getType() == Material.ENDER_PEARL);
    	else return false;
	}
}
