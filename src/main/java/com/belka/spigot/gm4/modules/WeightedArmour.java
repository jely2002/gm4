package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WeightedArmour implements Module, Listener {

	private MainClass mc;

	public WeightedArmour(MainClass mc){
		this.mc = mc;
	}
    
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

	public int level0 = 8;
	public int level1 = 16;
	public int level2 = 20;

	@Override
	public void init(MainClass mc) {
		if(!mc.getStorage().config().getBoolean("WeightedArmour.enabled")) return;
		
		leatherBoots = mc.getStorage().config().getInt("WeightedArmour.weight.leather.boots");
		leatherLeggings = mc.getStorage().config().getInt("WeightedArmour.weight.leather.leggings");
		leatherChestplate = mc.getStorage().config().getInt("WeightedArmour.weight.leather.chestplate");
		leatherHelmet = mc.getStorage().config().getInt("WeightedArmour.weight.leather.helmet");

		chainBoots = mc.getStorage().config().getInt("WeightedArmour.weight.chain.boots");
		chainLeggings = mc.getStorage().config().getInt("WeightedArmour.weight.chain.leggings");
		chainChestplate = mc.getStorage().config().getInt("WeightedArmour.weight.chain.chestplate");
		chainHelmet = mc.getStorage().config().getInt("WeightedArmour.weight.chain.helmet");

		goldBoots = mc.getStorage().config().getInt("WeightedArmour.weight.gold.boots");
		goldLeggings = mc.getStorage().config().getInt("WeightedArmour.weight.gold.leggings");
		goldChestplate = mc.getStorage().config().getInt("WeightedArmour.weight.gold.chestplate");
		goldHelmet = mc.getStorage().config().getInt("WeightedArmour.weight.gold.helmet");

		ironBoots = mc.getStorage().config().getInt("WeightedArmour.weight.iron.boots");
		ironLeggings = mc.getStorage().config().getInt("WeightedArmour.weight.iron.leggings");
		ironChestplate = mc.getStorage().config().getInt("WeightedArmour.weight.iron.chestplate");
		ironHelmet = mc.getStorage().config().getInt("WeightedArmour.weight.iron.helmet");

		diamondBoots = mc.getStorage().config().getInt("WeightedArmour.weight.diamond.boots");
		diamondLeggings = mc.getStorage().config().getInt("WeightedArmour.weight.diamond.leggings");
		diamondChestplate = mc.getStorage().config().getInt("WeightedArmour.weight.diamond.chestplate");
		diamondHelmet = mc.getStorage().config().getInt("WeightedArmour.weight.diamond.helmet");

		level0 = mc.getStorage().config().getInt("WeightedArmour.level-0");
		level1 = mc.getStorage().config().getInt("WeightedArmour.level-1");
		level2 = mc.getStorage().config().getInt("WeightedArmour.level-2");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(!mc.getStorage().config().getBoolean("WeightedArmour.enabled")) return;
		if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
		Player p = e.getPlayer();
		int weight = getArmorWeight(p);
		if(weight >= level0 && weight < level1) {
			if (e.getPlayer().hasPotionEffect(PotionEffectType.SLOW) && p.getPotionEffect(PotionEffectType.SLOW).getAmplifier() != 0) {
				e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 0, true, false));
		}
		else if(weight >= level1 && weight < level2) {
			if (e.getPlayer().hasPotionEffect(PotionEffectType.SLOW) && p.getPotionEffect(PotionEffectType.SLOW).getAmplifier() != 1) {
				e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 1, true, false));
		}
		else if(weight >= level2) {
			if (e.getPlayer().hasPotionEffect(PotionEffectType.SLOW) && p.getPotionEffect(PotionEffectType.SLOW).getAmplifier() != 2) {
				e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
			}
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 99999, 2, true, false));
		}
		else if (e.getPlayer().hasPotionEffect(PotionEffectType.SLOW)) {
			e.getPlayer().removePotionEffect(PotionEffectType.SLOW);
		}
	}
	
	public int getArmorWeight(Player p) {
		int weight = getBootsWeight(p) + getLeggingsWeight(p) + getChestplateWeight(p) + getHelmetWeight(p);
		return weight;
	}
	
	public int getBootsWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getBoots() != null) {
			switch (inv.getBoots().getType()) {
				case LEATHER_BOOTS:
					return leatherBoots;
				case CHAINMAIL_BOOTS:
					return chainBoots;
				case GOLDEN_BOOTS:
					return goldBoots;
				case IRON_BOOTS:
					return ironBoots;
				case DIAMOND_BOOTS:
					return diamondBoots;
				default:
					return 0;
			}
		}
		return 0;
	}
	public int getLeggingsWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getLeggings() != null) {
			switch (inv.getLeggings().getType()) {
				case LEATHER_LEGGINGS:
					return leatherLeggings;
				case CHAINMAIL_LEGGINGS:
					return chainLeggings;
				case GOLDEN_LEGGINGS:
					return goldLeggings;
				case IRON_LEGGINGS:
					return ironLeggings;
				case DIAMOND_LEGGINGS:
					return diamondLeggings;
				default:
					return 0;
			}
		}
		return 0;
	}
	public int getChestplateWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getChestplate() != null) {
			switch (inv.getChestplate().getType()) {
				case LEATHER_CHESTPLATE:
					return leatherChestplate;
				case CHAINMAIL_CHESTPLATE:
					return chainChestplate;
				case GOLDEN_CHESTPLATE:
					return goldChestplate;
				case IRON_CHESTPLATE:
					return ironChestplate;
				case DIAMOND_CHESTPLATE:
					return diamondChestplate;
				default:
					return 0;
			}
		}
		return 0;
	}
	public int getHelmetWeight(Player p) {
		PlayerInventory inv = p.getInventory();
		if (inv.getHelmet() != null) {
			switch (inv.getHelmet().getType()) {
				case LEATHER_HELMET:
					return leatherHelmet;
				case CHAINMAIL_HELMET:
					return chainHelmet;
				case GOLDEN_HELMET:
					return goldHelmet;
				case IRON_HELMET:
					return ironHelmet;
				case DIAMOND_HELMET:
					return diamondHelmet;
				default:
					return 0;
			}
		}
		return 0;
	}
}
