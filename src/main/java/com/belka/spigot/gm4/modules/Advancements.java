package com.belka.spigot.gm4.modules;

import api.SkullCreator;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.Initializable;
import eu.endercentral.crazy_advancements.*;
import eu.endercentral.crazy_advancements.manager.AdvancementManager;
import eu.endercentral.crazy_advancements.AdvancementDisplay.AdvancementFrame;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class Advancements implements Listener, Initializable {

    private MainClass mc;
    private static AdvancementManager manager;

    public Advancements(MainClass mc) {
        this.mc = mc;
    }

    public void init(MainClass mc) {
        manager = CrazyAdvancements.getNewAdvancementManager();
//        if (manager.getAdvancements("gm4").size() > 0 || !mc.getConfig().getBoolean("modules.Advancements.enabled")) return;

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.COMMAND_BLOCK, "Gamemode 4", "Vanilla Re-Imagined", AdvancementFrame.TASK, false, false, AdvancementVisibility.ALWAYS);
        rootDisplay.setBackgroundTexture("textures/block/cyan_concrete.png");
        Advancement gm4 = new Advancement(null, new NameKey("gm4", "root"), rootDisplay);

        manager.addAdvancement(gm4);

        Advancement cc = getParentAdvancement(gm4, "clever_crafting", "Clever Crafting", "Build yourself a Custom Crafter with droppers and a crafting table", Material.CRAFTING_TABLE, AdvancementFrame.CHALLENGE, true, true, AdvancementVisibility.ALWAYS, 1.5f, 0f);
        addAdvancement(cc, "clever_decrafting", "Clever Decrafting", "Assemble a Disassembler", Material.DROPPER, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2.5f, -1f);
        addAdvancement(cc, "clever_smelting", "Clever Smelting", "Create your own Blast Furnace", Material.FURNACE, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2.5f, 0f);
        addAdvancement(cc, "clever_crushing", "Clever Crushing", "Make yourself a new Block Compressor using a Custom Crafter", Material.PISTON, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2.5f, 1f);
        addAdvancement(cc, "green_feet", "Green Feet", "Wear the Boots of Ostara", CustomItems.BOOTS_OF_OSTARA(1), AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2.5f, 2f);

        Advancement hc = getParentAdvancement(cc, "cant_buy_me_love", "Can't buy me love", "Craft a Tier 1 Heart Canister", CustomItems.HEART_CANISTER_TIER_1(1), AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2.5f, -2f);
        Advancement hc2 = getParentAdvancement(hc, "love_is_all_you_need", "Love is all you need", "Craft a Tier 2 Heart Canister", CustomItems.HEART_CANISTER_TIER_2(1), AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 3.5f, -2f);
        addAdvancement(hc2, "all_my_loving", "All My Loving", "Max out your Heart Canister health", CustomItems.HEART_CANISTER_TIER_2(1), AdvancementFrame.CHALLENGE, true, true, AdvancementVisibility.PARENT_GRANTED, 4.5f, -2f);

        Advancement mobs = getParentAdvancement(gm4, "mobs", "Mobs", "", Material.IRON_SWORD, AdvancementFrame.TASK, false, false, AdvancementVisibility.ALWAYS, 3f, 3f);
        addAdvancement(mobs, "oink_tractors", "Oink Tractors", "Ride a pig with a hoe to make it a functional tractor!", Material.SADDLE, AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 4f, -0.5f);
        addAdvancement(mobs, "natural_defences", "Natural Defenses", "Get blinded by an underwater squid", Material.INK_SAC, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 4f, 0.5f);
        addAdvancement(mobs, "ender_aid", "Ender Aid", "Come in contact with a support Enderman", Material.ENDER_PEARL, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 4f, 1.5f);
        addAdvancement(mobs, "risen", "Risen", "Die and raise up an undead zombie in your body's place", Material.TOTEM_OF_UNDYING, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 4f, 2.5f);
        addAdvancement(mobs, "batboozled", "Batboozled", "Get blown up by a Bat Grenade", Material.TNT, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 4f, 3.5f);
        addAdvancement(mobs, "dr_frankenstein", "Dr. Frankenstein", "It's alive! Create a Shulker", Material.SHULKER_BOX, AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 4f, 4.5f);
        addAdvancement(mobs, "instant_regret", "Instant Regret", "Wither-ize a skeleton with potion swords", Material.WITHER_SKELETON_SKULL, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 4f, 5.5f);

        Advancement as = getParentAdvancement(gm4, "gettin_handsy", "Gettin' Handsy", "Give an armor stand arms", Material.ARMOR_STAND, AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 1f, 4f);
        addAdvancement(as, "plenty_o_posing", "Plenty o' Posing", "Make your armor stand strike a pose", Material.ARMOR_STAND, AdvancementFrame.GOAL, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, 4f);

        Advancement zc = getParentAdvancement(gm4, "potion_chef", "Potion Chef", "Create a Zauber Cauldron", Material.CAULDRON, AdvancementFrame.GOAL, true, true, AdvancementVisibility.ALWAYS, 2.5f, -3f);
        addAdvancement(zc, "underrated_materials", "Underrated Materials", "Suit up with a full suit of Zauber Armor.", Material.GOLDEN_CHESTPLATE, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 3.5f, -3f);
		addAdvancement(zc, "soup_kitchen_magician", "Soup Kitchen Magician", "Where did my lunch go?", Material.MUSHROOM_STEW, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 3.5f, -4f);
		addAdvancement(zc, "questionable_ingredients", "Questionable Ingredients", "Just like the Rabbits!", Material.CHORUS_FRUIT, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 3.5f, -5f);

		Advancement lt = getParentAdvancement(gm4, "oh_that_kind_of_tank", "Oh, That Kind of Tank", "Find out a liquid tank is not an awesome submarine", Material.HOPPER, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, -4f);
		ItemStack potion = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		meta.setBasePotionData(new PotionData(PotionType.JUMP));
		potion.setItemMeta(meta);
		addAdvancement(lt, "fizzy_lifting_drink", "Fizzy Lifting Drink", "Come on in, the air's fine.", potion, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, -4f);
		addAdvancement(lt, "experience_quicksave", "Experience Quicksave", "Bottle your own XP from a liquid tank.", Material.EXPERIENCE_BOTTLE, AdvancementFrame.TASK, true, true, AdvancementVisibility.PARENT_GRANTED, 2f, -5f);

        addAdvancement(gm4, "up_and_away", "Up, Up and Away!", "Ride a minecart going up a vertical rail", Material.LADDER, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, -2f);
        addAdvancement(gm4, "wheeeeeeeee", "Wheeeeeeeee", "Fly through the sky on a ziprail", Material.MINECART, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, -1f);
        addAdvancement(gm4, "all_my_audreys", "All my Audreys", "Find all Dearest Audrey messages in a bottle", Material.GLASS_BOTTLE, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, 1f);
		addAdvancement(gm4, "a_fun_gi", "A Fun-gi", "Decor some decorative mushroom", Material.RED_MUSHROOM_BLOCK, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, 2f);
        ItemStack phantomHead = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U5NTE1M2VjMjMyODRiMjgzZjAwZDE5ZDI5NzU2ZjI0NDMxM2EwNjFiNzBhYzAzYjk3ZDIzNmVlNTdiZDk4MiJ9fX0=");
        addAdvancement(gm4, "not_so_smart_defenses", "Not so smart defenses", "Get hit by a Phantom Scarecrow", phantomHead, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, 5f);
		addAdvancement(gm4, "dont_go_breaking_my_cart", "Don't go breaking my cart", "Capture a Monster Spawner", Material.SPAWNER, AdvancementFrame.TASK, true, true, AdvancementVisibility.ALWAYS, 1f, 6f);

        for (Player p : Bukkit.getOnlinePlayers()) {
            manager.addPlayer(p);
            manager.setAnnounceAdvancementMessages(false);
            manager.grantAdvancement(p, gm4);
            manager.loadProgress(p, "gm4");
            manager.saveProgress(p, "gm4");
            manager.setAnnounceAdvancementMessages(true);
        }
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
		manager.setAnnounceAdvancementMessages(false);
		manager.grantAdvancement(p, manager.getAdvancement(new NameKey("gm4", "root")));
		manager.loadProgress(p, "gm4");
		manager.saveProgress(p, "gm4");
		manager.setAnnounceAdvancementMessages(true);
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