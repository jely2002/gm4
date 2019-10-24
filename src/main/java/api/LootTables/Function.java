package api.LootTables;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Function")
public class Function implements ConfigurationSerializable {
	private String function;
	private Map<String, Object> tags;

	public Function(Map<String, Object> map) {
		this.function = (String) map.get("function");
		this.tags = (Map<String, Object>) map.get("tags");
	}

	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}

	public Map<String, Object> getTags() {
		return tags;
	}
	public void setTags(Map<String, Object> tags) {
		this.tags = tags;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("function", function);
		map.put("tags", tags);
		return map;
	}
}
