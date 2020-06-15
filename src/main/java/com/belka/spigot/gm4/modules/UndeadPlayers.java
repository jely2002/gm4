package com.belka.spigot.gm4.modules;

import api.Setting;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.UUID;

public class UndeadPlayers implements Listener, Module {

    private MainClass mc;

    public UndeadPlayers(MainClass mc) {
        this.mc = mc;
    }

	@Override
	public Setting getSetting() { return new Setting("Undead Players", Material.BONE); }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(!mc.getStorage().config().getBoolean("UndeadPlayers.enabled")) return;
        if(e.getKeepInventory()) return;
        if(e.getEntity().getGameMode() == GameMode.SPECTATOR) return;
        UUID entityUUID = e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.ZOMBIE).getUniqueId();
        Zombie zombie = (Zombie) mc.getServer().getEntity(entityUUID);
        zombie.setBaby(false);
        zombie.setCustomName("Undead Player of " + e.getEntity().getName());
        zombie.setCustomNameVisible(true);
        zombie.setCanPickupItems(true);
        zombie.getEquipment().setBoots(null);
        zombie.getEquipment().setHelmet(null);
        zombie.getEquipment().setChestplate(null);
        zombie.getEquipment().setLeggings(null);
        zombie.getEquipment().setItemInMainHand(null);
        zombie.getEquipment().setItemInOffHand(null);
        zombie.getEquipment().setBootsDropChance(1.0f);
        zombie.getEquipment().setHelmetDropChance(1.0f);
        zombie.getEquipment().setChestplateDropChance(1.0f);
        zombie.getEquipment().setLeggingsDropChance(1.0f);
        zombie.getEquipment().setItemInMainHandDropChance(1.0f);
        zombie.getEquipment().setItemInOffHandDropChance(1.0f);
        Advancements.grantAdvancement("risen", e.getEntity());
    }

}
