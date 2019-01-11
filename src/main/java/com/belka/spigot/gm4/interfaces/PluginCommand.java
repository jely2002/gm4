package com.belka.spigot.gm4.interfaces;

import org.bukkit.command.CommandExecutor;

public interface PluginCommand extends CommandExecutor {
    String[] getCommands();
}