package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.logging.Level;

public class PigTractors implements Listener, Module {

    private MainClass mc;

    public PigTractors(MainClass mc) {
        this.mc = mc;
    }

    HashMap<String, Entity> Tractors = new HashMap<>();

    private boolean isDisabled() {
        return !mc.getStorage().config().getBoolean("PigTractors.enabled");
    }

    @Override
    public void init(MainClass mc) {
        startScheduler();
    }

    private void startScheduler() {
        BukkitScheduler scheduler = mc.getServer().getScheduler();
        scheduler.runTaskTimer(mc, new Runnable() {
            @Override
            public void run() {
                for(Player p : mc.getServer().getOnlinePlayers()) {
                    if(p.isInsideVehicle() && p.getVehicle() instanceof Pig) {
                        if(hasHoe(p)) {
                            Tractors.putIfAbsent(p.getName(), p.getVehicle());
                            Advancements.grantAdvancement("oink_tractors", p);
                        }
                    }
                    if(Tractors.containsKey(p.getName())) {
                        if(!hasHoe(p) || !(p.isInsideVehicle() && p.getVehicle() instanceof Pig)) {
                            Tractors.remove(p.getName());
                        }
                    }
                }
            }
        }, 0L, 20L);
    }

    private void removeDurability(Inventory inv) {
        for(int i = 0; i < 35; ++i) {
            if(inv.getItem(i) == null) continue;
            if(inv.getItem(i).getType() == Material.DIAMOND_HOE || inv.getItem(i).getType() == Material.GOLDEN_HOE || inv.getItem(i).getType() == Material.IRON_HOE || inv.getItem(i).getType() == Material.STONE_HOE || inv.getItem(i).getType() == Material.WOODEN_HOE) {
                ItemStack item = inv.getItem(i);
                Damageable dmg = (Damageable) item.getItemMeta();
                dmg.setDamage(dmg.getDamage() + 1);
                item.setItemMeta((ItemMeta) dmg);
                inv.setItem(i, item);
                break;
            }

        }
    }

    @EventHandler
    public void PigMoveEvent(PlayerMoveEvent e) {
        if(isDisabled()) return;
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if(e.getFrom().getBlock().getType() != e.getTo().getBlock().getType() && e.getFrom().getY() != e.getTo().getY()) return;
        if(Tractors.containsKey(e.getPlayer().getName())) {
            if(!(Tractors.get(e.getPlayer().getName()) == e.getPlayer().getVehicle())) return;
            Player p = e.getPlayer();
            Entity vehicle = p.getVehicle();
            Block blockUnderneath = new Location(e.getTo().getWorld(), e.getTo().getX(), vehicle.getLocation().getY() - 0.5, e.getTo().getZ()).getBlock();
            if(blockUnderneath.getType() == Material.DIRT) {
                blockUnderneath.setType(Material.FARMLAND);
                removeDurability(p.getInventory());
            } else if(
                    (blockUnderneath.getType() == Material.FARMLAND || blockUnderneath.getType() == Material.SOUL_SAND) &&
                    (blockUnderneath.getRelative(BlockFace.UP).getType() == Material.CARROTS ||
                    blockUnderneath.getRelative(BlockFace.UP).getType() == Material.WHEAT ||
                    blockUnderneath.getRelative(BlockFace.UP).getType() == Material.BEETROOTS ||
                    blockUnderneath.getRelative(BlockFace.UP).getType() == Material.NETHER_WART ||
                    blockUnderneath.getRelative(BlockFace.UP).getType() == Material.POTATOES)
            ) {
                blockUnderneath.getRelative(BlockFace.UP).breakNaturally();
                removeDurability(p.getInventory());
            } else if(blockUnderneath.getType() == Material.FARMLAND && blockUnderneath.getRelative(BlockFace.UP).getType() == Material.AIR) {
                for (int i = 0; i < 35; ++i) {
                    if (p.getInventory().getItem(i) == null) break;
                    ItemStack item = p.getInventory().getItem(i);
                    if (item.getType() == Material.POTATO) {
                        item.setAmount(item.getAmount() - 1);
                        blockUnderneath.getRelative(BlockFace.UP).setType(Material.POTATOES);
                        break;
                    } else if (item.getType() == Material.CARROT) {
                        item.setAmount(item.getAmount() - 1);
                        blockUnderneath.getRelative(BlockFace.UP).setType(Material.CARROTS);
                        break;
                    } else if (item.getType() == Material.BEETROOT_SEEDS) {
                        item.setAmount(item.getAmount() - 1);
                        blockUnderneath.getRelative(BlockFace.UP).setType(Material.BEETROOTS);
                        break;
                    } else if (item.getType() == Material.WHEAT_SEEDS) {
                        item.setAmount(item.getAmount() - 1);
                        blockUnderneath.getRelative(BlockFace.UP).setType(Material.WHEAT);
                        break;
                    }
                }
            } else if (blockUnderneath.getType() == Material.SOUL_SAND && blockUnderneath.getRelative(BlockFace.UP).getType() == Material.AIR) {
                for (int i = 0; i < 35; ++i) {
                    if (p.getInventory().getItem(i) == null) break;
                    ItemStack item = p.getInventory().getItem(i);
                    if (item.getType() == Material.NETHER_WART) {
                        item.setAmount(item.getAmount() - 1);
                        blockUnderneath.getRelative(BlockFace.UP).setType(Material.NETHER_WART);
                        break;
                    }
                }
            }
        }
    }

    private boolean hasHoe(Player p) {
        return p.getInventory().contains(Material.WOODEN_HOE) || p.getInventory().contains(Material.STONE_HOE) || p.getInventory().contains(Material.IRON_HOE) || p.getInventory().contains(Material.GOLDEN_HOE) || p.getInventory().contains(Material.DIAMOND_HOE);
    }
}
