package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Bukkit;
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
        return !mc.getStorage().config().getBoolean("PigTractors.enabled");
    }

    private void callAchievement(Player p) {
        Advancements.grantAdvancement("oink_tractors", p);
    }

    private void removeDurability(Inventory inv) {
        for(int i = 0; i < 35; ++i) {
            if(inv.getItem(i) == null) continue;
            if(inv.getItem(i).getType() == Material.DIAMOND_HOE || inv.getItem(i).getType() == Material.GOLDEN_HOE || inv.getItem(i).getType() == Material.IRON_HOE || inv.getItem(i).getType() == Material.STONE_HOE || inv.getItem(i).getType() == Material.WOODEN_HOE) {
                if(inv.getItem(i) instanceof  Damageable) {
                    ((Damageable) inv.getItem(i)).setDamage(((Damageable) inv.getItem(i)).getDamage() + 1);
                }
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
                callAchievement(p);
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
                    callAchievement(p);
                }
            }
        }
    }
//TODO Add working behavior.
    /*@EventHandler
    public void isRiding(PlayerMoveEvent e) {
        if (isDisabled()) return;
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if (playersOnTractor.contains(e.getPlayer().getName())) {
            Location blockBelow = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY() - 1, e.getTo().getBlockZ());
            blockBelow = e.getTo().subtract(0,1,0);
			Location blockBelowFrom = new Location(e.getFrom().getWorld(), e.getFrom().getBlockX(), e.getFrom().getBlockY() - 1, e.getFrom().getBlockZ());
			blockBelowFrom = e.getFrom().subtract(0,1,0);
            Bukkit.broadcastMessage("blockBelow " + blockBelow.getBlock().getType());
            Bukkit.broadcastMessage("blockBelowFrom " + blockBelowFrom.getBlock().getType());
            if(e.getTo().add(0, 0.0625, 0).getBlock().getType() == Material.BEETROOTS || e.getTo().add(0, 0.0625, 0).getBlock().getType() == Material.CARROTS || e.getTo().add(0, 0.0625, 0).getBlock().getType() == Material.POTATOES ||e.getTo().add(0, 0.0625, 0).getBlock().getType() == Material.WHEAT || e.getTo().add(0, 0.0625, 0).getBlock().getType() == Material.NETHER_WART_BLOCK) {
                e.getPlayer().sendMessage("1");
            	BlockData bdata = e.getTo().getBlock().getBlockData();
                if(bdata instanceof Ageable) {
                    Ageable age = (Ageable) bdata;
                    if (age.getAge() == age.getMaximumAge()) {
                        e.getTo().getBlock().breakNaturally();
                        e.getPlayer().sendMessage("Broken crop");
                        return;
                    }
                }
            }
            if(blockBelowFrom.getBlock().getType() == Material.FARMLAND) {
            	e.getPlayer().sendMessage("2");
                if(e.getFrom().add(0, 0.0625, 0).getBlock().getType() != Material.BEETROOTS || e.getFrom().add(0, 0.0625, 0).getBlock().getType() != Material.CARROTS || e.getFrom().add(0, 0.0625, 0).getBlock().getType() != Material.POTATOES ||e.getFrom().add(0, 0.0625, 0).getBlock().getType() != Material.WHEAT || e.getFrom().add(0, 0.0625, 0).getBlock().getType() != Material.NETHER_WART_BLOCK) {
                    for (int i = 0; i < 35; ++i) {
                        if (e.getPlayer().getInventory().getItem(i) == null) break;
                        ItemStack item = e.getPlayer().getInventory().getItem(i);
                        if (item.getType() == Material.POTATO) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.POTATOES);
                            e.getPlayer().sendMessage("Placed crop");
                            break;
                        } else if (item.getType() == Material.CARROT) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.CARROTS);
                            e.getPlayer().sendMessage("Placed crop");
                            break;
                        } else if (item.getType() == Material.BEETROOT_SEEDS) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.BEETROOTS);
                            e.getPlayer().sendMessage("Placed crop");
                            break;
                        } else if (item.getType() == Material.WHEAT_SEEDS) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.WHEAT);
                            e.getPlayer().sendMessage("Placed crop");
                            break;
                        } else if (item.getType() == Material.NETHER_WART) {
                            item.setAmount(item.getAmount() - 1);
                            e.getFrom().add(0, 0.0625, 0).getBlock().setType(Material.NETHER_WART_BLOCK);
                            e.getPlayer().sendMessage("Placed crop");
                            break;
                        }
                    }
                }
                return;
            }
            if(blockBelow.getBlock().getType() == Material.DIRT || blockBelow.getBlock().getType() == Material.GRASS_BLOCK) {
            	e.getPlayer().sendMessage("3");
                blockBelow.getBlock().setType(Material.FARMLAND);
//                removeDurability(e.getPlayer().getInventory());
                e.getPlayer().sendMessage("made farmland");
            }
        }
    }*/
}
