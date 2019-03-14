package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import eu.endercentral.crazy_advancements.*;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import eu.endercentral.crazy_advancements.AdvancementDisplay.AdvancementFrame;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Advancements implements Listener, Initializable {
	
	private MainClass mc;
	private static AdvancementManager manager;

	public Advancements(MainClass mc) {
		this.mc = mc;
	}

    public void init(MainClass mc) {
		if (!mc.getConfig().getBoolean("modules.Advancements.enabled")) return;

		manager = CrazyAdvancements.getNewAdvancementManager();

		AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.COMMAND_BLOCK, "Gamemode 4", "Vanilla Re-Imagined", AdvancementFrame.TASK, false, false, AdvancementVisibility.ALWAYS);
		rootDisplay.setBackgroundTexture("textures/block/cyan_concrete.png");
		Advancement gm4 = new Advancement(null, new NameKey("gm4", "root"), rootDisplay);

		manager.addAdvancement(gm4);

		addAdvancement(gm4, "natural_defences", "Natural Defenses", "Get blinded by an underwater squid.", Material.INK_SAC, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, 0f);
		addAdvancement(gm4, "ender_aid", "Ender Aid", "Come in contact with a support Enderman.", Material.ENDER_EYE, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, 1f);
		addAdvancement(gm4, "risen", "Risen", "Die and raise up an undead zombie in your body's place.", Material.TOTEM_OF_UNDYING, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, 2f);

		addAdvancement(gm4, "all_my_audreys", "All my Audreys", "Find all Dearest Audrey messages in a bottle.", Material.GLASS_BOTTLE, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, -1f);
		addAdvancement(gm4, "a_fun_gi", "A Fun-gi", "Decor some decorative mushroom.", Material.RED_MUSHROOM_BLOCK, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, -2f);

		Advancement cc = getParentAdvancement(gm4, "clever_crafting", "Clever Crafting", "Build yourself a Custom Crafter with droppers and a crafting table.", Material.CRAFTING_TABLE, AdvancementFrame.CHALLENGE, true, true, AdvancementVisibility.ALWAYS, 1f, -3f);

		addAdvancement(cc, "clever_smelting", "Clever Smelting", "Create your own Blast Furnace.", Material.FURNACE, AdvancementFrame.GOAL, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, -3f);

		for (Player p : Bukkit.getOnlinePlayers()) {
			manager.addPlayer(p);
			manager.grantAdvancement(p, gm4);
			manager.loadProgress(p, "gm4");
			manager.saveProgress(p, "gm4");
		}

		// OLD
//		addAdvancementToParent("gm4/clever_decrafting", "Clever Decrafting", "Assemble a Disassembler.", "minecraft:dropper",
//				Trigger.builder(Trigger.TriggerType.IMPOSSIBLE, "default"), true, false, true, FrameType.GOAL, cc, worldName);
//
//		addAdvancementToParent("gm4/clever_crushing", "Clever Crushing", "Make yourself a new Block Compressor using a gm4 Crafter.", "minecraft:piston",
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

	public void addAdvancement(Advancement parent, String key, String title, String description, Material icon, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility, float x, float y) {
		AdvancementDisplay display = new AdvancementDisplay(icon, title, description, frame, showToast, announceChat, visibility);
		display.setCoordinates(x, y);
		Advancement child = new Advancement(parent, new NameKey("gm4", key), display);
		manager.addAdvancement(child);
	}

	public Advancement getParentAdvancement(Advancement parent, String key, String title, String description, Material icon, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility, float x, float y) {
		AdvancementDisplay display = new AdvancementDisplay(icon, title, description, frame, showToast, announceChat, visibility);
		display.setCoordinates(x, y);
		Advancement child = new Advancement(parent, new NameKey("gm4", key), display);
		manager.addAdvancement(child);
		return child;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		manager.addPlayer(e.getPlayer());
		manager.loadProgress(e.getPlayer(), "gm4");
		grantAdvancement("root", e.getPlayer());
		Bukkit.broadcastMessage("Welcome " + e.getPlayer().getName());
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		manager.addPlayer(e.getPlayer());
		grantAdvancement("natural_defences", e.getPlayer());
	}

	public static void grantAdvancement(String advName, Player p) {
		Advancement adv = manager.getAdvancement(new NameKey("gm4", advName));
		manager.grantAdvancement(p, adv);
		manager.saveProgress(p, "gm4");
	}
}





