package com.belka.spigot.gm4.modules;

import java.util.ArrayList;
import java.util.List;

import com.belka.spigot.gm4.crafting.CustomRecipies;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class MainClass extends JavaPlugin implements Listener, CommandExecutor {
	
	Config con;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println("Oo---------------oO " + ConsoleColor.CYAN + ConsoleColor.BOLD + "Gamemode 4" + ConsoleColor.RESET + " Oo---------------oO");
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Version  V" + pdfFile.getVersion() + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Author  MaximumFX & jely2002" + ConsoleColor.RESET);
		System.out.println(ConsoleColor.GREEN + ConsoleColor.BOLD + "		Status  Enabled" + ConsoleColor.RESET);
		System.out.println("Oo-----------------------oOo-----------------------oO");
		
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();

		con = new Config(this);
		BatGrenades bg = new BatGrenades(this);
		BetterArmorStands bta = new BetterArmorStands(this);
		BetterFire bf = new BetterFire(this);
		DesireLines dl = new DesireLines(this);
		EndermanSupportClass esc = new EndermanSupportClass(this);
		InkSpittingSquid isq = new InkSpittingSquid(this);
		VorpelSwords vs = new VorpelSwords(this);
		WeightedArmour ws = new WeightedArmour(this);
		CustomCrafter cc = new CustomCrafter(this);
		CustomRecipies cr = new CustomRecipies(this);
		HeartCanisters hr = new HeartCanisters(this);
		Advancements adv = new Advancements(this);
		
		getCommand("gamemode4").setExecutor(this);
		getCommand("gm4").setExecutor(this);
		
		registerEvents(this, con, bg, bta, bf, dl, isq, esc, vs, ws, hr, cc, cr, adv);
		
		con.makeConfig();
		
		bg.start();
		bta.start();
		bf.start();
		cc.start();
		esc.start();
		isq.start();
		ws.start();
		hr.start();
		adv.createAndSave();
	}
	
	public void onDisable() {
		System.out.println(ConsoleColor.RED + ConsoleColor.BOLD + "Gamemode 4 has been disabled!" + ConsoleColor.RESET);
		saveConfig();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Advancements.grantAdvancement("gm4/start", e.getPlayer());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((cmd.getName().equalsIgnoreCase("gamemode4") || cmd.getName().equalsIgnoreCase("gm4"))) {
			if(sender instanceof Player && (sender.isOp() || sender.hasPermission("gm4.reload")) || !(sender instanceof Player)) {
				if(args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
					sender.sendMessage(ChatColor.RED + "Available: [reload]");
				}
				else if(args[0].equalsIgnoreCase("reload")){
					reloadConfig();
					con.makeConfig();
					saveConfig();
					sender.sendMessage(ChatColor.GREEN + "Reloaded the Gamemode 4 Config File");
				}
			}
		}
		return true;
	}
	
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

	public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
		for (Listener listener : listeners) {
		  Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
}
