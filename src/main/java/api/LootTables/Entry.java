package api.LootTables;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Entry")
public class Entry implements ConfigurationSerializable {
	//TODO conditions
	private String type;//Required
	private String name;//Required
	//TODO children
	//TODO expand
	private List<Function> functions;//Required
	private int weight;//Required
	private int quality;

	public Entry(String type, String name, List<Function> functions, int weight) {
		this.type = type;
		this.name = name;
		this.functions = functions;
		this.weight = weight;
	}
	public Entry(Map<String, Object> map) {
		this.type = (String) map.get("type");
		this.name = (String) map.get("name");
		if (map.get("functions") != null) this.functions = (List<Function>) map.get("functions");
		this.weight = (int) map.get("weight");
		if (map.get("quality") != null) this.quality = (int) map.get("quality");
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<Function> getFunctions() {
		return functions;
	}
	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("type", type);
		map.put("name", name);
		if (functions != null) map.put("functions", functions);
		map.put("weight", weight);
		map.put("quality", quality);

		return map;
	}
	public static Entry deserialize(Map<String, Object> map) {
		return new Entry(map);
	}
}
