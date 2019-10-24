package com.belka.spigot.gm4;

import api.InventoryCreator;
import api.LootTables.Entry;
import api.LootTables.Function;
import api.LootTables.Pool;
import com.belka.spigot.gm4.config.ConfigManager;
import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.crafting.CustomCrafter;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.crafting.CustomRecipes;
import com.belka.spigot.gm4.crafting.RecipeHandler;
import com.belka.spigot.gm4.customTerrain.CoolerCaves;
import com.belka.spigot.gm4.customTerrain.CustomTerrain;
import com.belka.spigot.gm4.customTerrain.DangerousDungeons;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.modules.*;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class MainClass extends JavaPlugin {

	private CommandManager cmdMgmt;
	private ConfigManager storage;

	public SpeedPaths speedPaths;
	private CustomTerrain customTerrain;
	private CoolerCaves coolerCaves;
	private DangerousDungeons dangerousDungeons;

	public String chatPrefix = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "GM4" + ChatColor.WHITE + "] " + ChatColor.RESET;
	public String consolePrefix = ConsoleColor.BLACK + "[" + ConsoleColor.CYAN + "GM4" + ConsoleColor.BLACK + "] " + ConsoleColor.RESET;

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println("Oo---------------oO " + ConsoleColor.CYAN + ConsoleColor.BOLD + "Gamemode 4" + ConsoleColor.RESET + " Oo---------------oO");
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Version " + pdfFile.getVersion() + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Author  MaximumFX & jely2002" + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Status  Enabled" + ConsoleColor.RESET);
		System.out.println("Oo-----------------------oOo-----------------------oO");

		getConfig().options().copyDefaults(true);
		saveConfig();

		SettingsGUI gui = new SettingsGUI(this);
		CustomItems customItems = new CustomItems();

		cmdMgmt = new CommandManager(this, gui);
		storage = new ConfigManager(this);
		MainCommands mCmds = new MainCommands(this, customItems);

		Stats stats = new Stats();
		InventoryCreator inventoryCreator = new InventoryCreator(this);

//		Custom Crafting
		RecipeHandler recipeHandler = new RecipeHandler(this);
		CustomCrafter customCrafter = new CustomCrafter(this, recipeHandler);
		CustomRecipes customRecipes = new CustomRecipes(this);
//      With Craftable Items
		HeartCanisters heartCanisters = new HeartCanisters(this);
		BlastFurnaces blastFurnaces = new BlastFurnaces(this);
		LightningRods lightningRods = new LightningRods(this);
		TrappedSigns trappedSigns = new TrappedSigns(this);

//		Custom Terrain
		customTerrain = new CustomTerrain(this);
		coolerCaves = new CoolerCaves(this, customTerrain);
		dangerousDungeons = new DangerousDungeons(this, customTerrain);

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
		SoulProbes soulProbes = new SoulProbes(this, inventoryCreator);
		SpawnerMinecarts spawnerMinecarts = new SpawnerMinecarts(this);
		speedPaths = new SpeedPaths(this);
		WeightedArmour weightedArmour = new WeightedArmour(this);
		XPStorage xpStorage = new XPStorage(this);
		ZauberCauldrons zauberCauldrons = new ZauberCauldrons(this);

		registerClasses(this,
				storage,
				cmdMgmt,
				mCmds,
				gui,
				stats,

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
				soulProbes,
				spawnerMinecarts,
				speedPaths,
				weightedArmour,
				xpStorage,
				zauberCauldrons);
		ConfigurationSerialization.registerClass(Pool.class, "Pool");
		ConfigurationSerialization.registerClass(Entry.class, "Entry");
		ConfigurationSerialization.registerClass(Function.class, "Function");
	}

	@Override
	public void onDisable() {
		customTerrain.disable();
		saveConfig();
		storage().saveAll();

		for (Player p : Bukkit.getOnlinePlayers()) {
			Advancements.manager.saveProgress(p, "gm4");
		}
//		Advancements.manager.setAnnounceAdvancementMessages(true);
		System.out.println(ConsoleColor.RED + ConsoleColor.BOLD + "Gamemode 4 has been disabled!" + ConsoleColor.RESET);
	}

	public ConfigManager storage() {
		return storage;
	}

	public CoolerCaves coolerCaves() {
		return coolerCaves;
	}
	public DangerousDungeons dangerousDungeons() {
		return dangerousDungeons;
	}

	public File getResourceAsFile(String resource) throws IOException {
		InputStream inputStream = getResource(resource);
		File tmpFile = File.createTempFile("file", "temp");
		FileUtils.copyInputStreamToFile(inputStream, tmpFile); // FileUtils from apache-io
		return tmpFile;
	}

	private void registerClasses(Object... classes) {
		int listenersRegistered = 0;
		int commandsRegistered = 0;
		int subcommandsRegistered = 0;
		int classesInitialized = 0;
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
		}
		if(getConfig().getBoolean("internal.verbosemode")) {
			getLogger().log(Level.INFO, "Registered " + commandsRegistered + " commands.");
			getLogger().log(Level.INFO, "Registered " + subcommandsRegistered + " subcommands.");
			getLogger().log(Level.INFO, "Registered " + listenersRegistered + " listeners.");
			getLogger().log(Level.INFO, "Initialized  " + classesInitialized + " classes.");
		}
	}
}
