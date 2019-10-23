package api;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class LootTable {
	private File file;
	private JSONArray pools;

	public LootTable(File file) throws IOException {
		this.file = file;

		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(file)) {
			JSONObject obj = (JSONObject) jsonParser.parse(reader);
			pools = (JSONArray) obj.get("pools");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return file;
	}

	public JSONArray getPools() {
		return pools;
	}

	public void setLootTable(Block b) {
		/* TODO
		include bonus_rolls
		 */
		if (pools == null) return;
		for (Object poolObj: pools) {
			if (poolObj instanceof JSONObject) {
				JSONObject pool = (JSONObject) poolObj;
				if (!pool.containsKey("rolls")) return;
				Object rollsObj = pool.get("rolls");
				int rolls = 0;
				if (rollsObj instanceof Integer) {
					rolls = (int) rollsObj;
				}
				else if (rollsObj instanceof JSONObject) {
					JSONObject rollsMMObj = (JSONObject) rollsObj;
					if (!rollsMMObj.containsKey("min") || !rollsMMObj.containsKey("max")) return;
					if (rollsMMObj.get("min") instanceof Integer && rollsMMObj.get("max") instanceof Integer)
						rolls = getRandomNumberInRange((int) rollsMMObj.get("min"), (int) rollsMMObj.get("max"));
					else return;
				}
				if (rolls > 0) {
					if (!pool.containsKey("entries")) return;
					Object entriesObj = pool.get("entries");
					if (entriesObj instanceof JSONArray) {
						for (Object entryObj: (JSONArray) entriesObj) {
							if (entryObj instanceof JSONObject) {
								JSONObject entry = (JSONObject) entryObj;
								if (!entry.containsKey("type") || !entry.containsKey("name") || !entry.containsKey("weight")) return;
								if (entry.get("type") instanceof String && ((String) entry.get("type")).equalsIgnoreCase("minecraft:item")) {
									Material itemMat = Material.matchMaterial((String) entry.get("name"));
								}
							}
						}
					}
				}
			}
		}
	}

	private static int getRandomNumberInRange(int int1, int int2) {
		int min = Math.min(int1, int2);
		int max = Math.max(int1, int2);
		return new Random().nextInt((max - min) + 1) + min;
	}
}
