package api;

import org.bukkit.Material;

public class Setting {

	private String name;
	private Material icon;
	private String path = "";

	public Setting(String name, Material icon) {
		this.name = name;
		this.icon = icon;
	}

	public Setting setName(String name) {
		this.name = name;
		return this;
	}
	public String getName() {
		return name;
	}

	public Setting setIcon(Material icon) {
		this.icon = icon;
		return this;
	}
	public Material getIcon() {
		return icon;
	}

	public Setting setPath(String path) {
		this.path = path;
		return this;
	}
	public String getPath() {
		return path;
	}
}
