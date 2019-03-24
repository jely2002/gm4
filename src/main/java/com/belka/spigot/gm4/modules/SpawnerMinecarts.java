package com.belka.spigot.gm4.modules;

import api.Helper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class SpawnerMinecarts implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Bukkit.broadcastMessage("Ev " + e.getBlockAgainst().getType().toString());
        if (e.getBlock().getType() == Material.REDSTONE_BLOCK) {
            Bukkit.broadcastMessage("redstone");
            if (e.getBlockAgainst().getType() == Material.PISTON) {
                Piston piston = (Piston) e.getBlockAgainst().getBlockData();
                if (piston.getFacing().equals(BlockFace.DOWN)) {
                    Bukkit.broadcastMessage("down");
                    if (e.getBlockAgainst().getRelative(BlockFace.DOWN).getType() == Material.SPAWNER) {
                        Block sp = e.getBlockAgainst().getRelative(BlockFace.DOWN);
                        CreatureSpawner spawner = (CreatureSpawner) sp.getState();
                        Bukkit.broadcastMessage("spawner " + sp.getLocation().subtract(0,1,0).getBlock().getType().toString());
                        for (Entity entity : Helper.getNearbyEntities(sp.getLocation().subtract(0, 1, 0), 1)) {
                            if (entity instanceof Minecart) {
                                Bukkit.broadcastMessage("mc");
                                Minecart m = (Minecart) entity;
                                if (m.getPassengers().size() == 0) {
                                    Bukkit.broadcastMessage(m.getDisplayBlock().getItemType().toString());
                                    if (m.getDisplayBlock().getItemType() == Material.AIR || m.getDisplayBlock().getItemType() == Material.LEGACY_AIR) {
                                        Bukkit.broadcastMessage("Air");
                                        m.setDisplayBlock(spawner.getData());
                                        sp.setType(Material.AIR);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private Block getPiston(Block b) {
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(b.getRelative(1, 0, 0));
        blocks.add(b.getRelative(-1, 0, 0));
        blocks.add(b.getRelative(0, -1, 0));
        blocks.add(b.getRelative(0, -1, 0));
        blocks.add(b.getRelative(0, 0, 1));
        blocks.add(b.getRelative(0, 0, -1));
        for (Block block : blocks) {
            if (block.getType() == Material.PISTON) return block;
        }
        return b;
    }
}
