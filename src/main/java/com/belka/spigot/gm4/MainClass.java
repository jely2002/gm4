package com.belka.spigot.gm4;

import api.ConsoleColor;
import api.InventoryCreator;
import api.lootTables.Entry;
import api.lootTables.Function;
import api.lootTables.LootTable;
import api.lootTables.Pool;
import api.services.Metrics;
import api.services.Updater;
import com.belka.spigot.gm4.config.ConfigManager;
import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.crafting.CustomCrafter;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.crafting.CustomRecipes;
import com.belka.spigot.gm4.crafting.RecipeHandler;
import com.belka.spigot.gm4.customTerrain.CustomTerrain;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.interfaces.Reloadable;
import com.belka.spigot.gm4.modules.*;
import eu.endercentral.crazy_advancements.CrazyAdvancements;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;

public class MainClass extends JavaPlugin {

	private CommandManager cmdMgmt;
	private CustomTerrain customTerrain;
	private ConfigManager storage;

	private ArrayList<Reloadable> reloadableClasses;

	public SpeedPaths speedPaths;
	public CrazyAdvancements advancementsAPI;

	public String chatPrefix = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "GM4" + ChatColor.WHITE + "] " + ChatColor.RESET;
	public String consolePrefix = ConsoleColor.BLACK + "[" + ConsoleColor.CYAN + "GM4" + ConsoleColor.BLACK + "] " + ConsoleColor.RESET;

	@Override
	public void onEnable() {
		System.out.println("Oo---------------oO " + ConsoleColor.CYAN + ConsoleColor.BOLD + "Gamemode 4" + ConsoleColor.RESET + " Oo---------------oO");
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Version " + getDescription().getVersion() + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Author  MaximumFX & jely2002" + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Status  Enabled" + ConsoleColor.RESET);
		System.out.println("Oo-----------------------oOo-----------------------oO");

		serializeConfig();
		initializeMetrics();

		//Initialize advancement API
		advancementsAPI = new CrazyAdvancements(this);
		//Internals
		SettingsGUI gui = new SettingsGUI(this);
		CustomItems customItems = new CustomItems();
		cmdMgmt = new CommandManager(this, gui);
		storage = new ConfigManager(this);
		MainCommands mCmds = new MainCommands(this, customItems);
		//Services
		Updater updater = new Updater();
		InventoryCreator inventoryCreator = new InventoryCreator(this);
		//Custom Crafting
		RecipeHandler recipeHandler = new RecipeHandler(this);
		CustomCrafter customCrafter = new CustomCrafter(this, recipeHandler);
		CustomRecipes customRecipes = new CustomRecipes(this);
		//With Craftable Items
		HeartCanisters heartCanisters = new HeartCanisters(this);
		BlastFurnaces blastFurnaces = new BlastFurnaces(this);
		LightningRods lightningRods = new LightningRods(this);
		TrappedSigns trappedSigns = new TrappedSigns(this);
		//Custom Terrain
		LootTable lootTable = new LootTable();
		customTerrain = new CustomTerrain(this, lootTable);
		//Modules
		Advancements advancements = new Advancements(this);
		BatGrenades batGrenades = new BatGrenades(this);
		BetterArmorStands betterArmorStands = new BetterArmorStands(this);
		BetterFire betterFire = new BetterFire(this);
		Chairs chairs = new Chairs(this);
		DesireLines desireLines = new DesireLines(this);
		EnderHoppers enderHoppers = new EnderHoppers(this);
		EndermanSupportClass endermanSupportClass = new EndermanSupportClass();
		InkSpittingSquid inkSpittingSquid = new InkSpittingSquid(this);
		PigTractors pigTractors = new PigTractors(this);
		PotionSwords potionSwords = new PotionSwords(this);
		SoulProbes soulProbes = new SoulProbes(this, inventoryCreator);
		SpawnerMinecarts spawnerMinecarts = new SpawnerMinecarts(this);
		speedPaths = new SpeedPaths(this);
		WeightedArmour weightedArmour = new WeightedArmour(this);
		XPStorage xpStorage = new XPStorage(this);
		ZauberCauldrons zauberCauldrons = new ZauberCauldrons(this);
		//Register classes and used interfaces
		registerClasses(this,
				storage,
				cmdMgmt,
				mCmds,
				gui,
				updater,

				recipeHandler,
				customCrafter,
				customRecipes,
				heartCanisters,
				blastFurnaces,
				lightningRods,
				trappedSigns,

				customTerrain,

				advancements,
				batGrenades,
				betterArmorStands,
				betterFire,
				chairs,
				desireLines,
				enderHoppers,
				endermanSupportClass,
				inkSpittingSquid,
				pigTractors,
				potionSwords,
				soulProbes,
				spawnerMinecarts,
				speedPaths,
				weightedArmour,
				xpStorage,
				zauberCauldrons);
	}

