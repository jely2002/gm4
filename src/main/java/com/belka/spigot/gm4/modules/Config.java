package com.belka.spigot.gm4.modules;

import java.util.Arrays;

import org.bukkit.event.Listener;

public class Config implements Listener {

	private MainClass mc;

	public Config(MainClass mc){
		this.mc = mc;
	}
	
	String[] modules = {"BatGrenades","BetterArmorStands","BetterFire","HeartCanisters","CustomCrafter","DamagingSnowballs","DesireLines","EndermanSupportClass","InkSpittingSquid","VorpelSwords","WeightedArmour"};
	String[] achievements = {"InkSpittingSquid"};
	String[] armor = {"Boots","Leggings","Chestplate","Helmet"};
	
	public void makeConfig() {
		Arrays.sort(modules);
		Arrays.sort(achievements);
		
		for(int i = 0; i < modules.length; i++) {
			mc.getConfig().addDefault(modules[i], true);
		}
		for(int i = 0; i < achievements.length; i++) {
			mc.getConfig().addDefault("achievements." + achievements[i] , "");
		}
		
		mc.getConfig().addDefault("options.DesireLines.max", 50);
		mc.getConfig().addDefault("options.DesireLines.amount", 2);
		
		mc.getConfig().addDefault("options.VorpelSwords.max", 10);
		mc.getConfig().addDefault("options.VorpelSwords.amount", 3);
		
		mc.getConfig().addDefault("options.WeightedArmour.leather.boots" , 1);
		mc.getConfig().addDefault("options.WeightedArmour.leather.leggings" , 2);
		mc.getConfig().addDefault("options.WeightedArmour.leather.chestplate" , 3);
		mc.getConfig().addDefault("options.WeightedArmour.leather.helmet" , 1);
		
		mc.getConfig().addDefault("options.WeightedArmour.chain.boots" , 1);
		mc.getConfig().addDefault("options.WeightedArmour.chain.leggings" , 4);
		mc.getConfig().addDefault("options.WeightedArmour.chain.chestplate" , 5);
		mc.getConfig().addDefault("options.WeightedArmour.chain.helmet" , 2);
		
		mc.getConfig().addDefault("options.WeightedArmour.gold.boots" , 1);
		mc.getConfig().addDefault("options.WeightedArmour.gold.leggings" , 3);
		mc.getConfig().addDefault("options.WeightedArmour.gold.chestplate" , 5);
		mc.getConfig().addDefault("options.WeightedArmour.gold.helmet" , 2);
		
		mc.getConfig().addDefault("options.WeightedArmour.iron.boots" , 2);
		mc.getConfig().addDefault("options.WeightedArmour.iron.leggings" , 5);
		mc.getConfig().addDefault("options.WeightedArmour.iron.chestplate" , 6);
		mc.getConfig().addDefault("options.WeightedArmour.iron.helmet" , 2);
		
		mc.getConfig().addDefault("options.WeightedArmour.diamond.boots" , 3);
		mc.getConfig().addDefault("options.WeightedArmour.diamond.leggings" , 6);
		mc.getConfig().addDefault("options.WeightedArmour.diamond.chestplate" , 8);
		mc.getConfig().addDefault("options.WeightedArmour.diamond.helmet" , 3);
		
		mc.getConfig().addDefault("options.CustomCrafter.rotation", false);
		mc.getConfig().addDefault("options.CustomCrafter.particles", false);
		mc.getConfig().addDefault("options.CustomCrafter.StandardCrafting", true);
		mc.getConfig().addDefault("options.CustomCrafter.RecordCrafting", true);
		
		mc.saveConfig();
	}
}
