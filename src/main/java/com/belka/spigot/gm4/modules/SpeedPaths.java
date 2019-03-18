package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPaths implements Listener {

    private MainClass mc;
    private Material pathBlock;
    private int speedFactor;

    public SpeedPaths(MainClass mc) {
        this.mc = mc;
        loadValues();
    }

    public void loadValues() {
        this.pathBlock = Material.getMaterial(mc.storage().config().getString("SpeedPaths.pathBlock"));
        this.speedFactor = mc.storage().config().getInt("SpeedPaths.speedFactor");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        System.out.println("Moved");
        System.out.println(pathBlock.toString());
        Location player_feet = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY() -0.8, e.getPlayer().getLocation().getZ());
        System.out.println(player_feet.getBlock().getType().toString());
        if (player_feet.getBlock().getType() == pathBlock) {
            System.out.println("adding speed");
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999,speedFactor));
        }
        else if (e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
            e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        }
    }

}
