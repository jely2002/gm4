package com.belka.spigot.gm4.modules;

import api.ConsoleColor;
import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TrappedSigns implements Module, Listener {

    private MainClass mc;
    private boolean enabled = true;

    public TrappedSigns(MainClass mc) {
        this.mc = mc;
    }

	@Override
	public Setting getSetting() { return new Setting("Trapped Signs", Material.OAK_SIGN); }

    @Override
    public void init(MainClass mc) {
        if (!mc.getStorage().config().getBoolean("TrappedSigns.enabled")) enabled = false;
        if (!mc.getStorage().config().getBoolean("CustomCrafter.enabled")) {
            System.out.println(ConsoleColor.RED + "Enable CustomCrafter in order for TrappedSigns to work!" + ConsoleColor.RESET);
            mc.getStorage().config().set("TrappedSigns.enabled", false);
            mc.saveConfig();
            enabled = false;
        }
    }

    @EventHandler
    public void signDestroy(BlockBreakEvent e) {
        if (!enabled) return;
		if (!(e.getBlock().getState() instanceof Sign)) return;
		if (!mc.getStorage().data().contains("TrappedSigns.0")) return;
        for (String id : mc.getStorage().data().getConfigurationSection("TrappedSigns").getKeys(false)) {
            if (e.getBlock().getLocation().getBlockX() == mc.getStorage().data().getInt("TrappedSigns." + id + ".x")) {
                if (e.getBlock().getLocation().getBlockY() == mc.getStorage().data().getInt("TrappedSigns." + id + ".y")) {
                    if (e.getBlock().getLocation().getBlockZ() == mc.getStorage().data().getInt("TrappedSigns." + id + ".z")) {
                        if (e.getBlock().getWorld().getName().equals(mc.getStorage().data().getString("TrappedSigns." + id + ".world"))) {
                            mc.getStorage().data().set("TrappedSigns." + id, null);
                            mc.getStorage().saveData();
                            if (e.getPlayer().getGameMode() == GameMode.SURVIVAL || e.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                                e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.REDSTONE, 3));
                                e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.TRIPWIRE_HOOK, 2));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void signTextEdit(SignChangeEvent e) {
        if (!enabled) return;
		if (!mc.getStorage().data().contains("TrappedSigns.0")) return;
        for (String id : mc.getStorage().data().getConfigurationSection("TrappedSigns").getKeys(false)) {
            if (e.getBlock().getLocation().getBlockX() == mc.getStorage().data().getInt("TrappedSigns." + id + ".x")) {
                if (e.getBlock().getLocation().getBlockY() == mc.getStorage().data().getInt("TrappedSigns." + id + ".y")) {
                    if (e.getBlock().getLocation().getBlockZ() == mc.getStorage().data().getInt("TrappedSigns." + id + ".z")) {
                        if (e.getBlock().getWorld().getName().equals(mc.getStorage().data().getString("TrappedSigns." + id + ".world"))) {
//                            if (e.getLine(3).isEmpty()) {//TODO secret signs
                                e.setLine(3, "-x-");
//                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void signInteract(PlayerInteractEvent e) {
        if (!enabled) return;
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!(e.getClickedBlock().getState() instanceof Sign)) return;
        if (!mc.getStorage().data().contains("TrappedSigns.0")) return;
        for (String id : mc.getStorage().data().getConfigurationSection("TrappedSigns").getKeys(false)) {
            if (e.getClickedBlock().getLocation().getBlockX() == mc.getStorage().data().getInt("TrappedSigns." + id + ".x")) {
                if (e.getClickedBlock().getLocation().getBlockY() == mc.getStorage().data().getInt("TrappedSigns." + id + ".y")) {
                    if (e.getClickedBlock().getLocation().getBlockZ() == mc.getStorage().data().getInt("TrappedSigns." + id + ".z")) {
                        if (e.getClickedBlock().getWorld().getName().equals(mc.getStorage().data().getString("TrappedSigns." + id + ".world"))) {
                            for(Block b : getRedstone(e.getClickedBlock())) {
								AnaloguePowerable r = (AnaloguePowerable) b.getBlockData();
								if (r.getPower() != 0) continue;
								AtomicInteger count = new AtomicInteger();
								final int[] task = new int[]{-1};
								task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
									r.setPower(1);
									b.setBlockData(r);
									if (count.get() >= 15) {
										Bukkit.getScheduler().cancelTask(task[0]);
										b.setType(Material.AIR);
										b.setType(Material.REDSTONE_WIRE);
										r.setPower(0);
										b.setBlockData(r);
									}
									count.getAndIncrement();
								}, 0, 1L);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSignPlace(BlockPlaceEvent e) {
        if (!enabled) return;
		if (!(e.getBlockPlaced().getState() instanceof Sign)) return;
        if (!e.getItemInHand().hasItemMeta()) return;
        if (!e.getItemInHand().getItemMeta().getLore().get(0).contains("trapped")) return;
        Sign sign = (Sign) e.getBlockPlaced().getState();
        addSign(sign.getLocation());
    }

    private void addSign(Location loc) {
        int id = 0;
        if (mc.getStorage().data().contains("TrappedSigns.0")) id = mc.getStorage().data().getConfigurationSection("TrappedSigns").getKeys(false).size();

        mc.getStorage().data().set("TrappedSigns." + id + ".x", loc.getBlockX());
        mc.getStorage().data().set("TrappedSigns." + id + ".y", loc.getBlockY());
        mc.getStorage().data().set("TrappedSigns." + id + ".z", loc.getBlockZ());
        mc.getStorage().data().set("TrappedSigns." + id + ".world", loc.getWorld().getName());
        mc.getStorage().saveData();
    }

    private ArrayList<Block> getRedstone(Block b) {
    	ArrayList<Block> blocks = new ArrayList<>();
		ArrayList<Block> redstone = new ArrayList<>();
		if (b.getBlockData() instanceof WallSign) {
			WallSign s = (WallSign) b.getBlockData();
			Block att = b.getRelative(s.getFacing().getOppositeFace());
			blocks.add(b.getRelative(0, -1, 0));
			blocks.add(att.getRelative(0, 1, 0));
			blocks.add(att.getRelative(0, -1, 0));

			blocks.add(b.getRelative(1, 0, 0));
			blocks.add(b.getRelative(-1, 0, 0));
			blocks.add(b.getRelative(0, 0, 1));
			blocks.add(b.getRelative(0, 0, -1));

			blocks.add(att.getRelative(0, 0, 1));
			blocks.add(att.getRelative(0, 0, -1));
			blocks.add(att.getRelative(1, 0, 0));
			blocks.add(att.getRelative(-1, 0, 0));
		}
		else if (b.getState() instanceof Sign) {
			blocks.add(b.getRelative(0, -2, 0));
			blocks.add(b.getRelative(1, 0, 0));
			blocks.add(b.getRelative(1, -1, 0));
			blocks.add(b.getRelative(-1, 0, 0));
			blocks.add(b.getRelative(-1, -1, 0));
			blocks.add(b.getRelative(0, 0, 1));
			blocks.add(b.getRelative(0, -1, 1));
			blocks.add(b.getRelative(0, 0, -1));
			blocks.add(b.getRelative(0, -1, -1));
		}
		for (Block block : blocks) {
			if (block.getType() == Material.REDSTONE_WIRE) redstone.add(block);
		}
		return redstone;
	}
}
