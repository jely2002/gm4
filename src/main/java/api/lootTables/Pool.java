package api.lootTables;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;
import java.util.stream.Collectors;

@SerializableAs("Pool")
public class Pool implements ConfigurationSerializable {
	//TODO conditions
	private List<Integer> rolls;//Required
	private List<Integer> bonusRolls;
	private List<Entry> entries;//Required

	public Pool(List<Integer> rolls, List<Entry> entries) {
		this.rolls = rolls;
		this.entries = entries;
	}
	public Pool(int[] rolls, List<Entry> entries) {
		this.rolls = Arrays.stream(rolls).boxed().collect(Collectors.toList());;
		this.entries = entries;
	}
	public Pool(Map<String, Object> map) {
		if (map.get("rolls") instanceof Integer) this.rolls = Collections.singletonList((int) map.get("rolls"));
		else this.rolls = (List<Integer>) map.get("rolls");
		if (map.get("bonus_rolls") != null) this.bonusRolls = (List<Integer>) map.get("bonus_rolls");
		this.entries = (List<Entry>) map.get("entries");
	}

	public List<Integer> getRolls() {
		return rolls;
	}
	public void setRolls(List<Integer> rolls) {
		this.rolls = rolls;
	}

	public List<Integer> getBonusRolls() {
		return bonusRolls;
	}
	public void setBonusRolls(List<Integer> bonusRolls) {
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
		if (bonusRolls != null) map.put("bonus_rolls", bonusRolls);
		map.put("entries", entries);

		return map;
	}
	public static Pool deserialize(Map<String, Object> map) {
		return new Pool(map);
	}
}
