package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;

public class PigTractors implements Listener {

    private MainClass mc;

    public PigTractors(MainClass mc) {
        this.mc = mc;
    }

    ArrayList<String> playersOnTractor = new ArrayList<>();

    private boolean isDisabled() {
        return !mc.storage().config().getBoolean("PigTractors.enabled");
    }

    private void removeDurability(Inventory inv) {
        for(int i = 0; i < 35; ++i) {
            if(inv.getItem(i) == null) break;
            if(inv.getItem(i).getType() == Material.DIAMOND_HOE || inv.getItem(i).getType() == Material.GOLDEN_HOE || inv.getItem(i).getType() == Material.IRON_HOE || inv.getItem(i).getType() == Material.STONE_HOE || inv.getItem(i).getType() == Material.WOODEN_HOE) {
                if(inv.getItem(i) instanceof  Damageable) {
                    ((Damageable) inv.getItem(i)).setDamage(((Damageable) inv.getItem(i)).getDamage() + 1);
                }
                return;
            }

        }
    }

    @EventHandler
    public void mountPig(EntityMountEvent e) {
        if(isDisabled()) return;
        if(e.getMount() instanceof Pig && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(p.getInventory().contains(Material.WOODEN_HOE) || p.getInventory().contains(Material.STONE_HOE) || p.getInventory().contains(Material.IRON_HOE) || p.getInventory().contains(Material.GOLDEN_HOE) || p.getInventory().contains(Material.DIAMOND_HOE)) {
                playersOnTractor.add(p.getName());
            }
        }
    }

    @EventHandler
    public void unmountPig(EntityDismountEvent e) {
        if(isDisabled()) return;
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            playersOnTractor.remove(p.getName());
        }
    }

    @EventHandler
    public void hoeRemoved(PlayerDropItemEvent e) {
        if(isDisabled()) return;
        if (playersOnTractor.contains(e.getPlayer().getName())) {
            if (!(e.getPlayer().getInventory().contains(Material.WOODEN_HOE) || e.getPlayer().getInventory().contains(Material.STONE_HOE) || e.getPlayer().getInventory().contains(Material.IRON_HOE) || e.getPlayer().getInventory().contains(Material.GOLDEN_HOE) || e.getPlayer().getInventory().contains(Material.DIAMOND_HOE))) {
                playersOnTractor.remove(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void HoePickedUp(EntityPickupItemEvent e) {
        if(isDisabled()) return;
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(p.getInventory().contains(Material.WOODEN_HOE) || p.getInventory().contains(Material.STONE_HOE) || p.getInventory().contains(Material.IRON_HOE) || p.getInventory().contains(Material.GOLDEN_HOE) || p.getInventory().contains(Material.DIAMOND_HOE)) {
                if(p.isInsideVehicle() && p.getVehicle() instanceof Pig) {
                	if(playersOnTractor.contains(p.getName())) return;
                    playersOnTractor.add(p.getName());
                }
            }
        }
    }

    @EventHandler
    public void isRiding(PlayerMoveEvent e) {
        if(isDisabled()) return;
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if(playersOnTractor.contains(e.getPlayer().getName())) {
            Location blockBelow = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY() - 1, e.getTo().getBlockZ());
			Location blockBelowFrom = new Location(e.getFrom().getWorld(), e.getFrom().getBlockX(), e.getFrom().getBlockY() - 1, e.getFrom().getBlockZ());
            if(blockBelowFrom.getBlock().getType() == Material.FARMLAND) {
                if (!(e.getFrom().getBlock().getType() == Material.BEETROOTS || e.getFrom().getBlock().getType() == Material.CARROTS || e.getFrom().getBlock().getType() == Material.POTATOES || e.getFrom().getBlock().getType() == Material.WHEAT || e.getFrom().getBlock().getType() == Material.NETHER_WART_BLOCK)) {
                    for (int i = 0; i < 35; ++i) {
                        if (e.getPlayer().getInventory().getItem(i) == null) break;
                        ItemStack item = e.getPlayer().getInventory().getItem(i);
                        if (item.getType() == Material.POTATO) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.POTATOES);
                            return;
                        } else if (item.getType() == Material.CARROT) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.CARROTS);
                            return;
                        } else if (item.getType() == Material.BEETROOT_SEEDS) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.BEETROOTS);
                            return;
                        } else if (item.getType() == Material.WHEAT_SEEDS) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.WHEAT);
                            return;
                        } else if (item.getType() == Material.NETHER_WART) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.NETHER_WART_BLOCK);
                            return;
                        }
                    }
                } else {
                    if (e.getTo().getBlock().getType() == Material.BEETROOTS || e.getTo().getBlock().getType() == Material.CARROTS || e.getTo().getBlock().getType() == Material.POTATOES || e.getTo().getBlock().getType() == Material.WHEAT || e.getTo().getBlock().getType() == Material.NETHER_WART_BLOCK) {
                        BlockData bdata = e.getTo().getBlock().getBlockData();
                        if (bdata instanceof Ageable) {
                            Ageable age = (Ageable) bdata;
                            if (age.getAge() == age.getMaximumAge()) {
                                e.getTo().getBlock().breakNaturally();
                                return;
                            }
                        }
                    }
                }
            }
            if(blockBelow.getBlock().getType() == Material.DIRT || blockBelow.getBlock().getType() == Material.GRASS_BLOCK) {
                blockBelow.getBlock().setType(Material.FARMLAND);
                removeDurability(e.getPlayer().getInventory());
            }
        }
    }
}
