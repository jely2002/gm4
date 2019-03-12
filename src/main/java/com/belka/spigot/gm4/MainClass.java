package com.belka.spigot.gm4;

import com.belka.spigot.gm4.config.ConfigManager;
import com.belka.spigot.gm4.crafting.CustomCrafter;
import com.belka.spigot.gm4.crafting.RecipeHandler;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.modules.Advancements;
import com.belka.spigot.gm4.modules.ConsoleColor;
import com.belka.spigot.gm4.modules.HeartCanisters;
import com.belka.spigot.gm4.modules.SpeedPaths;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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

public class MainClass extends JavaPlugin implements Listener, PluginCommand {

    private CommandManager cmdMgmt;
    private ConfigManager storage;

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
        storage = new ConfigManager();

        SpeedPaths speedPaths = new SpeedPaths(this);
		RecipeHandler recipeHandler = new RecipeHandler();
		CustomCrafter customCrafter = new CustomCrafter(this, recipeHandler);
		HeartCanisters heartCanisters = new HeartCanisters(this);

        registerClasses(this, storage, cmdMgmt, speedPaths, customCrafter, heartCanisters);
    }

    @Override
    public void onDisable() {
		System.out.println(ConsoleColor.RED + ConsoleColor.BOLD + "Gamemode 4 has been disabled!" + ConsoleColor.RESET);
		saveConfig();
    	storage().saveAll();
    }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Advancements.grantAdvancement("gm4/start", e.getPlayer());
	}

	public String[] getCommands() {
		String commands[] = {"gamemode4","gm4"};
		return commands;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ((cmd.getName().equalsIgnoreCase("gamemode4") || cmd.getName().equalsIgnoreCase("gm4"))) {
			if (sender instanceof Player && (sender.isOp() || sender.hasPermission("gm4.reload")) || sender instanceof ConsoleCommandSender) {
				if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
					sender.sendMessage(ChatColor.RED + "Available: [reload]");
				}
				else if (args[0].equalsIgnoreCase("reload")) {
					reloadConfig();
					saveConfig();
					sender.sendMessage(ChatColor.GREEN + "Reloaded the Gamemode 4 DefaultEntries File");
				}
			}
		}
		return true;
	}

	// HELPER FUNCTIONS
	public List<Entity> getNearbyEntities(Location l, int size) {
		List<Entity> entities = new ArrayList<Entity>();

		for(Entity e : l.getWorld().getEntities()) {
			if(l.distance(e.getLocation()) <= size) {
				entities.add(e);
			}
		}
		return entities;
	}
	public List<EntityType> getNearbyEntityTypes(Location l, int size) {
		List<EntityType> entitytypes = new ArrayList<EntityType>();

		for(Entity e : l.getWorld().getEntities()) {
			if(l.distance(e.getLocation()) <= size) {
				entitytypes.add(e.getType());
			}
		}
		return entitytypes;
	}
	public List<Player> getNearbyPlayers(Location l, int size) {
		List<Player> players = new ArrayList<Player>();

		for(Entity e : l.getWorld().getEntities()) {
			if(l.distance(e.getLocation()) <= size && e instanceof Player) {
				players.add((Player) e);
			}
		}
		return players;
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
