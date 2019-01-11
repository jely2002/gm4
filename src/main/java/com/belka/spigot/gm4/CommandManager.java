package com.belka.spigot.gm4;

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

    public CommandManager(MainClass mc) {
        this.mc = mc;
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
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }
        Player p = (Player) commandSender;
        if(args.length >= 1) {
            PluginSubcommand handler = subcommands.get(args[0]);
            return handler.onSubcommand(args, p);
        } else {
            //TODO Show help.
        }
        return true;
    }
}
