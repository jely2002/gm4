package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPaths implements Listener {

    private MainClass mc;
    private Material pathBlock;

    public SpeedPaths(MainClass mc) {
        this.mc = mc;
        this.pathBlock = Material.getMaterial(mc.storage().config().getString("modules.speedpaths.path-block"));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        if(e.getTo().getBlock().getType() == pathBlock ) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999,3));
        } else if(e.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
            e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        }
    }

}
