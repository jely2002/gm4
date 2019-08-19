package com.belka.spigot.gm4.modules;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;

public class PigTractors {

    ArrayList<String> playersOnTractor = new ArrayList<>();

    @EventHandler
    public void mountPig(EntityMountEvent e) {
        if(e.getMount() instanceof Pig && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(p.getInventory().contains(Material.WOODEN_HOE) || p.getInventory().contains(Material.STONE_HOE) || p.getInventory().contains(Material.IRON_HOE) || p.getInventory().contains(Material.GOLDEN_HOE) || p.getInventory().contains(Material.DIAMOND_HOE)) {
                playersOnTractor.add(p.getName());
            }
        }
    }

    @EventHandler
    public void unmountPig(EntityDismountEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            playersOnTractor.remove(p.getName());
        }
    }

    @EventHandler
    public void hoeRemoved(PlayerDropItemEvent e) {
        if (playersOnTractor.contains(e.getPlayer().getName())) {
            if (!(e.getPlayer().getInventory().contains(Material.WOODEN_HOE) || e.getPlayer().getInventory().contains(Material.STONE_HOE) || e.getPlayer().getInventory().contains(Material.IRON_HOE) || e.getPlayer().getInventory().contains(Material.GOLDEN_HOE) || e.getPlayer().getInventory().contains(Material.DIAMOND_HOE))) {
                playersOnTractor.remove(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void HoePickedUp(EntityPickupItemEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(p.getInventory().contains(Material.WOODEN_HOE) || p.getInventory().contains(Material.STONE_HOE) || p.getInventory().contains(Material.IRON_HOE) || p.getInventory().contains(Material.GOLDEN_HOE) || p.getInventory().contains(Material.DIAMOND_HOE)) {
                if(p.isInsideVehicle() && p.getVehicle() instanceof Pig) {
                    playersOnTractor.add(p.getName());
                }
            }
        }
    }





    @EventHandler
    public void isRiding(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if(playersOnTractor.contains(e.getPlayer().getName())) {
            Location blockBelow = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY() - 1, e.getTo().getBlockZ());
            if(e.getTo().getBlock().getType() == Material.BEETROOTS || e.getTo().getBlock().getType() == Material.CARROTS || e.getTo().getBlock().getType() == Material.POTATOES ||e.getTo().getBlock().getType() == Material.WHEAT || e.getTo().getBlock().getType() == Material.NETHER_WART_BLOCK) {
                e.getTo().getBlock().breakNaturally();
            }
            if(blockBelow.getBlock().getType() == Material.DIRT) {
                blockBelow.getBlock().setType(Material.FARMLAND);
                //TODO Remove durability
            }
            if(blockBelow.getBlock().getType() == Material.FARMLAND) {
                for(int i = 0; i < 35; ++i) {
                    ItemStack item = e.getPlayer().getInventory().getItem(i);
                    if(item.getType() == Material.POTATO) {
                        item.setAmount(item.getAmount() - 1);
                        e.getTo().getBlock().setType(Material.POTATOES);
                        break;
                    } else if(item.getType() == Material.CARROT) {
                        item.setAmount(item.getAmount() - 1);
                        e.getTo().getBlock().setType(Material.CARROTS);
                        break;
                    } else if(item.getType() == Material.BEETROOT_SEEDS) {
                        item.setAmount(item.getAmount() - 1);
                        e.getTo().getBlock().setType(Material.BEETROOTS);
                        break;
                    } else if(item.getType()== Material.WHEAT_SEEDS){
                        item.setAmount(item.getAmount() - 1);
                        e.getTo().getBlock().setType(Material.WHEAT);
                        break;
                    } else if(item.getType() == Material.NETHER_WART) {
                        item.setAmount(item.getAmount() - 1);
                        e.getTo().getBlock().setType(Material.NETHER_WART_BLOCK);
                        break;
                    }
                }
            }
        }
    }
}
