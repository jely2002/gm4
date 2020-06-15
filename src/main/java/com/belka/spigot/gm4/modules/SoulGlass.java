package com.belka.spigot.gm4.modules;

import api.CustomBlock;
import api.CustomBlockType;
import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SoulGlass implements Module, Listener {

	private MainClass mc;

	private boolean enabled = true;
	private BlastingRecipe blastingRecipe;
	private Map<Player, Beacon> beaconMap = new HashMap<>();
	private List<CustomBlock> soulGlass = new ArrayList<>();
	private final int[] main = new int[]{-1};

	@Override
	public Setting getSetting() { return new Setting("Soul Glass", Material.SOUL_SAND); }

	@Override
	public void init(MainClass mc) {
		this.mc = mc;
		enabled = mc.getStorage().config().getBoolean("SoulGlass.enabled");

		blastingRecipe = new BlastingRecipe(new NamespacedKey(mc, "soul_glass_recipe"), CustomItems.SOUL_GLASS(1), Material.SOUL_SAND,
				mc.getStorage().config().getInt("SoulGlass.smelting.experience", 1), mc.getStorage().config().getInt("SoulGlass.smelting.cookingTime", 800));
		if (enabled) {
			Bukkit.addRecipe(blastingRecipe);
			start();
		}
		else {
			List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
			if (customBlockList != null)
				soulGlass = customBlockList.stream().filter(o -> o.getType() == CustomBlockType.SOUL_GLASS).collect(Collectors.toList());
			for (CustomBlock sg: soulGlass) {
				setSoulGlass((Beacon) sg.getBlock().getRelative(BlockFace.DOWN).getState(), false);
			}
		}
	}

	@Override
	public void reload() {
		if (enabled && !mc.getStorage().config().getBoolean("SoulGlass.enabled")) {//Was enabled before reload
			Bukkit.removeRecipe(new NamespacedKey(mc, "soul_glass_recipe"));
			Bukkit.getScheduler().cancelTask(main[0]);
			for (CustomBlock sg: soulGlass) {
				setSoulGlass((Beacon) sg.getBlock().getRelative(BlockFace.DOWN).getState(), false);
			}
		}
		else if (!enabled && mc.getStorage().config().getBoolean("SoulGlass.enabled")) {//Was disabled before reload
			blastingRecipe = new BlastingRecipe(new NamespacedKey(mc, "soul_glass_recipe"), CustomItems.SOUL_GLASS(1), Material.SOUL_SAND,
					mc.getStorage().config().getInt("SoulGlass.smelting.experience", 1), mc.getStorage().config().getInt("SoulGlass.smelting.cookingTime", 800));
			Bukkit.addRecipe(blastingRecipe);
			start();
		}

		enabled = mc.getStorage().config().getBoolean("SoulGlass.enabled");
	}

	private void start() {
		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		if (customBlockList != null) {
			soulGlass = customBlockList.stream().filter(o -> o.getType() == CustomBlockType.SOUL_GLASS).collect(Collectors.toList());
			main[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
				for (CustomBlock cb : soulGlass) {
					Location loc = cb.getLocation().clone();
					if (loc.getWorld() != null)
						loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc.add(.5, .5, .5), 15, .3, .3, .3, 0);
				}
			}, 0, 20L);
			for (CustomBlock sg: soulGlass) {
				if (sg.getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEACON)
					setSoulGlass((Beacon) sg.getBlock().getRelative(BlockFace.DOWN).getState(), true);
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getItemInHand().isSimilar(CustomItems.SOUL_GLASS(1))) {
			if (e.getBlockPlaced().getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
				CustomBlock cb = CustomBlock.create(CustomBlockType.SOUL_GLASS, e.getBlockPlaced().getLocation());
				if (!enabled) return;
				setSoulGlass((Beacon) e.getBlockPlaced().getRelative(BlockFace.DOWN).getState(), true);
				soulGlass.add(cb);
				Advancements.grantAdvancement("corruption_at_its_finest", e.getPlayer());
			}
			else {
				e.getBlockPlaced().setType(Material.AIR);
				if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) e.getBlockPlaced().getWorld().dropItemNaturally(e.getBlockPlaced().getLocation(), CustomItems.SOUL_GLASS(1));
				e.getPlayer().playSound(e.getBlockPlaced().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1f, .4f);
				e.getBlockPlaced().getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getBlockPlaced().getLocation().clone().add(.5, .5, .5), 15, .3, .3, .3, 0);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!enabled) return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock() == null) return;
		if (event.getClickedBlock().getType() != Material.BEACON) return;
		Player p = event.getPlayer();
		if (p.isSneaking() && p.getInventory().getItemInMainHand().getType() != Material.AIR) return;
		Beacon beacon = (Beacon) event.getClickedBlock().getState();
		beaconMap.put(p, beacon);
