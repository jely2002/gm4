package com.belka.spigot.gm4;

import api.*;
import api.lootTables.Entry;
import api.lootTables.Function;
import api.lootTables.LootTable;
import api.lootTables.Pool;
import api.services.Metrics;
import api.services.Updater;
import com.belka.spigot.gm4.config.ConfigManager;
import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.crafting.CraftingBook;
import com.belka.spigot.gm4.crafting.CustomCrafter;
import com.belka.spigot.gm4.crafting.CustomRecipes;
import com.belka.spigot.gm4.crafting.RecipeHandler;
import com.belka.spigot.gm4.customTerrain.CoolerCaves;
import com.belka.spigot.gm4.customTerrain.CustomTerrain;
import com.belka.spigot.gm4.customTerrain.DangerousDungeons;
import com.belka.spigot.gm4.customTerrain.TowerStructures;
import com.belka.spigot.gm4.interfaces.Module;
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

	private ArrayList<Module> modules = new ArrayList<>();

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
		cmdMgmt = new CommandManager(this, gui);
		getCommand("gamemode4").setExecutor(cmdMgmt);
		storage = new ConfigManager(this);
		//Custom Events
		getServer().getPluginManager().registerEvents(new ArmorListener(getStorage().data().getStringList("ArmorEvents.blocked")), this);
		getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
		//Services
		Updater updater = new Updater();
		InventoryCreator inventoryCreator = new InventoryCreator(this);
		//Custom Crafting
		CustomBlock customBlock = new CustomBlock(this);
		RecipeHandler recipeHandler = new RecipeHandler(this);
		CustomCrafter customCrafter = new CustomCrafter(this, recipeHandler);
		CustomRecipes customRecipes = new CustomRecipes(this);
		CraftingBook craftingBook = new CraftingBook(this, inventoryCreator);
		//With Craftable Items
		HeartCanisters heartCanisters = new HeartCanisters(this);
		BlastFurnaces blastFurnaces = new BlastFurnaces(this);
		LightningRods lightningRods = new LightningRods(this);
		TrappedSigns trappedSigns = new TrappedSigns(this);
		SoulGlass soulGlass = new SoulGlass();
		//Custom Terrain
		LootTable lootTable = new LootTable();
		customTerrain = new CustomTerrain(this, lootTable);
		CoolerCaves coolerCaves = new CoolerCaves(this, customTerrain);
		DangerousDungeons dangerousDungeons = new DangerousDungeons(this);
		TowerStructures towerStructures = new TowerStructures(this);
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
		NoteBlockInterface noteBlockInterface = new NoteBlockInterface();
		PigTractors pigTractors = new PigTractors(this);
		PodzolRootingSoil podzolRootingSoil = new PodzolRootingSoil(this);
		PotionSwords potionSwords = new PotionSwords(this);
		SoulProbes soulProbes = new SoulProbes(this, inventoryCreator);
		SpawnerMinecarts spawnerMinecarts = new SpawnerMinecarts(this);
		SpeedPaths speedPaths = new SpeedPaths(this);
		SunkenTreasure sunkenTreasure = new SunkenTreasure(this);
		UndeadPlayers undeadPlayers = new UndeadPlayers(this);
		WeightedArmour weightedArmour = new WeightedArmour(this);
		XPStorage xpStorage = new XPStorage(this);
		ZauberCauldrons zauberCauldrons = new ZauberCauldrons(this);
		Ziprails ziprails = new Ziprails(this);
		//Register classes and used interfaces
		registerClasses(
				storage,
				cmdMgmt,
				gui,
				updater,

				customBlock,
				recipeHandler,
				customCrafter,
				customRecipes,
				craftingBook,
				heartCanisters,
				blastFurnaces,
				lightningRods,
				trappedSigns,
				soulGlass,

				customTerrain,
				coolerCaves,
				dangerousDungeons,
				towerStructures,

				advancements,
				batGrenades,
				betterArmorStands,
				betterFire,
				chairs,
				desireLines,
				enderHoppers,
				endermanSupportClass,
				inkSpittingSquid,
				noteBlockInterface,
				pigTractors,
				podzolRootingSoil,
				potionSwords,
				soulProbes,
				spawnerMinecarts,
				speedPaths,
				sunkenTreasure,
				undeadPlayers,
				weightedArmour,
				xpStorage,
				zauberCauldrons,
				ziprails
				);
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
		getLogger().log(Level.INFO, ConsoleColor.RED + ConsoleColor.BOLD + "Gamemode 4 has been disabled!" + ConsoleColor.RESET);
	}

	private void registerClasses(Object... classes) {
		int listenersRegistered = 0;
		int modulesRegistered = 0;
		for(Object o : classes) {
			if(o instanceof Listener) {
				getServer().getPluginManager().registerEvents((Listener) o, this);
				listenersRegistered++;
			}
			if(o instanceof Module) {
				Module module = (Module) o;
				module.init(this);
				if (module.getSetting() != null) {
					String className = o.toString().replace("com.belka.spigot.gm4.", "").split("\\.")[1].split("@")[0];
					if (!getStorage().data().contains("settings." + className)) {
						getStorage().data().set("settings." + className + ".name", module.getSetting().getName());
						getStorage().data().set("settings." + className + ".type", module.getSetting().getIcon().name());
						getStorage().data().set("settings." + className + ".path", module.getSetting().getPath());
						getStorage().saveData();
					}
				}
				modules.add(module);
				modulesRegistered++;
			}
		}
		if(getConfig().getBoolean("internal.verbosemode")) {
			getLogger().log(Level.INFO, "Registered " + modulesRegistered + " commands.");
			getLogger().log(Level.INFO, "Registered " + listenersRegistered + " listeners.");
		}
	}

	public ConfigManager getStorage() {
		return storage;
	}

	public ArrayList<Module> getModules() { return modules; }

	private void serializeConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		ConfigurationSerialization.registerClass(LootTable.class, "LootTable");
		ConfigurationSerialization.registerClass(Pool.class, "Pool");
		ConfigurationSerialization.registerClass(Entry.class, "Entry");
		ConfigurationSerialization.registerClass(Function.class, "Function");
		ConfigurationSerialization.registerClass(CustomBlock.class, "CustomBlock");
	}

	private void initializeMetrics() {
		Metrics metrics = new Metrics(this,4251);
	}

	//Public Helper methods that need access to the JavaPlugin object
	public File getResourceAsFile(String resource) throws IOException {
		InputStream inputStream = getResource(resource);
		File tmpFile = File.createTempFile("file", "temp");
		FileUtils.copyInputStreamToFile(inputStream, tmpFile);
		return tmpFile;
	}

}
