package com.belka.spigot.gm4;

import api.InventoryCreator;
import com.belka.spigot.gm4.config.ConfigManager;
import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.crafting.CustomCrafter;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.crafting.CustomRecipes;
import com.belka.spigot.gm4.crafting.RecipeHandler;
import com.belka.spigot.gm4.customTerrain.CoolerCaves;
import com.belka.spigot.gm4.customTerrain.CustomTerrain;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MainClass extends JavaPlugin {

	private CommandManager cmdMgmt;
	private ConfigManager storage;
	private MainCommands mCmds;

	public SpeedPaths speedPaths;

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
		mCmds = new MainCommands(this, customItems);

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
		CustomTerrain customTerrain = new CustomTerrain(this);
		CoolerCaves coolerCaves = new CoolerCaves(this);

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
	}

	@Override
	public void onDisable() {
		System.out.println(ConsoleColor.RED + ConsoleColor.BOLD + "Gamemode 4 has been disabled!" + ConsoleColor.RESET);
		saveConfig();
		storage().saveAll();

		for (Player p : Bukkit.getOnlinePlayers()) {
			Advancements.manager.saveProgress(p, "gm4");
		}
//		Advancements.manager.setAnnounceAdvancementMessages(true);
	}

	public ConfigManager storage() {
		return storage;
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
