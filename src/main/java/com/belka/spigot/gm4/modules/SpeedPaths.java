package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
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
        pathBlock = Material.getMaterial(mc.storage().config().getString("SpeedPaths.pathBlock"));
        speedFactor = mc.storage().config().getInt("SpeedPaths.speedFactor");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
		if (!mc.getConfig().getBoolean("SpeedPaths.enabled")) return;
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        Location pLoc = e.getPlayer().getLocation().getBlock().getLocation();
        Location block1 = new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY() - 1, pLoc.getZ());
		Location block2 = new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY() - 2, pLoc.getZ());
        if (pLoc.getBlock().getType() == pathBlock || block1.getBlock().getType() == pathBlock || block2.getBlock().getType() == pathBlock) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, speedFactor, true, false));
        }
        else if (e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
            e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        }
    }

}
