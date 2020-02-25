package api.LootTables;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Function")
public class Function implements ConfigurationSerializable {//TODO add other functions
	private String function;//Required
	private Map<String, Object> tags;//Required

	public Function(Map<String, Object> map) {
		this.function = (String) map.get("function");
		map.remove("function");
		this.tags = map;
	}
	public Function(String function, Map<String, Object> map) {
		this.function = function;
		this.tags = map;
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
		map.putAll(tags);
		return map;
	}
	public static Function deserialize(Map<String, Object> map) {
		return new Function(map);
	}

	public ItemStack apply(ItemStack item) {
		if (function.contains("set_count")) {
			if (tags.containsKey("count")) {
				if (tags.get("count") instanceof List) {
					List<Integer> count = (List<Integer>) tags.get("count");
					if (count.size() == 1)
						item.setAmount(count.get(0));
					else if (count.size() == 2)
						item.setAmount(LootTable.getRandomInRange(count.get(0), count.get(1)));
				}
			}
		}
		else if (function.contains("enchant_with_levels")) {
			boolean treasure = false;
			if (tags.containsKey("treasure")) {
				treasure = (boolean) tags.get("treasure");
			}
			if (tags.containsKey("levels")) {
				if (tags.get("levels") instanceof List) {
					List<Integer> levels = (List<Integer>) tags.get("levels");
					int level = 0;
					if (levels.size() == 1)
						level = levels.get(0);
					else if (levels.size() == 2)
						level = LootTable.getRandomInRange(levels.get(0), levels.get(1));
					item = Enchantments.enchant(item, level, treasure);
				}
			}
		}
		return item;
	}
}
