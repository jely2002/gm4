package api.lootTables;

import api.ConsoleColor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SerializableAs("LootTable")
public class LootTable implements ConfigurationSerializable {
	public LootTable() {

	}

	private String type;
	private List<Pool> pools;//Required

	public LootTable(List<Pool> pools) {
		this.pools = pools;
	}
	public LootTable(String type, List<Pool> pools) {
		this.type = type;
		this.pools = pools;
	}
	public LootTable(Map<String, Object> map) {
		if (map.get("type") != null) this.type = (String) map.get("type");
		this.pools = (List<Pool>) map.get("pools");
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public List<Pool> getPools() {
		return pools;
	}
	public void setPools(List<Pool> pools) {
		this.pools = pools;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		if (type != null) map.put("type", type);
		map.put("pools", pools);
		return map;
	}
	public static LootTable deserialize(Map<String, Object> map) {
		return new LootTable(map);
	}

	public LootTable load(String location, String name) {
		File file = new File(location, name + ".yml");
		LootTable lootTable = null;
		if (file.exists()) lootTable = (LootTable) YamlConfiguration.loadConfiguration(file).get(name);
		else {
			System.out.println(ConsoleColor.BLACK + "[" + ConsoleColor.CYAN + "GM4" + ConsoleColor.BLACK + "] " + ConsoleColor.RESET + "Yml file doesn't exist, trying to load from json");
			File json = new File(location, name + ".json");
			if (json.exists()) {
				try {
					saveJsonAsYaml(file, location, name);
					lootTable = (LootTable) YamlConfiguration.loadConfiguration(new File(location, name + ".yml")).get(name);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else System.out.println(ConsoleColor.BLACK + "[" + ConsoleColor.CYAN + "GM4" + ConsoleColor.BLACK + "] " + ConsoleColor.RESET + "Json file doesn't exist!");
		}
		return lootTable;
	}
	public LootTable load(File file, String name) {
		LootTable lootTable = null;
		if (file.exists()) lootTable = (LootTable) YamlConfiguration.loadConfiguration(file).get(name);
		return lootTable;
	}
	public void saveJsonAsYaml(File file, String location, String name) throws IOException {
		String fileString = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
		FileUtils.writeStringToFile(new File(location, name + ".yml"), getAsYaml(fileString, name), StandardCharsets.UTF_8);
	}

	public void place(Chest c, boolean empty) {
		List<ItemStack> items = new ArrayList<>();
		for (Pool pool: pools) {
			int poolRolls;
			if (pool.getRolls().size() == 1) poolRolls = pool.getRolls().get(0);
			else poolRolls = getRandomInRange(pool.getRolls().get(0), pool.getRolls().get(1));
//			if (pool.getBonusRolls() != null) {//TODO add per point of luck
//				if (pool.getBonusRolls().size() == 1) poolRolls += pool.getBonusRolls().get(0);
//				else poolRolls += getRandomInRange(pool.getBonusRolls().get(0), pool.getBonusRolls().get(1));
//			}
			List<Entry> entries = pool.getEntries();
			WeightedRandom<Entry> weightedRandom = new WeightedRandom<>();
			for (Entry entry: entries) {
				weightedRandom.addEntry(entry, entry.getWeight());
			}
			for (int i = 0; i < poolRolls; i++) {
				Entry entry = weightedRandom.getRandom();
				if (entry.getType().contains("item")) {
					Material mat = Material.matchMaterial(entry.getName());
					assert mat != null;
					ItemStack item = new ItemStack(mat);
					if (entry.getFunctions() != null)
						for (Function function: entry.getFunctions()) {
							item = function.apply(item);
						}
					items.add(item);
				}
			}
		}
		if (empty) c.getBlockInventory().clear();
		fillInventory(c.getBlockInventory(), items);
	}
	private void fillInventory(Inventory inv, List<ItemStack> items) {
		Random random = new Random();
//		for (ItemStack itemStack : items) {
//			int randomSlot;
//			do {
//				randomSlot = random.nextInt(inv.getSize());
//				if (inv.getItem(randomSlot) == null)
//					inv.setItem(randomSlot, itemStack);
//			} while (inv.firstEmpty() != -1); // avoid infinite loop
//		}
		boolean[] chosen = new boolean[inv.getSize()]; // This checks which slots are already taken in the inventory.
		for (ItemStack item : items) { // Loop through all items you want to add.
			int slot;
			do {
				slot = random.nextInt(inv.getSize());
			} while (chosen[slot]); // Make sure the slot does not already have an item in it.
			chosen[slot] = true;
			inv.setItem(random.nextInt(inv.getSize()), item); // Set the item in the chest to a random place (which is not taken).
		}
	}


	private String getAsYaml(String jsonString, String name) throws JsonProcessingException {
		JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
		JsonArray pools = json.getAsJsonArray("pools");
		for (JsonElement poolEl: pools) {
			JsonObject pool = poolEl.getAsJsonObject();
			pool.remove("conditions");
			JsonArray entries = pool.getAsJsonArray("entries");
			for (JsonElement entryEl: entries) {
				JsonObject entry = entryEl.getAsJsonObject();
				entry.remove("conditions");
				entry.remove("children");
				entry.remove("expand");
				if (entry.has("functions")) {
					for (JsonElement functionEl: entry.getAsJsonArray("functions")) {
						JsonObject function = functionEl.getAsJsonObject();
						function.remove("conditions");
					}
				}
			}
		}
		String yml = new YAMLMapper().writeValueAsString(new ObjectMapper().readTree(json.toString()));
		yml = yml.replace("\n", "\n  ");
		yml = yml.replace("---", name + ":\n  ==: LootTable");
		yml = yml.replace("\n  - ", "\n  - ==: Pool\n    ");
		yml = yml.replace("\n    - ", "\n    - ==: Entry\n      ");
		yml = yml.replace("\n      - ", "\n      - ==: Function\n        ");
		yml = yml.replace("min:", "-").replace("max:", "-");
		return yml;
	}

	public static int getRandomInRange(int int1, int int2) {
		int min = Math.min(int1, int2);
		int max = Math.max(int1, int2);
		return new Random().nextInt((max - min) + 1) + min;
	}
}
