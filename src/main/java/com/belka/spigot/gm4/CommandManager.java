package com.belka.spigot.gm4;

import api.CustomBlock;
import api.CustomBlockType;
import api.Helper;
import api.lootTables.LootTable;
import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.crafting.CustomItems;
import com.belka.spigot.gm4.interfaces.Module;
import com.belka.spigot.gm4.modules.Advancements;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements TabCompleter, CommandExecutor {

	private MainClass mc;
	private SettingsGUI gui;
	private CustomItems ci = new CustomItems();

	public CommandManager(MainClass mc, SettingsGUI gui) {
		this.mc = mc;
		this.gui = gui;
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
							loot = LootTable.load(mc.getResourceAsFile("custom_terrain/" + args[2] + ".nbt"), name);
						} catch (IOException ignored) {}
					}
					else if (args[1].equalsIgnoreCase("file")) {
						loot = LootTable.load(mc.getDataFolder() + "/", name);
					}
					Block block = new Location(((Player) sender).getWorld(), Helper.toInteger(args[3]), Helper.toInteger(args[4]), Helper.toInteger(args[5])).getBlock();
					if (block.getState() instanceof Chest && loot != null) {
						loot.place((Chest) block.getState(), true);
					}
				}
			}
			else if (args[0].equalsIgnoreCase("customblock")) {
				if (args.length == 1) {
					sender.sendMessage(ChatColor.RED + "Available: [place/replace/destroy]");
				}
				else if (args[1].equalsIgnoreCase("place")) {//gm4 customblock place <type> <x> <y> <z> [world]
					if (args.length == 6 || args.length == 7) {
						if (CustomBlockType.isType(args[2])) {
							CustomBlockType cbt = CustomBlockType.getById(args[2]);
							assert cbt != null;
							if (Helper.isInteger(args[3])) {
								if (Helper.isInteger(args[4])) {
									if (Helper.isInteger(args[5])) {
										World w = null;
										if (args.length == 7) {
											if (Bukkit.getWorld(args[6]) != null) {
												w = Bukkit.getWorld(args[6]);
											}
											else sender.sendMessage(ChatColor.RED + "That world doesn't exist!");
										}
										else if (sender instanceof Player) w = ((Player) sender).getWorld();
										else if (sender instanceof BlockCommandSender) w = ((BlockCommandSender) sender).getBlock().getWorld();
										else if (sender instanceof CommandMinecart) w = ((CommandMinecart) sender).getWorld();
										else sender.sendMessage(ChatColor.RED + "You need to provide a world!");
										if (w != null) {
											CustomBlock cb = CustomBlock.create(cbt, new Location(w, Helper.toInteger(args[3]), Helper.toInteger(args[4]), Helper.toInteger(args[5])));
											sender.sendMessage(ChatColor.GREEN + "Successfully created a " + cbt.getName() + " at " + args[3] + " " + args[4] + " " + args[5] + ".");
										}
										else sender.sendMessage(ChatColor.RED + "No world found!");
									}
									else number(sender, args[5], "z");
								}
								else number(sender, args[4], "y");
							}
							else number(sender, args[3], "x");
						}
						else sender.sendMessage(ChatColor.RED + "\"" + args[2] + "\" is not a valid custom block! (type)");
					}
					else sender.sendMessage(ChatColor.RED + "Check your argument count!");
				}
				else if (args[1].equalsIgnoreCase("replace")) {

				}
				else if (args[1].equalsIgnoreCase("destroy")) {//gm4 customblock destroy <x> <y> <z> [world]
					if (args.length == 5 || args.length == 6) {
						if (Helper.isInteger(args[2])) {
							if (Helper.isInteger(args[3])) {
								if (Helper.isInteger(args[4])) {
									World w = null;
									if (args.length == 6) {
										if (Bukkit.getWorld(args[5]) != null) {
											w = Bukkit.getWorld(args[5]);
										}
										else sender.sendMessage(ChatColor.RED + "That world doesn't exist!");
									}
									else if (sender instanceof Player) w = ((Player) sender).getWorld();
									else if (sender instanceof BlockCommandSender) w = ((BlockCommandSender) sender).getBlock().getWorld();
									else if (sender instanceof CommandMinecart) w = ((CommandMinecart) sender).getWorld();
									else sender.sendMessage(ChatColor.RED + "You need to provide a world!");
									if (w != null) {
										CustomBlock cb = CustomBlock.get(new Location(w, Helper.toInteger(args[2]), Helper.toInteger(args[3]), Helper.toInteger(args[4])));
										if (cb == null) {
											sender.sendMessage(ChatColor.RED + "That block was not found!");
										}
										else {
											cb.destroy(true);
											sender.sendMessage(ChatColor.GREEN + "Successfully destroyed a " + cb.getType().getName() + " at " + args[2] + " " + args[3] + " " + args[4] + ".");
										}
									}
									else sender.sendMessage(ChatColor.RED + "No world found!");
								}
								else number(sender, args[5], "z");
							}
							else number(sender, args[4], "y");
						}
						else number(sender, args[3], "x");
					}
					else sender.sendMessage(ChatColor.RED + "Check your argument count!");
				}
				else sender.sendMessage(ChatColor.RED + "Available: [place/replace/destroy]");
			}
		}
		else {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!p.hasPermission("gm4.settings")) return true;
