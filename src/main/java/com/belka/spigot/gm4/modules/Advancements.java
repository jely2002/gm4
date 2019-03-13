package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import eu.endercentral.crazy_advancements.*;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Advancements implements Initializable {
	
	private static MainClass mc;
	private static AdvancementManager manager = CrazyAdvancements.getNewAdvancementManager();

	public Advancements(MainClass mc) {
		Advancements.mc = mc;
	}

    public void init(MainClass mc) {
		Bukkit.broadcastMessage("Init Advancements");
		AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.ARMOR_STAND, "My Custom Advancements", "With cool additions", AdvancementDisplay.AdvancementFrame.TASK, false, false, AdvancementVisibility.ALWAYS);
		rootDisplay.setBackgroundTexture("textures/blocks/concrete_yellow.png");
		Advancement root = new Advancement(null, new NameKey("custom", "root"), rootDisplay);

		AdvancementDisplay childrenDisplay = new AdvancementDisplay(Material.ENDER_EYE, "To the right", "Your Goal", AdvancementDisplay.AdvancementFrame.GOAL, true, true, AdvancementVisibility.VANILLA);
		childrenDisplay.setCoordinates(1, 0);//x, y
		Advancement children = new Advancement(root, new NameKey("custom", "right"), childrenDisplay);

		manager.addAdvancement(root, children);
		Bukkit.broadcastMessage("Added Advancements");
		// OLD
//		String worldName = Bukkit.getWorlds().get(0).getName();
//		Bukkit.unloadWorld(worldName, false);
//
//		AdvancementAPI gm4 = AdvancementAPI.builder(new NamespacedKey(mc, "gm4/start"))
//			.title("Gamemode 4")
//			.description("Vanilla Re-Imagined")
//			.icon("minecraft:command_block")
//			.trigger(Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"))
//			.announce(false)
//			.hidden(false)
//			.toast(false)
//			.background("minecraft:textures/blocks/concrete_cyan.png")
//			.frame(FrameType.CHALLENGE)
//			.build();
//
//		gm4.save(worldName);
//
//		addAdvancementToParent("gm4/risen", "Risen", "Die and raise up an undead zombie in your body's place.", "minecraft:totem_of_undying",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.TASK, gm4, worldName);
//
//		addAdvancementToParent("gm4/all_my_audreys", "All my Audreys", "Find all Dearest Audrey messages in a bottle.", "minecraft:glass_bottle",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.TASK, gm4, worldName);
//
//		addAdvancementToParent("gm4/a_fun_gi", "A Fun-gi", "Decor some decorative mushroom.", "minecraft:red_mushroom_block",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.TASK, gm4, worldName);
//
//		// CUSTOM CRAFTING
//		AdvancementAPI cc = AdvancementAPI.builder(new NamespacedKey(mc, "gm4/clever_crafting"))
//			.title("Clever Crafting")
//			.description("Build yourself a Custom Crafter with droppers and a crafting table.")
//			.icon("minecraft:crafting_table")
//			.trigger(Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"))
//			.announce(true)
//			.hidden(false)
//			.toast(true)
//			.background(gm4.getBackground())
//			.frame(FrameType.CHALLENGE)
//    		.parent(gm4.getId().toString())
//			.build();
//
//		cc.save(worldName);
//
//		addAdvancementToParent("gm4/clever_smelting", "Clever Smelting", "Create your own Blast Furnace.", "minecraft:furnace",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.GOAL, cc, worldName);
//
//		addAdvancementToParent("gm4/clever_decrafting", "Clever Decrafting", "Assemble a Disassembler.", "minecraft:dropper",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.GOAL, cc, worldName);
//
//		addAdvancementToParent("gm4/clever_crushing", "Clever Crushing", "Make yourself a new Block Compressor using a Custom Crafter.", "minecraft:piston",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.GOAL, cc, worldName);
//
//		addAdvancementToParent("gm4/green_feet", "Green Feet", "Wear the Boots of Ostara.", "minecraft:leather_boots",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.GOAL, cc, worldName);
//
//		// HEART CANISTER
//		AdvancementAPI hc = AdvancementAPI.builder(new NamespacedKey(mc, "gm4/cant_buy_me_love"))
//			.title("Can't buy me love")
//			.description("Craft a Tier 1 Heart Canister.")
//			.icon("minecraft:dragon_breath")
//			.trigger(Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"))
//			.announce(true)
//			.hidden(false)
//			.toast(true)
//			.background(gm4.getBackground())
//			.frame(FrameType.TASK)
//    		.parent(gm4.getId().toString())
//			.build();
//
//		hc.save(worldName);
//
//		AdvancementAPI hc2 = AdvancementAPI.builder(new NamespacedKey(mc, "gm4/love_is_all_you_need"))
//			.title("Love is all you need")
//			.description("Craft a Tier 2 Heart Canister.")
//			.icon("minecraft:dragon_breath")
//			.trigger(Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"))
//			.announce(true)
//			.hidden(false)
//			.toast(true)
//			.background(gm4.getBackground())
//			.frame(FrameType.TASK)
//	    	.parent(hc.getId().toString())
//			.build();
//
//		hc2.save(worldName);
//
//		addAdvancementToParent("gm4/all_my_loving", "All My Loving.", "Max out your Heart Canister health", "minecraft:dragon_breath",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.TASK, hc2, worldName);
//
//		// MOBS
//		AdvancementAPI mobs = AdvancementAPI.builder(new NamespacedKey(mc, "gm4/mobs"))
//			.title("Mobs")
//			.description("")
//			.icon("minecraft:iron_sword")
//			.trigger(Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"))
//			.announce(false)
//			.hidden(false)
//			.toast(false)
//			.background(gm4.getBackground())
//			.frame(FrameType.CHALLENGE)
//    		.parent(gm4.getId().toString())
//			.build();
//
//		mobs.save(worldName);
//
//		addAdvancementToParent("gm4/natural_defences", "Natural Defences", "Get blinded by an underwater squid.", "minecraft:ink_sac",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.TASK, mobs, worldName);
//
//		addAdvancementToParent("gm4/ender_aid", "Ender Aid", "Come in contact with a support Enderman.", "minecraft:ender_pearl",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.TASK, mobs, worldName);
//
//		addAdvancementToParent("gm4/batboozled", "Batboozled", "Get blown up by a Bat Grenade. XD", "minecraft:tnt",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.TASK, mobs, worldName);
//
//		addAdvancementToParent("gm4/dr_frankenstein", "Dr. Frankenstein", "It's alive! Create a Shulker.", "minecraft:purple_shulker_box",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.CHALLENGE, mobs, worldName);
//
//		addAdvancementToParent("gm4/instant_regret", "Instant Regret", "Wither-ize a skeleton with potion swords.", "minecraft:coal",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.CHALLENGE, mobs, worldName);
//
//		// ARMOR STANDS
//		AdvancementAPI ar = AdvancementAPI.builder(new NamespacedKey(mc, "gm4/gettin_handsy"))
//			.title("Gettin' Handsy")
//			.description("Give an armor stand arms.")
//			.icon("minecraft:armor_stand")
//			.trigger(Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"))
//			.announce(true)
//			.hidden(false)
//			.toast(true)
//			.background(gm4.getBackground())
//			.frame(FrameType.GOAL)
//    		.parent(gm4.getId().toString())
//			.build();
//
//		ar.save(worldName);
//
//		addAdvancementToParent("gm4/plenty_o_posing", "Plenty o' Posing", "Make your armor stand strike a pose.", "minecraft:armor_stand",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.GOAL, ar, worldName);
//
//		Bukkit.createWorld(WorldCreator.name(worldName));
	}

//	private void addAdvancementToParent(String key, String title, String desc, String icon, TriggerBuilder trigger, Boolean announce, Boolean hidden, Boolean toast, FrameType frame, AdvancementAPI parent, String world) {
//    	AdvancementAPI advancementAPI = AdvancementAPI.builder(new NamespacedKey(mc, key))
//    			.title(title)
//    			.description(desc)
//    			.icon(icon)
//    			.trigger(trigger)
//    			.announce(announce)
//    			.hidden(hidden)
//    			.toast(toast)
//    			.background(parent.getBackground())
//    			.frame(frame)
//    			.parent(parent.getId().toString())
//    			.build();
//    		advancementAPI.save(world);
//    }
	
	public static void grantAdvancement(String advName, Player p) {
		manager.grantAdvancement(p, manager.getAdvancement(new NameKey(advName, "root")));
//		NamespacedKey key = new NamespacedKey(mc, advName);
//		AdvancementProgress progress = p.getAdvancementProgress(Bukkit.getAdvancement(key));
//		for(String criteria : progress.getRemainingCriteria()) {
//		    progress.awardCriteria(criteria);
//		}
	}
}





