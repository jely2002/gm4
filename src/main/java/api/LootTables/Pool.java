package api.LootTables;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pool implements ConfigurationSerializable {
	private int[] rolls;
	private List<Entry> entries;

	public Pool(int[] rolls, List<Entry> entries) {
		this.rolls = rolls;
		this.entries = entries;
	}

	public Pool(Map<String, Object> map) {
		this.rolls = (int[]) map.get("rolls");
		this.entries = (List<Entry>) map.get("entries");
//		if (map.get("idle") != null) this.idleFrame = (int) map.get("idle");
//		if (map.get("interact") != null) this.interactFrame = (int) map.get("interact");
//		this.rideable = map.get("rideable") != null && (boolean) map.get("rideable");
//		this.walkFrames = (List<Integer>) map.get("walk");
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("rolls", rolls);
		map.put("entries", entries);

		return map;
	}

	public static Pool deserialize(Map<String, Object> map) {
		return new Pool(map);
	}
}