	@Override
	public void onDisable() {
		if(customTerrain != null) {
			customTerrain.disable();
		}
		if(storage != null) {
			saveConfig();
			storage.saveAll();
		}
		if(Advancements.manager != null) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Advancements.manager.saveProgress(p, "gm4");
			}
			advancementsAPI.disable();
		} else {
			getLogger().log(Level.SEVERE, "AD Manager is null");
		}
//		Advancements.manager.setAnnounceAdvancementMessages(true);
		System.out.println(ConsoleColor.RED + ConsoleColor.BOLD + "Gamemode 4 has been disabled!" + ConsoleColor.RESET);
	}

	private void registerClasses(Object... classes) {
		int listenersRegistered = 0;
		int commandsRegistered = 0;
		int subcommandsRegistered = 0;
		int classesInitialized = 0;
		int reloadableClassesInitialized = 0;
		for(Object o : classes) {
			if(o instanceof Listener) {
				getServer().getPluginManager().registerEvents((Listener) o, this);
				listenersRegistered++;
			}
			if(o instanceof PluginCommand) {
				PluginCommand plcmd = (PluginCommand) o;
				for(String arg : plcmd.getCommands()) {
					getCommand(arg).setExecutor(plcmd);
					commandsRegistered++;
				}
			}
			if(o instanceof PluginSubcommand) {
				PluginSubcommand plscmd = (PluginSubcommand) o;
				for(String arg : plscmd.getSubcommand()) {
					cmdMgmt.addCommand(arg, plscmd);
					subcommandsRegistered++;
				}
			}
			if(o instanceof Initializable) {
				Initializable initClass = (Initializable) o;
				initClass.init(this);
				classesInitialized++;
			}
			if(o instanceof Reloadable) {
				Reloadable reloadClass = (Reloadable) o;
				reloadableClasses.add(reloadClass);
				reloadableClassesInitialized++;
			}
		}
		if(getConfig().getBoolean("internal.verbosemode")) {
			getLogger().log(Level.INFO, "Registered " + commandsRegistered + " commands.");
			getLogger().log(Level.INFO, "Registered " + subcommandsRegistered + " subcommands.");
			getLogger().log(Level.INFO, "Registered " + listenersRegistered + " listeners.");
			getLogger().log(Level.INFO, "Registered  " + reloadableClassesInitialized + " reloadable classes.");
			getLogger().log(Level.INFO, "Initialized  " + classesInitialized + " classes.");
		}
	}

	public ConfigManager getStorage() {
		return storage;
	}
	public ArrayList<Reloadable> getReloadableClasses() { return reloadableClasses; }

	private void serializeConfig(){
		getConfig().options().copyDefaults(true);
		saveConfig();
		ConfigurationSerialization.registerClass(LootTable.class, "LootTable");
		ConfigurationSerialization.registerClass(Pool.class, "Pool");
		ConfigurationSerialization.registerClass(Entry.class, "Entry");
		ConfigurationSerialization.registerClass(Function.class, "Function");
	}

	private void initializeMetrics() {
		Metrics metrics = new Metrics(this,4251);
	}

	//Public Helper methods that need acces to the JavaPlugin object
	public File getResourceAsFile(String resource) throws IOException {
		InputStream inputStream = getResource(resource);
		File tmpFile = File.createTempFile("file", "temp");
		FileUtils.copyInputStreamToFile(inputStream, tmpFile);
		return tmpFile;
	}

}
