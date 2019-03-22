package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class TrappedSigns implements Listener {

    private MainClass mc;

    public TrappedSigns(MainClass mc) {
        this.mc = mc;
    }

    @EventHandler
    public void signDestroy(BlockBreakEvent e) {
        if(!mc.getConfig().getBoolean("TrappedSigns.enabled")) return;
        if(!(e.getBlock().getType() == Material.SIGN || e.getBlock().getType() == Material.WALL_SIGN)) return;
        for (String id : mc.storage().data().getConfigurationSection("TrappedSigns").getKeys(false)) {
            if (e.getBlock().getLocation().getBlockX() == mc.storage().data().getInt("TrappedSigns." + id + ".x")) {
                if (e.getBlock().getLocation().getBlockY() == mc.storage().data().getInt("TrappedSigns." + id + ".y")) {
                    if (e.getBlock().getLocation().getBlockZ() == mc.storage().data().getInt("TrappedSigns." + id + ".z")) {
                        if (e.getBlock().getWorld().getName().equals(mc.storage().data().getString("TrappedSigns." + id + ".world"))) {
                            mc.storage().data().set("TrappedSigns." + id, null);
                            mc.storage().saveData();
                            if(e.getPlayer().getGameMode() == GameMode.SURVIVAL || e.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                                e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.SIGN, 1));
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
        for (String id : mc.storage().data().getConfigurationSection("TrappedSigns").getKeys(false)) {
            if (e.getBlock().getLocation().getBlockX() == mc.storage().data().getInt("TrappedSigns." + id + ".x")) {
                if (e.getBlock().getLocation().getBlockY() == mc.storage().data().getInt("TrappedSigns." + id + ".y")) {
                    if (e.getBlock().getLocation().getBlockZ() == mc.storage().data().getInt("TrappedSigns." + id + ".z")) {
                        if (e.getBlock().getWorld().getName().equals(mc.storage().data().getString("TrappedSigns." + id + ".world"))) {
                            if (e.getLine(3).isEmpty()) {
                                e.setLine(3, "-x-");
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void signInteract(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!(e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.WALL_SIGN)) return;
        if(!mc.storage().data().contains("TrappedSigns")) return;
        for (String id : mc.storage().data().getConfigurationSection("TrappedSigns").getKeys(false)) {
            if (e.getClickedBlock().getLocation().getBlockX() == mc.storage().data().getInt("TrappedSigns." + id + ".x")) {
                if(e.getClickedBlock().getLocation().getBlockY() == mc.storage().data().getInt("TrappedSigns." + id + ".y")) {
                    if(e.getClickedBlock().getLocation().getBlockZ() == mc.storage().data().getInt("TrappedSigns." + id + ".z")) {
                        if(e.getClickedBlock().getWorld().getName().equals(mc.storage().data().getString("TrappedSigns." + id + ".world"))) {
                            for(Block b : Helper.getNearbyBlocks(e.getClickedBlock().getLocation(), 2)) {
                                if(b.getType() == Material.REDSTONE_WIRE) {
                                    AnaloguePowerable r = (AnaloguePowerable) b.getBlockData();
                                    AtomicInteger count = new AtomicInteger();
                                    final int[] task = new int[]{-1};
                                    task[0] = mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, () -> {
                                        r.setPower(1);
                                        b.setBlockData(r);
                                        if(count.get() >= 15) {
                                            Bukkit.getScheduler().cancelTask(task[0]);
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
    }

    @EventHandler
    public void onSignPlace(BlockPlaceEvent e) {
        if(!mc.getConfig().getBoolean("TrappedSigns.enabled")) return;
        if(!(e.getBlockPlaced().getType() == Material.SIGN || e.getBlockPlaced().getType() == Material.WALL_SIGN)) return;
        if(!e.getItemInHand().hasItemMeta()) return;
        if(!e.getItemInHand().getItemMeta().getLore().get(0).contains("trapped")) return;
        Sign sign = (Sign) e.getBlockPlaced().getState();
        addSign(sign.getLocation());
    }

    private void addSign(Location loc) {
        int id;
        if(!mc.storage().data().contains("TrappedSigns")) {
            id = 0;
        } else {
            id = mc.storage().data().getConfigurationSection("TrappedSigns").getKeys(false).size();
        }
        mc.storage().data().set("TrappedSigns." + id + ".x", loc.getBlockX());
        mc.storage().data().set("TrappedSigns." + id + ".y", loc.getBlockY());
        mc.storage().data().set("TrappedSigns." + id + ".z", loc.getBlockZ());
        mc.storage().data().set("TrappedSigns." + id + ".world", loc.getWorld().getName());
        mc.storage().saveData();
    }

}
