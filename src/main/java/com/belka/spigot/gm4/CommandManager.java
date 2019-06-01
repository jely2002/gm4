package com.belka.spigot.gm4;

import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager implements PluginCommand, TabCompleter {

	private HashMap<String, PluginSubcommand> subcommands = new HashMap<>();

	private MainClass mc;
	private SettingsGUI gui;

	public CommandManager(MainClass mc, SettingsGUI gui) {
		this.mc = mc;
		this.gui = gui;
	}

	public void addCommand(String subcommand, PluginSubcommand plcmd) {
		subcommands.put(subcommand, plcmd);
	}

	public void removeCommand(String subcommand) {
		subcommands.remove(subcommand);
	}

	public void removeCommands(PluginSubcommand plcmd) {
		for (String key : subcommands.keySet()) {
			if (subcommands.get(key) == plcmd) {
				subcommands.remove(key);
			}
		}
	}

	public String[] getCommands() {
		String[] commands = {"gm4", "gamemode4"};
		return commands;
	}

	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
		if (args.length >= 1) {
			PluginSubcommand handler = subcommands.get(args[0]);
			return handler.onSubcommand(args, commandSender);
		}
		else {
			if (commandSender instanceof Player) {
				Player p = (Player) commandSender;
				if (!p.hasPermission("gm4.settings")) return true;
				gui.openInventory(p);
			}
			else {
				commandSender.sendMessage(ChatColor.RED + "The settings menu can only be opened as player.");
			}
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gamemode4")) {
			if (sender instanceof Player) {
				if (args.length == 1) return Arrays.asList("reload", "version", "achievement", "give");
				else if (args[0].equalsIgnoreCase("give")) {
					if (args.length == 2) {
						ArrayList<String> playerNames = new ArrayList<>();
						for (Player p:  Bukkit.getOnlinePlayers())
							playerNames.add(p.getDisplayName());
						return playerNames;
					}
					else if (args.length == 3) {
						return Arrays.asList("HEART_CANISTER_TIER_1", "HEART_CANISTER_TIER_2", "MINIUM_DUST", "INERT_STONE", "PHILOSOPHERS_STONE", "PHILOSOPHERS_STONE_MKII", "PHILOSOPHERS_STONE_MKIII", "PHILOSOPHERS_STONE_MKIV", "AC_ERROR", "BOOTS_OF_OSTARA", "TRAPPED_SIGN", "LIGHTNING_ROD", "SOUL_PROBES_BOOK", "EMPTY_SPAWN_EGG");
					}
				}
			}
		}
		return null;
	}
}
