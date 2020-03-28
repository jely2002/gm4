package com.belka.spigot.gm4.config;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {

	private MainClass mc;

	private CustomConfig data;
	public ConfigManager(MainClass mc) {
		this.mc = mc;
		init(mc);
	}


	public void init(MainClass mc) {
		data = new CustomConfig(mc, "data.yml") ;
		data.saveDefaultConfig();
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

	public void reloadAll() {
		reloadData();
		reloadConfig();
	}

	public void saveAll() {
		mc.saveConfig();
		saveData();
	}

	public void reloadData() {
		data.reloadConfig();
	}

	public void saveData() {
		data.save();
	}

	public void reloadConfig() {
		mc.reloadConfig();
	}

	public void saveConfigFile() {
		mc.saveConfig();
	}

}
