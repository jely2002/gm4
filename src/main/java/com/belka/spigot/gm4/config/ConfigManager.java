package com.belka.spigot.gm4.config;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager implements Initializable {

    private MainClass mc;

    private CustomConfig data;
    private CustomConfig locale;

    public void init(MainClass mc) {
       data = new CustomConfig(mc, "data.yml") ;
       locale = new CustomConfig(mc, "locale.yml");
       data.saveDefaultConfig();
       locale.saveDefaultConfig();
       loadDefaultConfig();
    }

    public void loadDefaultConfig() {
        File f = new File(mc.getDataFolder(), "config.yml");
        if (!f.exists()) {
            this.mc.saveResource("config.yml", false);
        }
    }

    public FileConfiguration config() {
        return mc.getConfig();
    }

    public FileConfiguration data() {
        return data.getConfig();
    }

    public FileConfiguration locale() {
        return locale.getConfig();
    }

    public void reloadAll() {
        reloadData();
        reloadConfig();
        reloadLocale();
    }

    public void saveAll() {
        mc.saveConfig();
        saveLocale();
        saveData();
    }

    public void reloadData() {
        data.reloadConfig();
    }

    public void saveData() {
        data.saveConfig();
    }

    public void reloadLocale() {
        locale.reloadConfig();
    }

    public void saveLocale() {
        locale.saveConfig();
    }

    public void reloadConfig() {
        mc.reloadConfig();
    }

    public void saveConfigFile() {
        mc.saveConfig();
    }

}