//		Bukkit.broadcastMessage("put");
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!enabled) return;
		if (e.getInventory().getType() != InventoryType.BEACON) return;
		Player p = (Player) e.getPlayer();
		if (beaconMap.containsKey(p)) {
			Beacon beacon = beaconMap.get(p);
			CustomBlock cb = CustomBlock.get(beacon.getBlock().getRelative(BlockFace.UP).getLocation());
			if (cb != null && cb.getType() == CustomBlockType.SOUL_GLASS) {
				final int[] task = new int[]{-1};
				task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
					Beacon b = (Beacon) beacon.getBlock().getState();
					if (beaconMap.containsKey(p)) {
						if (b.getPrimaryEffect() != null || b.getSecondaryEffect() != null) {
							Bukkit.getScheduler().cancelTask(task[0]);
//							Bukkit.broadcastMessage("set");
							setSoulGlass((Beacon) beacon.getBlock().getState(), true);
							Advancements.grantAdvancement("corruption_at_its_finest", p);
							beaconMap.remove(p);
						}
					}
					else Bukkit.getScheduler().cancelTask(task[0]);
				}, 0, 20L);
			}
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) { // Remove Custom Blocks
		Block b = event.getBlock();
		CustomBlock cb = CustomBlock.get(b.getLocation());
		if (cb != null && cb.getType() == CustomBlockType.SOUL_GLASS) {
			soulGlass.remove(cb);
			if (event.getPlayer().getGameMode() == GameMode.CREATIVE) cb.destroy(false);
			else cb.destroy(true);
			if (b.getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
				setSoulGlass((Beacon) b.getRelative(BlockFace.DOWN).getState(), false);
			}
		}
		else if (b.getType() == Material.BEACON) {
			Beacon beacon = (Beacon) b.getState();
			if (beaconMap.containsValue(beacon)) {
				beaconMap.entrySet().removeIf(entry -> entry.getValue().equals(beacon));
			}
		}
	}

	@EventHandler
	public void onPush(BlockPistonExtendEvent e) {
		handlePiston(e.getBlocks(), e.getDirection());
	}
	@EventHandler
	public void onPull(BlockPistonRetractEvent e) {
		handlePiston(e.getBlocks(), e.getDirection());
	}
	private void handlePiston(@NotNull List<Block> blocks, @NotNull BlockFace direction) {
		for (Block b: blocks) {
			CustomBlock cb = CustomBlock.get(b.getLocation());
			if (cb != null && cb.getType() == CustomBlockType.SOUL_GLASS) {
				cb.move(b.getRelative(direction).getLocation());

				if (!enabled) return;
				if (b.getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
					if (b.getRelative(direction).getRelative(BlockFace.DOWN).getType() != Material.BEACON) {
						setSoulGlass((Beacon) b.getRelative(BlockFace.DOWN).getState(), false);
					}
					else {
						setSoulGlass((Beacon) b.getRelative(BlockFace.DOWN).getState(), false);
						setSoulGlass((Beacon) b.getRelative(direction).getRelative(BlockFace.DOWN).getState(), true);
					}
				}
				if (b.getRelative(BlockFace.DOWN).getType() != Material.BEACON)
					if (b.getRelative(direction).getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
						setSoulGlass((Beacon) b.getRelative(direction).getRelative(BlockFace.DOWN).getState() ,true);
					}
			}
		}
	}

	private void setSoulGlass(@NotNull Beacon beacon, boolean enabled) {
		PotionEffect primaryEffect = beacon.getPrimaryEffect();
		PotionEffect secondaryEffect = beacon.getSecondaryEffect();
		boolean reversed = false;

		if (primaryEffect != null) {
//			Bukkit.broadcastMessage("P " + primaryEffect.getType().getName() + " " + primaryEffect.getAmplifier());
			PotionEffectType reverse;
			if (enabled) reverse = getCorrupted(primaryEffect.getType());
			else reverse = getOriginal(primaryEffect.getType());
			if (reverse != null && !primaryEffect.getType().getName().equalsIgnoreCase(reverse.getName())) {
				if (primaryEffect.getAmplifier() > 0)
					beacon.setSecondaryEffect(reverse);
				beacon.setPrimaryEffect(reverse);
				beacon.update();
				reversed = true;
			}
		}
		if (secondaryEffect != null) {
//			Bukkit.broadcastMessage("S " + secondaryEffect.getType().getName() + " " + secondaryEffect.getAmplifier());
			PotionEffectType reverse;
			if (enabled) reverse = getCorrupted(secondaryEffect.getType());
			else reverse = getOriginal(secondaryEffect.getType());
			if (reverse != null && !secondaryEffect.getType().getName().equalsIgnoreCase(reverse.getName())) {
				beacon.setSecondaryEffect(reverse);
				beacon.update();
				reversed = true;
			}
		}
		if (enabled && reversed) {
			beacon.getWorld().spawnParticle(Particle.SPELL_WITCH, beacon.getLocation().clone().add(.5, .5, .5), 40, .5, .5, .5, 0, null, true);
			beacon.getWorld().playSound(beacon.getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.BLOCKS, 1.3f, .5f);
		}
		else if (!enabled && reversed) {
			beacon.getWorld().spawnParticle(Particle.TOTEM, beacon.getLocation().clone().add(.5, .5, .5), 40, .5, .5, .5, 0, null, true);
			beacon.getWorld().playSound(beacon.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1f, 1f);
		}
	}

	@Nullable
	private PotionEffectType getCorrupted(PotionEffectType pet) {
		switch (pet.getName()) {
			case "SPEED": return PotionEffectType.SLOW;
			case "FAST_DIGGING": return PotionEffectType.SLOW_DIGGING;
			case "DAMAGE_RESISTANCE": return PotionEffectType.GLOWING;
			case "JUMP": return PotionEffectType.SLOW_FALLING;
			case "INCREASE_DAMAGE": return PotionEffectType.WEAKNESS;
			case "REGENERATION": return PotionEffectType.POISON;
			default: return null;
		}
	}

	@Nullable
	private PotionEffectType getOriginal(PotionEffectType pet) {
		switch (pet.getName()) {
			case "SLOW": return PotionEffectType.SPEED;
			case "SLOW_DIGGING": return PotionEffectType.FAST_DIGGING;
			case "GLOWING": return PotionEffectType.DAMAGE_RESISTANCE;
			case "SLOW_FALLING": return PotionEffectType.JUMP;
			case "WEAKNESS": return PotionEffectType.INCREASE_DAMAGE;
			case "POISON": return PotionEffectType.REGENERATION;
			case "REGENERATION": return PotionEffectType.POISON;
			default: return null;
		}
	}
}
