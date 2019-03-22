package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class TrappedSigns implements Listener {

    private MainClass mc;

    public TrappedSigns(MainClass mc) {
        this.mc = mc;
    }

    @EventHandler
    public void signInteract(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (!(e.getClickedBlock().getType() == Material.SIGN)) return;
        if(!mc.storage().data().contains("TrappedSigns.0")) return;
        for (String id : mc.storage().data().getConfigurationSection("TrappedSigns").getKeys(false)) {
            if (e.getClickedBlock().getLocation().getBlockX() == mc.storage().data().getInt("TrappedSigns." + id + ".x")) {
                if(e.getClickedBlock().getLocation().getBlockY() == mc.storage().data().getInt("TrappedSigns." + id + ".y")) {
                    if(e.getClickedBlock().getLocation().getBlockZ() == mc.storage().data().getInt("TrappedSigns." + id + ".z")) {
                        if(e.getClickedBlock().getWorld().getName() == mc.storage().data().getString("TrappedSigns." + id + ".world")) {
                            for(Block b : Helper.getNearbyBlocks(e.getClickedBlock().getLocation(), 2)) {
                                if(b.getType() == Material.REDSTONE_WIRE) {
                                    RedstoneWire r = (RedstoneWire) b.getState();
                                    r.setPower(r.getMaximumPower());
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
        if(!(e.getBlockPlaced().getType() == Material.SIGN)) return;
        if(!e.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§b Trapped Sign")) return;
        if(!e.getItemInHand().getItemMeta().getLore().get(0).contains("trapped")) return;
        Sign sign = (Sign) e.getBlockPlaced().getState();
        addSign(sign.getLocation());
    }

    private void addSign(Location loc) {
        int id = mc.storage().data().getConfigurationSection("TrappedSigns").getKeys(false).size();
        mc.storage().data().set("TrappedSigns." + id + ".x", loc.getBlockX());
        mc.storage().data().set("TrappedSigns." + id + ".y", loc.getBlockY());
        mc.storage().data().set("TrappedSigns." + id + ".z", loc.getBlockZ());
        mc.storage().data().set("TrappedSigns." + id + ".world", loc.getWorld().getName());
        mc.storage().saveData();
    }

}