//				gui.openInventory(p);
				p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				p.openInventory(gui.settings(p, 1));
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
					return Helper.filterTab(args[0], "version", "reload", "give", "customblock");
				else if (args[0].equalsIgnoreCase("give")) {
					if (args.length == 2) {
						return null;
					}
					else if (args.length == 3) {
						List<String> items = new ArrayList<>();
						for (Method method : CustomItems.class.getDeclaredMethods()) {
							String name = method.getName();
							if (!name.equalsIgnoreCase("getSkull") && !name.equalsIgnoreCase("ENDER_HOPPER_SKULL")) items.add(name);
						}
						return Helper.filterTab(args[2], items);
					}
				}
				else if (args[0].equalsIgnoreCase("loot")) {
					if (args.length == 2) return Helper.filterTab(args[1], "source", "file");
					if (args.length == 3 && args[1].equalsIgnoreCase("source")) return Helper.filterTab(args[2], "tower_structures/", "dangerous_dungeons/");
					if (args.length == 4) return Helper.filterTab(args[3], Helper.getLookingCoords((Player) sender, 0, 3));
					if (args.length == 5) return Helper.filterTab(args[4], Helper.getLookingCoords((Player) sender, 1, 3));
					if (args.length == 6) return Helper.filterTab(args[5], Helper.getLookingCoords((Player) sender, 2, 3));
				}
				else if (args[0].equalsIgnoreCase("customblock")) {
					/*
					/gm4 customblock place <type> <x> <y> <z>
					/gm4 customblock replace <type> <x> <y> <z>
					/gm4 customblock destroy <x> <y> <z>
					 */
					if (args.length == 2) return Helper.filterTab(args[1], "place", "replace", "destroy");
					if (args.length == 3) {
						if (args[1].equalsIgnoreCase("place") || args[1].equalsIgnoreCase("replace"))
							return Helper.filterTab(args[2], Arrays.stream(CustomBlockType.values()).map(CustomBlockType::getId));
						if (args[1].equalsIgnoreCase("destroy")) return Helper.filterTab(args[2], Helper.getLookingCoords((Player) sender, 0, 3));
					}
					if (args.length == 4) {
						if (args[1].equalsIgnoreCase("place") || args[1].equalsIgnoreCase("replace"))
							return Helper.filterTab(args[3], Helper.getLookingCoords((Player) sender, 0, 3));
						if (args[1].equalsIgnoreCase("destroy")) return Helper.filterTab(args[3], Helper.getLookingCoords((Player) sender, 1, 3));
					}
					if (args.length == 5) {
						if (args[1].equalsIgnoreCase("place") || args[1].equalsIgnoreCase("replace"))
							return Helper.filterTab(args[4], Helper.getLookingCoords((Player) sender, 1, 3));
						if (args[1].equalsIgnoreCase("destroy")) return Helper.filterTab(args[4], Helper.getLookingCoords((Player) sender, 2, 3));
					}
					if (args.length == 6) return Helper.filterTab(args[5], Helper.getLookingCoords((Player) sender, 2, 3));
				}
			}
		}
		return null;
	}

	private static void number(CommandSender sender, String value, String var) {
		sender.sendMessage(ChatColor.RED + "\"" + value + "\" is not a valid number! (" + var + ")");
	}
}
