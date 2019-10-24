package api.LootTables;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Pool")
public class Pool implements ConfigurationSerializable {
	//TODO conditions
	private int[] rolls;
	private int[] bonusRolls;
	private List<Entry> entries;

	public Pool(int[] rolls, List<Entry> entries) {
		this.rolls = rolls;
		this.entries = entries;
	}
	public Pool(Map<String, Object> map) {
		this.rolls = (int[]) map.get("rolls");
		if (map.get("bonus_rolls") != null) this.bonusRolls = (int[]) map.get("bonus_rolls");
		this.entries = (List<Entry>) map.get("entries");
	}

	public int[] getRolls() {
		return rolls;
	}
	public void setRolls(int[] rolls) {
		this.rolls = rolls;
	}

	public int[] getBonusRolls() {
		return bonusRolls;
	}
	public void setBonusRolls(int[] bonusRolls) {
		this.bonusRolls = bonusRolls;
	}

	public List<Entry> getEntries() {
		return entries;
	}
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	public void addEntry(Entry entry) {
		entries.add(entry);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("rolls", rolls);
		map.put("bonus_rolls", bonusRolls);
		map.put("entries", entries);

		return map;
	}

	public static Pool deserialize(Map<String, Object> map) {
		return new Pool(map);
	}
}
