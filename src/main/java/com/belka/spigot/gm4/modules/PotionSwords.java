package com.belka.spigot.gm4.modules;

import api.Helper;
import com.belka.spigot.gm4.MainClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.loot.LootTables;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class PotionSwords implements Listener {

    private MainClass mc;

    public PotionSwords(MainClass mc) {
        this.mc = mc;
    }

  @EventHandler
  public void onEntityDamage(EntityDamageByEntityEvent e) {
      if(!mc.getStorage().config().getBoolean("PotionSwords.enabled")) return;
      if(e.getDamager() instanceof Player) {
          Player p = (Player) e.getDamager();
          if(p.getInventory().getItemInMainHand().getType() == Material.GOLDEN_SWORD) {
              if(p.getInventory().contains(getPotionItemStack(PotionType.SLOWNESS, true, false))) {
                  for(Entity nearbyEntity : Helper.getNearbyEntities(p.getLocation(), 3)) {
                      if(nearbyEntity instanceof Mob) {
                          ((Mob) nearbyEntity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 4, 9999));
                      }
                  }
              }
              if(p.getInventory().contains(getPotionItemStack(PotionType.POISON, false, true))) {
                  if(e.getEntity() instanceof Skeleton) {
                      Skeleton skeleton = (Skeleton) e.getEntity();
                      if(skeleton.getType() == EntityType.SKELETON) {
                          Advancements.grantAdvancement("instant_regret", p);
                          Location skeletonLoc = skeleton.getLocation();
                          Double skeletonHealth = skeleton.getHealth();
                          skeletonLoc.getWorld().spawnEntity(skeletonLoc, EntityType.WITHER_SKELETON);
                          skeleton.remove();
                          for (Entity nearbyEntity : Helper.getNearbyEntities(skeletonLoc, 0.1)) {
                              if (nearbyEntity instanceof WitherSkeleton) {
                                  WitherSkeleton witherSkeleton = (WitherSkeleton) nearbyEntity;
                                  witherSkeleton.setLootTable(LootTables.EMPTY.getLootTable());
                                  witherSkeleton.setHealth(skeletonHealth);
                              }
                          }
                      }
                  }
                  for(Entity nearbyEntity : Helper.getNearbyEntities(p.getLocation(), 3)) {
                      if(nearbyEntity instanceof Mob) {
                          ((Mob) nearbyEntity).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 4, 1));
                      }
                  }
              }
              if(p.getInventory().contains(getPotionItemStack(PotionType.INSTANT_HEAL, false, true))) {
                  for(Player nearbyPlayer : Helper.getNearbyPlayers(p.getLocation(), 3)) {
                      nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,20*8,1));
                  }
              }
              if(p.getInventory().contains(getPotionItemStack(PotionType.INVISIBILITY, true, false))) {
                  for(Player nearbyPlayer : Helper.getNearbyPlayers(p.getLocation(), 3)) {
                      nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20*8,1));
                  }
              }
          }
      }
  }

    private ItemStack getPotionItemStack(PotionType type, boolean extend, boolean upgraded) {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionData(new PotionData(type, extend, upgraded));
        potion.setItemMeta(meta);
        return potion;
    }

}
