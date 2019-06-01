package com.belka.spigot.gm4;

import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class MainCommands implements PluginSubcommand {

	private MainClass mc;
	private CustomItems ci;

	MainCommands(MainClass mc, CustomItems ci) {
		this.mc = mc;
		this.ci = ci;
	}

	@Override
	public String[] getSubcommand() {
        String[] commands = {"reload", "version", "advancement", "give"};
		return commands;
	}

	@Override
	public Boolean onSubcommand(String[] args, CommandSender sender) {
		if(args[0].equalsIgnoreCase("reload")) {
			if (sender instanceof Player && (sender.isOp() || sender.hasPermission("gm4.reload")) || sender instanceof ConsoleCommandSender) {
				mc.storage().reloadAll();
				mc.storage().saveAll();
				mc.speedPaths.loadValues();
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
		else if(args[0].equalsIgnoreCase("give") && (args.length == 3 || args.length == 4)) {
			Player p = Bukkit.getPlayer(args[1]);
			if(Bukkit.getOnlinePlayers().contains(p)) {
				int x = 1;
				if (args.length== 4) x = Integer.parseInt(args[3]);
				try {
					Class<?> type = CustomItems.class;
					Method method = type.getMethod(args[2], int.class);
					//as the method is not static, you need to have an instance of the class to be able to invoke the method
					CustomItems instance = new CustomItems();
					ItemStack itemStack = (ItemStack) method.invoke(instance, x);
					p.getInventory().addItem(itemStack);
					sender.sendMessage("Gave " + x + " [" + ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()) + "] to " + args[1]);
				} catch (Exception toBeHandled) {}
			}
			else {
				sender.sendMessage(ChatColor.RED + "That player doesn't exist!");
			}
		}
		return true;
	}
}
