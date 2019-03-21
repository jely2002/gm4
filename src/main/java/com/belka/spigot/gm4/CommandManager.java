package com.belka.spigot.gm4;

import com.belka.spigot.gm4.config.SettingsGUI;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandManager implements PluginCommand {

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
        for(String key : subcommands.keySet()) {
            if(subcommands.get(key) == plcmd) {
                subcommands.remove(key);
            }
        }
    }

    public String[] getCommands() {
        String commands[] = {"gm4","gamemode4"};
        return commands;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length >= 1) {
            PluginSubcommand handler = subcommands.get(args[0]);
            return handler.onSubcommand(args, commandSender);
        } else if(args.length == 0) {
            if(commandSender instanceof Player) {
                Player p = (Player) commandSender;
                if(!p.hasPermission("gm4.settings")) return true;
                gui.openInventory(p);
            } else {
                commandSender.sendMessage(ChatColor.RED + "The settings menu can only be opened as player.");
            }

        } else {
            //TODO Show help.
        }
        return true;
    }
}
