package com.belka.spigot.gm4.interfaces;

import org.bukkit.command.CommandSender;

public interface PluginSubcommand {
    String[] getSubcommand();
    Boolean onSubcommand(String[] args, CommandSender sender);
}
