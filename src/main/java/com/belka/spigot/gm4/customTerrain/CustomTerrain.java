package com.belka.spigot.gm4.customTerrain;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import javafx.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class CustomTerrain implements Listener, Initializable {

	private MainClass mc;

	boolean customTerrain;
	boolean coolerCaves;
	boolean dangerousDungeons;

	List<Pair<Integer, Integer>> loadedChunks = new ArrayList<>();

	int loadRadius = 3;
	List<UpdatableBlock> replacements = new ArrayList<>();
	private boolean isReplacing = false;
	private int replacingSpeed = 24576;

	public CustomTerrain(MainClass mc) {
		this.mc = mc;
	}

	public void init(MainClass mc) {
		customTerrain = mc.getConfig().getBoolean("CustomTerrain.enabled");
		coolerCaves = mc.getConfig().getBoolean("CustomTerrain.CoolerCaves.enabled");
		dangerousDungeons = mc.getConfig().getBoolean("CustomTerrain.DangerousDungeons.enabled");

		loadRadius = mc.storage().config().getInt("CustomTerrain.loadRadius");
		replacingSpeed = mc.storage().config().getInt("CustomTerrain.replacingSpeed");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (!customTerrain) return;

		if (coolerCaves || dangerousDungeons)
			if (e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0)))
				loadChunks(e.getPlayer().getLocation());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (!customTerrain) return;

		if (coolerCaves || dangerousDungeons)
			if (e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0)))
				if (!e.getFrom().getChunk().equals(e.getTo().getChunk()))
					loadChunks(e.getTo());
	}
	
	private void loadChunks(Location loc) {
		Chunk c = loc.getChunk();

		for (int x = c.getX() - loadRadius; x <= c.getX() + loadRadius; x++) {
			for (int z = c.getZ() - loadRadius; z <= c.getZ() + loadRadius; z++) {
				Chunk chunk = c.getWorld().getChunkAt(x, z);
				if (!loadedChunks.contains(new Pair<>(chunk.getX(), chunk.getZ()))) {
					Location corner = new Location(c.getWorld(), c.getX() * 16, 0, c.getZ() * 16);
					Block nw = corner.getBlock();
					if (nw.getType() == Material.BARRIER) continue;

					if (coolerCaves) mc.coolerCaves().loadChunk(chunk);
					if (dangerousDungeons) mc.dangerousDungeons();

					loadedChunks.add(new Pair<>(chunk.getX(), chunk.getZ()));
				}
			}
		}
	}

	void updateBlocks() {
		if (isReplacing) return;
		isReplacing = true;
		final int[] task = new int[]{-1};
		task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
			List<UpdatableBlock> tmp = new ArrayList<>();
			for (int i = 0; i < replacingSpeed; i++) {// Create temp block cache for replacing
				if (replacements.size() == 0) break;
				tmp.add(replacements.get(0));
				replacements.remove(0);
			}
			for (int i = 0; i < tmp.size(); i++) {// Update blocks in cache
				UpdatableBlock pair = tmp.get(i);
				if (pair.getMaterial() != null) {
					Block b = pair.getLocation().getBlock();
					b.setType(pair.getMaterial(), pair.isUpdate());
					if (pair.getConsumer() != null) pair.getConsumer().accept(b);
				}

				if (tmp.size() < replacingSpeed && i == tmp.size() - 1) {// Done
					System.out.println("Updated queued CustomTerrain chunks");
					Bukkit.getScheduler().cancelTask(task[0]);
					isReplacing = false;
				}
			}
		}, 0L, 20L);
	}
}
