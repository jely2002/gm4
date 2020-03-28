package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SpeedPaths implements Module, Listener {

    private MainClass mc;
    private Material pathBlock;
    private int speedFactor;
    private List<Player> speedPlayers = new ArrayList<>();

    public SpeedPaths(MainClass mc) {
        this.mc = mc;
        loadValues();
    }

    @Override
    public void reload() {
        loadValues();
    }

    public void loadValues() {
        pathBlock = Material.getMaterial(mc.getStorage().config().getString("SpeedPaths.pathBlock"));
        speedFactor = mc.getStorage().config().getInt("SpeedPaths.speedFactor");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
		if (!mc.getStorage().config().getBoolean("SpeedPaths.enabled")) return;
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        Location pLoc = e.getPlayer().getLocation().getBlock().getLocation();
        Location block1 = new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY() - 1, pLoc.getZ());
		Location block2 = new Location(pLoc.getWorld(), pLoc.getX(), pLoc.getY() - 2, pLoc.getZ());
        if (pLoc.getBlock().getType() == pathBlock || block1.getBlock().getType() == pathBlock || block2.getBlock().getType() == pathBlock) {
            if(e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) return;
            if(speedPlayers.contains(e.getPlayer())) return;
            speedPlayers.add(e.getPlayer());
            e.getPlayer().setWalkSpeed(e.getPlayer().getWalkSpeed() * speedFactor);
        }
        else if (speedPlayers.contains(e.getPlayer())) {
            speedPlayers.remove(e.getPlayer());
            e.getPlayer().setWalkSpeed(0.2f);
        }
    }
}
