package com.belka.spigot.gm4;

import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MainCommands implements PluginSubcommand {

	private MainClass mc;

	MainCommands(MainClass mc) {
		this.mc = mc;
	}

	@Override
	public String[] getSubcommand() {
		String commands[] = {"reload","version","advancement"};
		return commands;
	}

	@Override
	public Boolean onSubcommand(String[] args, CommandSender sender) {
		if(args[0].equalsIgnoreCase("reload")) {
			if (sender instanceof Player && (sender.isOp() || sender.hasPermission("gm4.reload")) || sender instanceof ConsoleCommandSender) {
				mc.storage().reloadAll();
				mc.storage().saveAll();
				sender.sendMessage(ChatColor.GREEN + "Reloaded Gamemode 4 configuration files.");
			}
		}
		else if(args[0].equalsIgnoreCase("version")) {
			sender.sendMessage(ChatColor.GREEN + mc.getDescription().getFullName() + " is on version:");
			sender.sendMessage(ChatColor.GREEN + mc.getDescription().getVersion());
		}
		else if(args[0].equalsIgnoreCase("advancement") && args.length == 3) {
			if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
				Advancements.grantAdvancement(args[2], Bukkit.getPlayer(args[1]));
				sender.sendMessage(ChatColor.GREEN + "Granted " + args[1] + " the advancement " + args[2] + ".");
			}
			else {
				sender.sendMessage(ChatColor.RED + "That player doesn't exist!");
			}
		}
		return true;
	}
}
