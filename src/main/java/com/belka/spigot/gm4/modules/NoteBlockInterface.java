package com.belka.spigot.gm4.modules;

import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class NoteBlockInterface implements Module, Listener {

	private MainClass mc;

	private boolean enabled = true;
	private Map<Block, ArmorStand> displaying = new HashMap<>();
	private Map<ArmorStand, Integer> timer = new HashMap<>();

	@Override
	public Setting getSetting() { return new Setting("Note Block Interface", Material.NOTE_BLOCK); }

	@Override
	public void init(MainClass mc) {
		this.mc = mc;
		enabled = mc.getStorage().config().getBoolean("NoteBlockInterface.enabled");
	}

	@Override
	public void reload() {
		enabled = mc.getStorage().config().getBoolean("NoteBlockInterface.enabled");
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (!enabled) return;
		if (e.getClickedBlock() == null) return;
		if (e.getClickedBlock().getType() != Material.NOTE_BLOCK) return;
		Block block = e.getClickedBlock();
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (p.isSneaking() && p.getInventory().getItemInMainHand().getType() == Material.AIR) {
				e.setCancelled(true);
				NoteBlock noteBlock = (NoteBlock) block.getBlockData();
				int note = noteBlock.getNote().getId() - 1;
				if (note < 0) note = 24;
				noteBlock.setNote(new Note(note));
				block.setBlockData(noteBlock);
				p.playNote(block.getLocation(), noteBlock.getInstrument(), noteBlock.getNote());
				block.getWorld().spawnParticle(Particle.NOTE, block.getLocation().clone().add(.5, 1.25, .5), 0, note, 0, 0, .1);
				Bukkit.getPluginManager().callEvent(new NotePlayEvent(block, noteBlock.getInstrument(), noteBlock.getNote()));
				displayNote(block);
			}
			else
				mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> displayNote(block), 1L);
		}
		else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			displayNote(block);
		}
	}

	private void displayNote(Block block) {
		ArmorStand as;
		if (displaying.containsKey(block)) {
			as = displaying.get(block);
			as.setFireTicks(Integer.MAX_VALUE);
		}
		else {
			Location spawn = block.getLocation().clone().add(.5, 0, .5);
			spawn.setY(0);
			as = (ArmorStand) block.getWorld().spawnEntity(spawn, EntityType.ARMOR_STAND);
			as.setVisible(false);
			as.setInvulnerable(true);
			as.setMarker(true);
			as.setGravity(false);
			as.setSmall(true);
			as.setCustomNameVisible(true);
			as.setFireTicks(Integer.MAX_VALUE);
			Location teleport = spawn.clone();
			teleport.setY(block.getY() + 1);
			mc.getServer().getScheduler().scheduleSyncDelayedTask(mc, () -> as.teleport(teleport), 2L);

			displaying.put(block, as);
			timer.put(as, 0);

			final int[] task = new int[]{-1};
			task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
				if (timer.containsKey(as)) {
					timer.put(as, timer.get(as) + 5);
					if (timer.get(as) > 20) {
						Bukkit.getScheduler().cancelTask(task[0]);
						displaying.remove(block);
						timer.remove(as);
						as.remove();
					}
				}
				else Bukkit.getScheduler().cancelTask(task[0]);
			}, 0L, 5L);
		}
		timer.put(as, 0);
		NoteBlock noteBlock = (NoteBlock) block.getBlockData();
		as.setCustomName(noteBlock.getNote().getTone().name() + (noteBlock.getNote().isSharped()?"♯":""));
	}
}
