package com.belka.spigot.gm4.modules;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.crafting.CustomItems;
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
import org.bukkit.inventory.ItemStack;

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

		Advancement cc = getParentAdvancement(gm4, "clever_crafting", "Clever Crafting", "Build yourself a Custom Crafter with droppers and a crafting table.", Material.CRAFTING_TABLE, AdvancementFrame.CHALLENGE, true, true, AdvancementVisibility.ALWAYS, 1f, 0f);
		addAdvancement(cc, "clever_decrafting", "Clever Decrafting", "Assemble a Disassembler.", Material.DROPPER, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, -1f);
		addAdvancement(cc, "clever_smelting", "Clever Smelting", "Create your own Blast Furnace.", Material.FURNACE, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, 0f);
		addAdvancement(cc, "clever_crushing", "Clever Crushing", "Make yourself a new Block Compressor using a Custom Crafter.", Material.PISTON, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, 1f);
		addAdvancement(cc, "green_feet", "Green Feet", "Wear the Boots of Ostara.", Material.LEATHER_BOOTS, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, 2f);

		Advancement hc = getParentAdvancement(cc, "cant_buy_me_love", "Can't buy me love", "Craft a Tier 1 Heart Canister.", CustomItems.HEART_CANISTER_TIER_1(1), AdvancementFrame.GOAL, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, -2f);
		Advancement hc2 = getParentAdvancement(hc, "love_is_all_you_need", "Love is all you need", "Craft a Tier 2 Heart Canister.", CustomItems.HEART_CANISTER_TIER_2(1), AdvancementFrame.GOAL, true, true, AdvancementVisibility.PARENT_GRANTED, 3f, -2f);
		addAdvancement(hc2, "all_my_loving", "All My Loving.", "Max out your Heart Canister health.", CustomItems.HEART_CANISTER_TIER_2(1), AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 4f, -2f);

		Advancement mobs = getParentAdvancement(gm4, "mobs", "Mobs", "", Material.IRON_SWORD, AdvancementFrame.TASK, false, false, AdvancementVisibility.ALWAYS, 2f, 3f);
		addAdvancement(mobs, "natural_defences", "Natural Defenses", "Get blinded by an underwater squid.", Material.INK_SAC, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 3f, 0.5f);
		addAdvancement(mobs, "ender_aid", "Ender Aid", "Come in contact with a support Enderman.", Material.ENDER_EYE, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 3f, 1.5f);
		addAdvancement(mobs, "risen", "Risen", "Die and raise up an undead zombie in your body's place.", Material.TOTEM_OF_UNDYING, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 3f, 2.5f);
		addAdvancement(mobs, "batboozled", "Batboozled", "Get blown up by a Bat Grenade.", Material.TNT, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 3f, 3.5f);
		addAdvancement(mobs, "dr_frankenstein", "Dr. Frankenstein", "It's alive! Create a Shulker.", Material.SHULKER_BOX, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 3f, 4.5f);
		addAdvancement(mobs, "instant_regret", "Instant Regret", "Wither-ize a skeleton with potion swords.", Material.WITHER_SKELETON_SKULL, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 3f, 5.5f);

//		addAdvancement(gm4, "all_my_audreys", "All my Audreys", "Find all Dearest Audrey messages in a bottle.", Material.GLASS_BOTTLE, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, -1f);
//		addAdvancement(gm4, "a_fun_gi", "A Fun-gi", "Decor some decorative mushroom.", Material.RED_MUSHROOM_BLOCK, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, -2f);

		for (Player p : Bukkit.getOnlinePlayers()) {
			manager.addPlayer(p);
			manager.grantAdvancement(p, gm4);
			manager.loadProgress(p, "gm4");
			manager.saveProgress(p, "gm4");
		}

		// OLD
//		// HEART CANISTER
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
	public void addAdvancement(Advancement parent, String key, String title, String description, ItemStack icon, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility, float x, float y) {
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
	public Advancement getParentAdvancement(Advancement parent, String key, String title, String description, ItemStack icon, AdvancementFrame frame, boolean showToast, boolean announceChat, AdvancementVisibility visibility, float x, float y) {
		AdvancementDisplay display = new AdvancementDisplay(icon, title, description, frame, showToast, announceChat, visibility);
		display.setCoordinates(x, y);
		Advancement child = new Advancement(parent, new NameKey("gm4", key), display);
		manager.addAdvancement(child);
		return child;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		manager.addPlayer(p);
		manager.loadProgress(p, "gm4");
		grantAdvancement("root", p);
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





