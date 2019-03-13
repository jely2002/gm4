package com.belka.spigot.gm4.interfaces;

import org.bukkit.entity.Player;

public interface PluginSubcommand {
    String[] getSubcommand();
    Boolean onSubcommand(String[] args, Player p);
}
