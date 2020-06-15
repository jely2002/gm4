package api.lootTables;

import api.ConsoleColor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTCompoundList;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

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

	public static LootTable load(String location, String name) {
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
	public static LootTable load(File file, String name) {
		LootTable lootTable = null;
		if (file.exists()) lootTable = (LootTable) YamlConfiguration.loadConfiguration(file).get(name);
		return lootTable;
	}
	public static void saveJsonAsYaml(File file, String location, String name) throws IOException {
		String fileString = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
		FileUtils.writeStringToFile(new File(location, name + ".yml"), getAsYaml(fileString, name), StandardCharsets.UTF_8);
	}

	public void place(Chest c, boolean empty) {
		List<ItemStack> items = getItems();

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
	public ItemStack asItem() {
		NBTItem nbti = new NBTItem(new ItemStack(Material.CHEST));
		NBTCompound blockEntityTag = nbti.addCompound("BlockEntityTag");
		NBTCompoundList items = blockEntityTag.getCompoundList("Items");

		List<ItemStack> itemsList = getItems();

		Random random = new Random();
		boolean[] chosen = new boolean[27];

		for (ItemStack itemStack: itemsList) {
			int slot;
			do {
				slot = random.nextInt(27);
			} while (chosen[slot]); // Make sure the slot does not already have an item in it.
			chosen[slot] = true;
			NBTItem nbtItem = getNBTItem(itemStack);
			nbtItem.setInteger("Slot", random.nextInt(27));
			items.addCompound(nbtItem);
		}

		return nbti.getItem();
	}

	private List<ItemStack> getItems() {
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
		return items;
	}

	private NBTItem getNBTItem(ItemStack itemStack) {
		NBTItem nbtItem = new NBTItem(itemStack);
		nbtItem.setString("id", itemStack.getType().name().toLowerCase());
		nbtItem.setInteger("Count", itemStack.getAmount());

		NBTCompound tag = nbtItem.addCompound("tag");

		//Damage
		if (itemStack instanceof Damageable)
			tag.setInteger("Damage", ((Damageable) itemStack).getDamage());

		//Enchantments
		if (itemStack.getEnchantments().size() > 0) {
			NBTCompoundList enchantments = tag.getCompoundList("Enchantments");
			for (Map.Entry<Enchantment, Integer> enchantment: itemStack.getEnchantments().entrySet()) {
				NBTListCompound enchant = enchantments.addCompound();
				enchant.setString("id", enchantment.getKey().getKey().getKey());
				enchant.setInteger("lvl", enchantment.getValue());
			}
		}

		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			NBTCompound display = nbtItem.addCompound("display");
			if (meta.hasDisplayName())
				display.setString("Name", meta.getDisplayName());
			if (meta.getLore() != null && meta.getLore().size() > 0) {
				List<String> l = display.getStringList("Lore");
				l.addAll(meta.getLore());
			}
			if (meta instanceof LeatherArmorMeta) {
//				Color color = ((LeatherArmorMeta) meta).getColor();
//				int rgb = color.getRed();
//				rgb = (rgb << 8) + color.getGreen();
//				rgb = (rgb << 8) + color.getBlue();
				display.setInteger("color", ((LeatherArmorMeta) meta).getColor().asRGB());//TODO check if correct
			}

			if (meta.getAttributeModifiers() != null && meta.getAttributeModifiers().size() > 0) {
				NBTCompoundList attribute = nbtItem.getCompoundList("AttributeModifiers");
				for (Map.Entry<Attribute, Collection<AttributeModifier>> modifier: meta.getAttributeModifiers().asMap().entrySet()) {

					NBTListCompound mod = attribute.addCompound();
					mod.setInteger("Amount", 10);
					mod.setString("AttributeName", "generic.maxHealth");
					mod.setString("Name", "generic.maxHealth");
					mod.setInteger("Operation", 0);
					mod.setInteger("UUIDLeast", 59664);
					mod.setInteger("UUIDMost", 31453);
				}
			}
			if (meta.isUnbreakable())
				nbtItem.setBoolean("Unbreakable", true);

			if (meta instanceof SkullMeta) {
				nbtItem.setString("SkullOwner", ((SkullMeta) meta).getOwningPlayer().getName());
//				NBTCompound skull = nbti.addCompound("SkullOwner");
//				skull.setString("Name", "tr7zw");
//				skull.setString("Id", "fce0323d-7f50-4317-9720-5f6b14cf78ea");
//				NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
//				texture.setString("Signature", "XpRfRz6/vXE6ip7/vq+40H6W70GFB0yjG6k8hG4pmFdnJFR+VQhslE0gXX/i0OAGThcAVSIT+/W1685wUxNofAiy+EhcxGNxNSJkYfOgXEVHTCuugpr+EQCUBI6muHDKms3PqY8ECxdbFTUEuWxdeiJsGt9VjHZMmUukkGhk0IobjQS3hjQ44FiT1tXuUU86oAxqjlKFpXG/iXtpcoXa33IObSI1S3gCKzVPOkMGlHZqRqKKElB54I2Qo4g5CJ+noudIDTzxPFwEEM6XrbM0YBi+SOdRvTbmrlkWF+ndzVWEINoEf++2hkO0gfeCqFqSMHuklMSgeNr/YtFZC5ShJRRv7zbyNF33jZ5DYNVR+KAK9iLO6prZhCVUkZxb1/BjOze6aN7kyN01u3nurKX6n3yQsoQQ0anDW6gNLKzO/mCvoCEvgecjaOQarktl/xYtD4YvdTTlnAlv2bfcXUtc++3UPIUbzf/jpf2g2wf6BGomzFteyPDu4USjBdpeWMBz9PxVzlVpDAtBYClFH/PFEQHMDtL5Q+VxUPu52XlzlUreMHpLT9EL92xwCAwVBBhrarQQWuLjAQXkp3oBdw6hlX6Fj0AafMJuGkFrYzcD7nNr61l9ErZmTWnqTxkJWZfZxmYBsFgV35SKc8rkRSHBNjcdKJZVN4GA+ZQH5B55mi4=");
//				texture.setString("Value", "eyJ0aW1lc3RhbXAiOjE0OTMwNDkwMTcxNTIsInByb2ZpbGVJZCI6ImZjZTAzMjNkN2Y1MDQzMTc5NzIwNWY2YjE0Y2Y3OGVhIiwicHJvZmlsZU5hbWUiOiJ0cjd6dyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI3NDZlNWU5OGMwZWRmZTU1YTI3ZGRjNjUxMmJmNjllYzJiYmNlNmM3ZmNhNTQ5YmEzNjZkYThiNTRjZTRkYiJ9fX0=");
			}

			if (meta.getItemFlags().size() > 0) {
				int flags = 0;
				for (ItemFlag itemFlag: meta.getItemFlags()) {
					switch (itemFlag) {
						case HIDE_ENCHANTS:
							flags += 1;
							break;
						case HIDE_ATTRIBUTES:
							flags += 2;
							break;
						case HIDE_UNBREAKABLE:
							flags += 4;
							break;
						case HIDE_DESTROYS:
							flags += 8;
							break;
						case HIDE_PLACED_ON:
							flags += 16;
							break;
						case HIDE_POTION_EFFECTS:
							flags += 32;
							break;
					}
				}
				nbtItem.setInteger("HideFlags", flags);
			}

			//TODO N/A CanDestroy
			//TODO N/A PickupDelay
			//TODO N/A Age

			if (meta instanceof BookMeta && ((BookMeta) meta).getGeneration() != null) {
				switch (((BookMeta) meta).getGeneration()) {
					case ORIGINAL:
						nbtItem.setString("generation", "Original");
						break;
					case COPY_OF_ORIGINAL:
						nbtItem.setString("generation", "Copy of Original");
						break;
					case COPY_OF_COPY:
						nbtItem.setString("generation", "Copy of Copy");
						break;
					case TATTERED:
						nbtItem.setString("generation", "Tattered");
						break;
				}
			}
		}

		return nbtItem;
	}

	private static String getAsYaml(String jsonString, String name) throws JsonProcessingException {
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
