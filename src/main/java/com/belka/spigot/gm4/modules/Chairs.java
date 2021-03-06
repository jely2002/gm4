package com.belka.spigot.gm4.modules;

import api.Helper;
import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.List;

public class Chairs implements Listener, Module {

	private MainClass mc;

	public Chairs(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public Setting getSetting() { return new Setting("Chairs", Material.OAK_STAIRS); }

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (!mc.getStorage().config().getBoolean("Chairs.enabled")) return;
		Item i = e.getItemDrop();
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			if (i.isOnGround()) {
				Bukkit.getScheduler().cancelTask(task[0]);
				Location loc = i.getLocation();
				if (i.getItemStack().getType() == Material.SADDLE) {
					Block block;
					Block b = loc.getBlock();
					Block b2 = loc.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock();

					if (b.getBlockData() instanceof Stairs) block = b;
					else if (b2.getBlockData() instanceof Stairs) block = b2;
					else return;

					List<String> active = mc.getStorage().data().getStringList("Chairs");
					if (!active.contains("x:" + block.getX() + " y:" + block.getY() + " z:" + block.getZ() + " w:" + block.getWorld().getName())) {
						Location pigLoc = block.getLocation();
						Stairs stairs = (Stairs) block.getBlockData();
						if (stairs.getFacing().equals(BlockFace.NORTH)) {
							pigLoc.add(.5, -.39, .55);
							pigLoc.setYaw(0f);
						} else if (stairs.getFacing().equals(BlockFace.EAST)) {
							pigLoc.add(.45, -.39, .5);
							pigLoc.setYaw(90f);
						} else if (stairs.getFacing().equals(BlockFace.SOUTH)) {
							pigLoc.add(.5, -.39, .45);
							pigLoc.setYaw(180f);
						} else if (stairs.getFacing().equals(BlockFace.WEST)) {
							pigLoc.add(.55, -.39, .5);
							pigLoc.setYaw(-90f);
						}
						Pig pig = (Pig) b.getWorld().spawnEntity(pigLoc, EntityType.PIG);
						pig.teleport(pigLoc);
						pig.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, false));
						pig.setAI(false);
						pig.setSaddle(true);
						pig.setGravity(false);
						pig.setCollidable(false);
						pig.setSilent(true);
						pig.setInvulnerable(true);
						pig.setBreed(false);
						pig.setCanPickupItems(false);
						pig.addScoreboardTag("gm4_chairs");
						pig.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1.0);
						pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0);
						pig.setLootTable(null);
						i.remove();
						active.add("x:" + block.getX() + " y:" + block.getY() + " z:" + block.getZ() + " w:" + block.getWorld().getName());
						mc.getStorage().data().set("Chairs", active);
						mc.getStorage().saveData();
					}
				}
			}
		}, 0, 1L);
	}

	@EventHandler
	public void onPigDismount(EntityDismountEvent e) {
		if (!(e.getDismounted() instanceof Pig)) return;
		Pig pig = (Pig) e.getDismounted();
		if (pig.getScoreboardTags().contains("gm4_chairs")) {
			Block block = pig.getLocation().add(0, 1, 0).getBlock();
			Player p = (Player) e.getEntity();
			if (block.getBlockData() instanceof Stairs) {
				Stairs stairs = (Stairs) block.getBlockData();

				Bukkit.getScheduler().scheduleSyncDelayedTask(mc, () -> {
					Location playerLoc = pig.getLocation();
					if (stairs.getFacing().equals(BlockFace.EAST)) {
						playerLoc = pig.getLocation().subtract(1,0,0).add(0,1,0);
					} else if (stairs.getFacing().equals(BlockFace.WEST)) {
						playerLoc = pig.getLocation().add(1,1,0);
					} else if (stairs.getFacing().equals(BlockFace.NORTH)) {
						playerLoc = pig.getLocation().add(0,1,1);
					} else if (stairs.getFacing().equals(BlockFace.SOUTH)) {
						playerLoc = pig.getLocation().subtract(0,0,1).add(0,1,0);
					}
					playerLoc.setPitch(p.getLocation().getPitch());
					playerLoc.setYaw(p.getLocation().getYaw());
					p.teleport(playerLoc);
					Location pigLoc = block.getLocation();
					if (stairs.getFacing().equals(BlockFace.NORTH)) {
						pigLoc.add(.5, -.39, .55);
						pigLoc.setYaw(0f);
					} else if (stairs.getFacing().equals(BlockFace.EAST)) {
						pigLoc.add(.45, -.39, .5);
						pigLoc.setYaw(90f);
					} else if (stairs.getFacing().equals(BlockFace.SOUTH)) {
						pigLoc.add(.5, -.39, .45);
						pigLoc.setYaw(180f);
					} else if (stairs.getFacing().equals(BlockFace.WEST)) {
						pigLoc.add(.55, -.39, .5);
						pigLoc.setYaw(-90f);
					}
					pig.teleport(pigLoc);
				}, 1L);

			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		List<String> active = mc.getStorage().data().getStringList("Chairs");
		World w = b.getWorld();
		if(active.contains("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName())) {
			Location loc = b.getLocation().add(0.5, 0.5, 0.5);
			w.dropItem(loc, new ItemStack(Material.SADDLE, 1));

			active.remove("x:" + b.getX() + " y:" + b.getY() + " z:" + b.getZ() + " w:" + w.getName());
			mc.getStorage().data().set("Chairs", active);
			mc.getStorage().saveData();

			for(Entity e : Helper.getNearbyEntities(b.getLocation().add(.5, -.39, .5), 0.5)) {
				if (e instanceof Pig) {
					if (e.getScoreboardTags().contains("gm4_chairs")) {
						e.remove();
					}
				}
			}
		}
	}
}
