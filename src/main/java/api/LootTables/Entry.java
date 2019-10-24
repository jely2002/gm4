package api.LootTables;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Entry")
public class Entry implements ConfigurationSerializable {
	//TODO conditions
	private String type;
	private String name;
	//TODO children
	//TODO expand
	private List<Function> functions;
	private int weight;
	//TODO quality

	public Entry(String type, String name, List<Function> functions, int weight) {
		this.type = type;
		this.name = name;
		this.functions = functions;
		this.weight = weight;
	}
	public Entry(Map<String, Object> map) {
		this.type = (String) map.get("type");
		this.name = (String) map.get("name");
		this.functions = (List<Function>) map.get("functions");
		this.weight = (int) map.get("weight");
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

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("type", type);
		map.put("name", name);
		map.put("functions", functions);
		map.put("weight", weight);

		return map;
	}

	public static Entry deserialize(Map<String, Object> map) {
		return new Entry(map);
	}
}
