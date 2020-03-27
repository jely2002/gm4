package com.belka.spigot.gm4;

import api.Helper;
import api.lootTables.LootTable;
import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.Module;
import com.belka.spigot.gm4.modules.Advancements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabCompleter, CommandExecutor {

	private ArrayList<String> subCommands = new ArrayList();

	private MainClass mc;
	private SettingsGUI gui;
	private CustomItems ci = new CustomItems();

	public CommandManager(MainClass mc, SettingsGUI gui) {
		this.mc = mc;
		this.gui = gui;
		subCommands.add("version");
		subCommands.add("reload");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (args.length >= 1) {
			if(args[0].equalsIgnoreCase("reload")) {
				if (sender instanceof Player && (sender.isOp() || sender.hasPermission("gm4.reload")) || sender instanceof ConsoleCommandSender) {
					mc.getStorage().reloadAll();
					mc.getStorage().saveAll();
					for(Module module : mc.getModules()) {
						module.reload();
					}
					sender.sendMessage(ChatColor.GREEN + "Reloaded Gamemode 4 configuration files & modules");
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
						ItemStack itemStack = (ItemStack) method.invoke(ci, x);
						p.getInventory().addItem(itemStack);
						sender.sendMessage("Gave " + x + " [" + ChatColor.stripColor(itemStack.getItemMeta().getDisplayName()) + "] to " + args[1]);
					} catch (Exception ignored) {}
				}
				else {
					sender.sendMessage(ChatColor.RED + "That player doesn't exist!");
				}
			}
			else if(args[0].equalsIgnoreCase("loot")) {//gm4 loot <source/file> <name> <x> <y> <z>
				if (args.length == 6 && sender instanceof Player) {
					LootTable loot = null;
					String name = args[2];
					if (name.contains("/")) name = name.split("/")[name.split("/").length];
					if (args[1].equalsIgnoreCase("source")) {
						try {
							loot = new LootTable().load(mc.getResourceAsFile("custom_terrain/" + args[2] + ".nbt"), name);
						} catch (IOException ignored) {}
					}
					else if (args[1].equalsIgnoreCase("file")) {
						loot = new LootTable().load(mc.getDataFolder() + "/", name);
					}
					Block block = new Location(((Player) sender).getWorld(), Helper.toInteger(args[3]), Helper.toInteger(args[4]), Helper.toInteger(args[5])).getBlock();
					if (block.getState() instanceof Chest && loot != null) {
						loot.place((Chest) block.getState(), true);
					}
				}
			}
		}
		else {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!p.hasPermission("gm4.settings")) return true;
				gui.openInventory(p);
			}
			else {
				sender.sendMessage(ChatColor.RED + "The settings menu can only be opened as player.");
			}
		}
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gamemode4")) {
			if (sender instanceof Player) {
				if (args.length == 1)
					return Helper.filterTab(args[0], subCommands.stream());
				else if (args[0].equalsIgnoreCase("give")) {
					if (args.length == 2) {
						return null;
					}
					else if (args.length == 3)
						return Helper.filterTab(args[2], "HEART_CANISTER_TIER_1", "HEART_CANISTER_TIER_2", "MINIUM_DUST", "INERT_STONE", "PHILOSOPHERS_STONE", "PHILOSOPHERS_STONE_MKII", "PHILOSOPHERS_STONE_MKIII", "PHILOSOPHERS_STONE_MKIV", "AC_ERROR", "BOOTS_OF_OSTARA", "TRAPPED_SIGN", "LIGHTNING_ROD", "SOUL_PROBES_BOOK", "EMPTY_SPAWN_EGG");
				}
				else if (args[0].equalsIgnoreCase("loot")) {
					if (args.length == 2) return Helper.filterTab(args[1], "source", "file");
					if (args.length == 3 && args[1].equalsIgnoreCase("source")) return Helper.filterTab(args[2], "tower_structures/", "dangerous_dungeons/");
					if (args.length == 4) return Helper.filterTab(args[3], Helper.getLookingCoords((Player) sender, 0, 3).stream());
					if (args.length == 5) return Helper.filterTab(args[4], Helper.getLookingCoords((Player) sender, 1, 3).stream());
					if (args.length == 6) return Helper.filterTab(args[5], Helper.getLookingCoords((Player) sender, 2, 3).stream());
				}
			}
		}
		return null;
	}
}
