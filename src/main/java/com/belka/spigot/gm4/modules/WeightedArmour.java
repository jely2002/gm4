package com.belka.spigot.gm4.modules;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WeightedArmour implements Listener {

	private MainClass mc;

	public WeightedArmour(MainClass mc){
		this.mc = mc;
	}

    public int weight = 0;
    
    public int boots = 0;
    public int leggings = 0;
    public int chestplate = 0;
    public int helmet = 0;
    
    public int leatherBoots = 1;
    public int leatherLeggings = 2;
    public int leatherChestplate = 3;
    public int leatherHelmet = 1;

    public int chainBoots = 1;
    public int chainLeggings = 4;
    public int chainChestplate = 5;
    public int chainHelmet = 2;

    public int goldBoots = 1;
    public int goldLeggings = 3;
    public int goldChestplate = 5;
    public int goldHelmet = 2;

    public int ironBoots = 2;
    public int ironLeggings = 5;
    public int ironChestplate = 6;
    public int ironHelmet = 2;

    public int diamondBoots = 3;
    public int diamondLeggings = 6;
    public int diamondChestplate = 8;
    public int diamondHelmet = 3;
    
	public void start() {
		mc.getServer().getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
			@Override
			public void run() {
				if(mc.getConfig().getBoolean("WeightedArmour") == true) {
					loadArmorWeight();
					for(Player p : Bukkit.getOnlinePlayers()) {
						weight = getArmorWeight(p);
						if(weight >= 8 && weight < 16) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 0), false);
						}
						else if(weight >= 16 && weight < 20) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 1), false);
						}
						else if(weight >= 20) {
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 2), false);
						}
					}
				}
			}
		}, 0, (long) 0.05 * 20L); // Seconds * 20L
	}
	
	public int getArmorWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if(inv.getBoots() != null) {
			boots = getBootsWeight(p);
		}
		if(inv.getLeggings() != null) {
			leggings = getLeggingsWeight(p);
		}
		if(inv.getChestplate() != null) {
			chestplate = getChestplateWeight(p);
		}
		if(inv.getHelmet() != null) {
			helmet = getHelmetWeight(p);
		}
		if(inv.getBoots() == null) {
			boots = 0;
		}
		if(inv.getLeggings() == null) {
			leggings = 0;
		}
		if(inv.getChestplate() == null) {
			chestplate = 0;
		}
		if(inv.getHelmet() == null) {
			helmet = 0;
		}
		weight = boots + leggings + chestplate + helmet;
		return weight;
	}
	
	public int getBootsWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getBoots() != null) {
			if(inv.getBoots().getType().equals(Material.LEATHER_BOOTS)) {
				boots = leatherBoots;
			}
			else if(inv.getBoots().getType().equals(Material.CHAINMAIL_BOOTS)) {
				boots = chainBoots;
			}
			else if(inv.getBoots().getType().equals(Material.GOLD_BOOTS)) {
				boots = goldBoots;
			}
			else if(inv.getBoots().getType().equals(Material.IRON_BOOTS)) {
				boots = ironBoots;
			}
			else if(inv.getBoots().getType().equals(Material.DIAMOND_BOOTS)) {
				boots = diamondBoots;
			}
			else {
				boots = 0;
			}
		}
		else {
			boots = 0;
		}
		return boots;
	}
	public int getLeggingsWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getLeggings() != null) {
			if(inv.getLeggings().getType().equals(Material.LEATHER_LEGGINGS)) {
				return leatherLeggings;
			}
			else if(inv.getLeggings().getType().equals(Material.CHAINMAIL_LEGGINGS)) {
				leggings = chainLeggings;
			}
			else if(inv.getLeggings().getType().equals(Material.GOLD_LEGGINGS)) {
				leggings = goldLeggings;
			}
			else if(inv.getLeggings().getType().equals(Material.IRON_LEGGINGS)) {
				leggings = ironLeggings;
			}
			else if(inv.getLeggings().getType().equals(Material.DIAMOND_LEGGINGS)) {
				leggings = diamondLeggings;
			}
			else {
				leggings = 0;
			}
		}
		else {
			leggings = 0;
		}
		return leggings;
	}
	public int getChestplateWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getChestplate() != null) {
			if(inv.getChestplate().getType().equals(Material.LEATHER_CHESTPLATE)) {
				chestplate = leatherChestplate;
			}
			else if(inv.getChestplate().getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
				chestplate = chainChestplate;
			}
			else if(inv.getChestplate().getType().equals(Material.GOLD_CHESTPLATE)) {
				chestplate = goldChestplate;
			}
			else if(inv.getChestplate().getType().equals(Material.IRON_CHESTPLATE)) {
				chestplate = ironChestplate;
			}
			else if(inv.getChestplate().getType().equals(Material.DIAMOND_CHESTPLATE)) {
				chestplate = diamondChestplate;
			}
			else {
				chestplate = 0;
			}
		}
		else {
			chestplate = 0;
		}
		return chestplate;
	}
	public int getHelmetWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getHelmet() != null) {
			if(inv.getHelmet().getType().equals(Material.LEATHER_HELMET)) {
				helmet = leatherHelmet;
			}
			else if(inv.getHelmet().getType().equals(Material.CHAINMAIL_HELMET)) {
				helmet = chainHelmet;
			}
			else if(inv.getHelmet().getType().equals(Material.GOLD_HELMET)) {
				helmet = goldHelmet;
			}
			else if(inv.getHelmet().getType().equals(Material.IRON_HELMET)) {
				helmet = ironHelmet;
			}
			else if(inv.getHelmet().getType().equals(Material.DIAMOND_HELMET)) {
				helmet = diamondHelmet;
			}
			else {
				helmet = 0;
			}
		}
		else {
			helmet = 0;
		}
		return helmet;
	}
	
	public void loadArmorWeight() {
		leatherBoots = mc.getConfig().getInt("options.WeightedArmour.leather.boots");
		leatherLeggings = mc.getConfig().getInt("options.WeightedArmour.leather.leggings");
		leatherChestplate = mc.getConfig().getInt("options.WeightedArmour.leather.chestplate");
		leatherHelmet = mc.getConfig().getInt("options.WeightedArmour.leather.helmet");
					
		chainBoots = mc.getConfig().getInt("options.WeightedArmour.chain.boots");
		chainLeggings = mc.getConfig().getInt("options.WeightedArmour.chain.leggings");
		chainChestplate = mc.getConfig().getInt("options.WeightedArmour.chain.chestplate");
		chainHelmet = mc.getConfig().getInt("options.WeightedArmour.chain.helmet");
					
		goldBoots = mc.getConfig().getInt("options.WeightedArmour.gold.boots");
		goldLeggings = mc.getConfig().getInt("options.WeightedArmour.gold.leggings");
		goldChestplate = mc.getConfig().getInt("options.WeightedArmour.gold.chestplate");
		goldHelmet = mc.getConfig().getInt("options.WeightedArmour.gold.helmet");
					
		ironBoots = mc.getConfig().getInt("options.WeightedArmour.iron.boots");
		ironLeggings = mc.getConfig().getInt("options.WeightedArmour.iron.leggings");
		ironChestplate = mc.getConfig().getInt("options.WeightedArmour.iron.chestplate");
		ironHelmet = mc.getConfig().getInt("options.WeightedArmour.iron.helmet");
					
		diamondBoots = mc.getConfig().getInt("options.WeightedArmour.diamond.boots");
		diamondLeggings = mc.getConfig().getInt("options.WeightedArmour.diamond.leggings");
		diamondChestplate = mc.getConfig().getInt("options.WeightedArmour.diamond.chestplate");
		diamondHelmet = mc.getConfig().getInt("options.WeightedArmour.diamond.helmet");
	}
}
