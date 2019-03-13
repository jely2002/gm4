package com.belka.spigot.gm4;

import com.belka.spigot.gm4.config.ConfigManager;
import com.belka.spigot.gm4.crafting.CustomCrafter;
import com.belka.spigot.gm4.crafting.RecipeHandler;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MainClass extends JavaPlugin implements Listener{

    private CommandManager cmdMgmt;
    private ConfigManager storage;
    private MainCommands mCmds;

    @Override
    public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println("Oo---------------oO " + ConsoleColor.CYAN + ConsoleColor.BOLD + "Gamemode 4" + ConsoleColor.RESET + " Oo---------------oO");
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Version  V" + pdfFile.getVersion() + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Author  MaximumFX & jely2002" + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Status  Enabled" + ConsoleColor.RESET);
		System.out.println("Oo-----------------------oOo-----------------------oO");

		getConfig().options().copyDefaults(true);
		saveConfig();

        cmdMgmt = new CommandManager(this);
        storage = new ConfigManager(this);
        mCmds = new MainCommands(this);

        SpeedPaths speedPaths = new SpeedPaths(this);
		RecipeHandler recipeHandler = new RecipeHandler();
		CustomCrafter customCrafter = new CustomCrafter(this, recipeHandler);
		HeartCanisters heartCanisters = new HeartCanisters(this);
		DesireLines desireLines = new DesireLines(this);
		Advancements advancements = new Advancements(this);
		BatGrenades batGrenades = new BatGrenades(this);
		BetterArmorStands betterArmorStands = new BetterArmorStands(this);
		BetterFire betterFire = new BetterFire(this);
		EndermanSupportClass endermanSupportClass = new EndermanSupportClass(this);
		InkSpittingSquid inkSpittingSquid = new InkSpittingSquid(this);

        registerClasses(this, storage, cmdMgmt, mCmds, speedPaths, customCrafter, heartCanisters, desireLines, advancements, batGrenades, betterArmorStands, betterFire, endermanSupportClass, inkSpittingSquid);
    }

    @Override
    public void onDisable() {
		System.out.println(ConsoleColor.RED + ConsoleColor.BOLD + "Gamemode 4 has been disabled!" + ConsoleColor.RESET);
		saveConfig();
    	storage().saveAll();
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Advancements.grantAdvancement("custom", e.getPlayer());
		Bukkit.broadcastMessage("Welcome " + e.getPlayer().getName());
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
