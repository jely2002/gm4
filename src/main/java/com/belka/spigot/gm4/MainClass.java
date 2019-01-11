package com.belka.spigot.gm4;

import com.belka.spigot.gm4.config.ConfigManager;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.belka.spigot.gm4.interfaces.PluginCommand;
import com.belka.spigot.gm4.interfaces.PluginSubcommand;
import com.belka.spigot.gm4.modules.SpeedPaths;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class MainClass extends JavaPlugin {

    private CommandManager cmdMgmt;
    private ConfigManager storage;

    @Override
    public void onEnable() {
        cmdMgmt = new CommandManager(this);
        storage = new ConfigManager();

        SpeedPaths speedPaths = new SpeedPaths(this);

        registerClasses(storage, cmdMgmt, speedPaths);
    }

    @Override
    public void onDisable() { storage().saveAll(); }

    public ConfigManager storage() {
        return storage;
    }

    private void registerClasses(Object... classes) {
        int listenersRegistered = 0;
        int commandsRegistered = 0;
        int subcommandsRegistered = 0;
        int classesInitialized = 0;
        for(Object o : classes) {
            if(o instanceof Listener) {
                getServer().getPluginManager().registerEvents((Listener) o, this);
                listenersRegistered++;
            }
            if(o instanceof PluginCommand) {
                PluginCommand plcmd = (PluginCommand) o;
                for(String arg : plcmd.getCommands()) {
                    getCommand(arg).setExecutor(plcmd);
                    commandsRegistered++;
                }
            }
            if(o instanceof PluginSubcommand) {
                PluginSubcommand plscmd = (PluginSubcommand) o;
                for(String arg : plscmd.getSubcommand()) {
                    cmdMgmt.addCommand(arg, plscmd);
                    subcommandsRegistered++;
                }
            }
            if(o instanceof Initializable) {
                Initializable initClass = (Initializable) o;
                initClass.init(this);
                classesInitialized++;
            }
        }
        if(getConfig().getBoolean("internal.verbosemode")) {
            getLogger().log(Level.INFO, "Registered " + commandsRegistered + " commands.");
            getLogger().log(Level.INFO, "Registered " + subcommandsRegistered + " subcommands.");
            getLogger().log(Level.INFO, "Registered " + listenersRegistered + " listeners.");
            getLogger().log(Level.INFO, "Initialized  " + listenersRegistered + " classes.");
        }
    }

}
