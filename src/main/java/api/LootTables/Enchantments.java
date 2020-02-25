package api.LootTables;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Enchantments {//Made from https://minecraft.gamepedia.com/Mechanics/Enchanting#How_enchantments_are_chosen

	public static ItemStack enchant(ItemStack item, int level, boolean treasure) {
		if (item.getType() == Material.BOOK) item.setType(Material.ENCHANTED_BOOK);
		int enchantability = getEnchantability(item.getType());
		int randEnchantability = 1 + LootTable.getRandomInRange(0, enchantability / 4) + LootTable.getRandomInRange(0, enchantability / 4);
		int k = level + randEnchantability;//Choose the enchantment level
		float randBonusPercent = (float) (1 + (randomFloat() + randomFloat() - 1) * 0.15);//A random bonus, between .85 and 1.15
		int modifiedLevel = Math.round(k * randBonusPercent);//Finally, we calculate the level
		if (modifiedLevel < 1) modifiedLevel = 1;

		Map<Enchantment, Integer> possibleEnchantments = getPossibleEnchantments(item, modifiedLevel, treasure);
		if (possibleEnchantments.size() == 0) return item;
		WeightedRandom<Enchantment> weighted = getWeighted(new ArrayList<>(possibleEnchantments.keySet()));
		Enchantment e = weighted.getRandom();
		if (item.getType() != Material.ENCHANTED_BOOK)
			item.addEnchantment(e, possibleEnchantments.get(e));
		else {
			EnchantmentStorageMeta esm = (EnchantmentStorageMeta) item.getItemMeta();
			assert esm != null;
			esm.addStoredEnchant(e, possibleEnchantments.get(e), true);
			item.setItemMeta(esm);
		}
		possibleEnchantments.remove(e);
		if (possibleEnchantments.size() > 0) {
			Random r = new Random();
			double mLevel = modifiedLevel;
			boolean go = r.nextDouble() < ((mLevel + 1) / 50.0);
			while (go) {
				possibleEnchantments = possibleEnchantments.entrySet().stream().filter(ench -> !conflicts(ench.getKey(), item)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

				WeightedRandom<Enchantment> weighted2 = getWeighted(new ArrayList<>(possibleEnchantments.keySet()));
				e = weighted2.getRandom();
				if (item.getType() != Material.ENCHANTED_BOOK)
					item.addEnchantment(e, possibleEnchantments.get(e));
				else {
					EnchantmentStorageMeta esm = (EnchantmentStorageMeta) item.getItemMeta();
					assert esm != null;
					esm.addStoredEnchant(e, possibleEnchantments.get(e), true);
					item.setItemMeta(esm);
				}
				possibleEnchantments.remove(e);

				if (possibleEnchantments.size() == 0) break;
				mLevel = Math.floor(mLevel / 2);
				go = r.nextDouble() < ((mLevel + 1) / 50.0);
			}
		}
		if (item.getType() == Material.ENCHANTED_BOOK) {
			EnchantmentStorageMeta esm = (EnchantmentStorageMeta) item.getItemMeta();
			assert esm != null;
			Bukkit.broadcastMessage(esm.getStoredEnchants().size() + "book");
			if (esm.getStoredEnchants().size() == 0)
				item.setType(Material.BOOK);
		}
		return item;
	}

	private static int getEnchantability(Material mat) {
		switch (mat) {
			default: return 0;
			case WOODEN_AXE:
			case WOODEN_HOE:
			case WOODEN_PICKAXE:
			case WOODEN_SHOVEL:
			case WOODEN_SWORD:
			case LEATHER_BOOTS:
			case LEATHER_CHESTPLATE:
			case LEATHER_HELMET:
			case LEATHER_LEGGINGS:
				return 15;
			case STONE_AXE:
			case STONE_HOE:
			case STONE_PICKAXE:
			case STONE_SHOVEL:
			case STONE_SWORD:
				return 5;
			case IRON_BOOTS:
			case IRON_CHESTPLATE:
			case IRON_HELMET:
			case IRON_LEGGINGS:
				return 9;
			case IRON_AXE:
			case IRON_HOE:
			case IRON_PICKAXE:
			case IRON_SHOVEL:
			case IRON_SWORD:
				return 14;
			case CHAINMAIL_BOOTS:
			case CHAINMAIL_CHESTPLATE:
			case CHAINMAIL_HELMET:
			case CHAINMAIL_LEGGINGS:
				return 12;
			case DIAMOND_BOOTS:
			case DIAMOND_CHESTPLATE:
			case DIAMOND_HELMET:
			case DIAMOND_LEGGINGS:
			case DIAMOND_AXE:
			case DIAMOND_HOE:
			case DIAMOND_PICKAXE:
			case DIAMOND_SHOVEL:
			case DIAMOND_SWORD:
				return 10;
			case GOLDEN_BOOTS:
			case GOLDEN_CHESTPLATE:
			case GOLDEN_HELMET:
			case GOLDEN_LEGGINGS:
				return 25;
			case GOLDEN_AXE:
			case GOLDEN_HOE:
			case GOLDEN_PICKAXE:
			case GOLDEN_SHOVEL:
			case GOLDEN_SWORD:
				return 22;
			case BOW:
			case FISHING_ROD:
			case BOOK:
				return 1;
		}
	}

	private static Map<Enchantment, Integer> getPossibleEnchantments(ItemStack item, int level, boolean treasure) {
		Map<Enchantment, Integer> enchantments = new HashMap<>();
		for (Enchantment ench: Enchantment.values()) {
			if (ench.canEnchantItem(item) || item.getType() == Material.ENCHANTED_BOOK) {
				int powerLevel = getLevel(ench, level);
				if (powerLevel >= ench.getStartLevel() && powerLevel <= ench.getMaxLevel()) {
					if (!ench.isTreasure() || treasure) {
						enchantments.put(ench, powerLevel);
					}
				}
			}
		}
		return enchantments;
	}
	private static int getLevel(Enchantment enchantment, int l) {//https://minecraft.gamepedia.com/Enchanting/Levels
		List<Integer> levels = new ArrayList<>();
		//ARMOR ENCHANTMENT
		if (enchantment.equals(Enchantment.PROTECTION_ENVIRONMENTAL)) {//Protection
			if (l>= 1 && l<= 12) levels.add(1);
			if (l>= 12 && l<= 23) levels.add(2);
			if (l>= 23 && l<= 34) levels.add(3);
			if (l>= 34 && l<= 45) levels.add(4);
		}
		else if (enchantment.equals(Enchantment.PROTECTION_FIRE)) {//Fire Protection
			if (l>= 10 && l<= 18) levels.add(1);
			if (l>= 18 && l<= 26) levels.add(2);
			if (l>= 26 && l<= 34) levels.add(3);
			if (l>= 34 && l<= 42) levels.add(4);
		}
		else if (enchantment.equals(Enchantment.PROTECTION_FALL)) {//Feather Falling
			if (l>= 5 && l<= 11) levels.add(1);
			if (l>= 11 && l<= 17) levels.add(2);
			if (l>= 17 && l<= 23) levels.add(3);
			if (l>= 23 && l<= 29) levels.add(4);
		}
		else if (enchantment.equals(Enchantment.PROTECTION_EXPLOSIONS)) {//Blast Protection
			if (l>= 5 && l<= 13) levels.add(1);
			if (l>= 13 && l<= 21) levels.add(2);
			if (l>= 21 && l<= 29) levels.add(3);
			if (l>= 29 && l<= 37) levels.add(4);
		}
		else if (enchantment.equals(Enchantment.PROTECTION_PROJECTILE)) {//Projectile Protection
			if (l>= 3 && l<= 9) levels.add(1);
			if (l>= 9 && l<= 15) levels.add(2);
			if (l>= 15 && l<= 21) levels.add(3);
			if (l>= 21 && l<= 27) levels.add(4);
		}
		else if (enchantment.equals(Enchantment.OXYGEN)) {//Respiration
			if (l>= 10 && l<= 14) levels.add(1);
			if (l>= 20 && l<= 50) levels.add(2);
			if (l>= 30 && l<= 60) levels.add(3);
		}
		else if (enchantment.equals(Enchantment.WATER_WORKER)) {//Aqua Affinity
			if (l>= 1 && l<= 41) levels.add(1);
		}
		else if (enchantment.equals(Enchantment.THORNS)) {//Thorns
			if (l>= 10 && l<= 61) levels.add(1);
			if (l>= 30 && l<= 71) levels.add(2);
			if (l>= 50 && l<= 81) levels.add(3);
		}
		else if (enchantment.equals(Enchantment.DEPTH_STRIDER)) {//Depth Strider
			if (l>= 10 && l<= 25) levels.add(1);
			if (l>= 20 && l<= 35) levels.add(2);
			if (l>= 30 && l<= 45) levels.add(3);
		}
		else if (enchantment.equals(Enchantment.FROST_WALKER)) {//Frost Walker
			if (l>= 10 && l<= 25) levels.add(1);
		}
		else if (enchantment.equals(Enchantment.BINDING_CURSE)) {//Curse of Binding
			if (l>= 25 && l<= 50) levels.add(1);
		}
		//SWORD ENCHANTMENT
		else if (enchantment.equals(Enchantment.DAMAGE_ALL)) {//Sharpness
			if (l>= 1 && l<= 21) levels.add(1);
			if (l>= 12 && l<= 32) levels.add(2);
			if (l>= 23 && l<= 43) levels.add(3);
			if (l>= 34 && l<= 54) levels.add(4);
			if (l>= 45 && l<= 65) levels.add(5);
		}
		else if (enchantment.equals(Enchantment.DAMAGE_UNDEAD) ||//Smite
				enchantment.equals(Enchantment.DAMAGE_ARTHROPODS)) {//Bane of Athropods
			if (l>= 5 && l<= 25) levels.add(1);
			if (l>= 13 && l<= 33) levels.add(2);
			if (l>= 21 && l<= 41) levels.add(3);
			if (l>= 29 && l<= 49) levels.add(4);
			if (l>= 37 && l<= 57) levels.add(5);
		}
		else if (enchantment.equals(Enchantment.KNOCKBACK)) {//Knockback
			if (l>= 5 && l<= 61) levels.add(1);
			if (l>= 25 && l<= 71) levels.add(2);
		}
		else if (enchantment.equals(Enchantment.FIRE_ASPECT)) {//Fire Aspect
			if (l>= 10 && l<= 61) levels.add(1);
			if (l>= 30 && l<= 71) levels.add(2);
		}
		else if (enchantment.equals(Enchantment.LOOT_BONUS_MOBS)) {//Looting
			if (l>= 15 && l<= 61) levels.add(1);
			if (l>= 24 && l<= 71) levels.add(2);
			if (l>= 33 && l<= 81) levels.add(3);
		}
		else if (enchantment.equals(Enchantment.SWEEPING_EDGE)) {//Sweeping Edge
			if (l>= 5 && l<= 20) levels.add(1);
			if (l>= 14 && l<= 29) levels.add(2);
			if (l>= 23 && l<= 38) levels.add(3);
		}
		//BOW ENCHANTMENT
		else if (enchantment.equals(Enchantment.ARROW_DAMAGE)) {//Power
			if (l>= 1 && l<= 16) levels.add(1);
			if (l>= 11 && l<= 26) levels.add(2);
			if (l>= 21 && l<= 36) levels.add(3);
			if (l>= 31 && l<= 46) levels.add(4);
			if (l>= 41 && l<= 56) levels.add(5);
		}
		else if (enchantment.equals(Enchantment.ARROW_KNOCKBACK)) {//Punch
			if (l>= 12 && l<= 37) levels.add(1);
			if (l>= 32 && l<= 57) levels.add(2);
		}
		else if (enchantment.equals(Enchantment.ARROW_FIRE) ||//Flame
				enchantment.equals(Enchantment.ARROW_INFINITE)) {//Infinity
			if (l>= 20 && l<= 50) levels.add(1);
		}
		//TOOL ENCHANTMENT
		else if (enchantment.equals(Enchantment.DIG_SPEED)) {//Efficiency
			if (l>= 1 && l<= 61) levels.add(1);
			if (l>= 11 && l<= 71) levels.add(2);
			if (l>= 21 && l<= 81) levels.add(3);
			if (l>= 31 && l<= 91) levels.add(4);
			if (l>= 41 && l<= 101) levels.add(5);
		}
		else if (enchantment.equals(Enchantment.SILK_TOUCH)) {//Silk Touch
			if (l>= 15 && l<= 61) levels.add(1);
		}
		else if (enchantment.equals(Enchantment.LOOT_BONUS_BLOCKS)) {//Fortune
			if (l>= 15 && l<= 61) levels.add(1);
			if (l>= 24 && l<= 71) levels.add(2);
			if (l>= 33 && l<= 81) levels.add(3);
		}
		//FISHING ROD ENCHANTMENT
		else if (enchantment.equals(Enchantment.LUCK) ||//Luck of the Sea
				enchantment.equals(Enchantment.LURE)) {//Lure
			if (l>= 15 && l<= 61) levels.add(1);
			if (l>= 24 && l<= 71) levels.add(2);
			if (l>= 33 && l<= 81) levels.add(3);
		}
		//"ANYTHING" ENCHANTMENT
		else if (enchantment.equals(Enchantment.DURABILITY)) {//Unbreaking
			if (l>= 5 && l<= 61) levels.add(1);
			if (l>= 13 && l<= 71) levels.add(2);
			if (l>= 21 && l<= 81) levels.add(3);
		}
		else if (enchantment.equals(Enchantment.MENDING)) {//Mending
			if (l>= 25 && l<= 75) levels.add(1);
		}
		else if (enchantment.equals(Enchantment.VANISHING_CURSE)) {//Curse of vanishing
			if (l>= 25 && l<= 50) levels.add(1);
		}
		//TRIDENT ENCHANTMENT
		else if (enchantment.equals(Enchantment.CHANNELING)) {//Channeling
			if (l>= 25 && l<= 50) levels.add(1);
		}
		else if (enchantment.equals(Enchantment.IMPALING)) {//Impaling
			if (l>= 1 && l<= 21) levels.add(1);
			if (l>= 9 && l<= 29) levels.add(2);
			if (l>= 17 && l<= 37) levels.add(3);
			if (l>= 25 && l<= 45) levels.add(4);
			if (l>= 33 && l<= 53) levels.add(5);
		}
		else if (enchantment.equals(Enchantment.LOYALTY)) {//Loyalty
			if (l>= 12 && l<= 50) levels.add(1);
			if (l>= 19 && l<= 50) levels.add(2);
			if (l>= 26 && l<= 50) levels.add(3);
		}
		else if (enchantment.equals(Enchantment.RIPTIDE)) {//Riptide
			if (l>= 17 && l<= 50) levels.add(1);
			if (l>= 24 && l<= 50) levels.add(2);
			if (l>= 31 && l<= 50) levels.add(3);
		}
		//CROSSBOW ENCHANTMENT
		else if (enchantment.equals(Enchantment.MULTISHOT)) {//Multishot
			if (l>= 20 && l<= 50) levels.add(1);
		}
		else if (enchantment.equals(Enchantment.PIERCING)) {//Piercing
			if (l>= 1 && l<= 50) levels.add(1);
			if (l>= 11 && l<= 50) levels.add(2);
			if (l>= 21 && l<= 50) levels.add(3);
			if (l>= 31 && l<= 50) levels.add(4);
		}
		else if (enchantment.equals(Enchantment.QUICK_CHARGE)) {//Quick Charge
			if (l>= 12 && l<= 50) levels.add(1);
			if (l>= 32 && l<= 50) levels.add(2);
			if (l>= 50 && l<= 52) levels.add(3);
		}
		if (levels.size() == 0) return 0;
		else if (levels.size() == 1) return levels.get(0);
		else return levels.get(levels.size() - 1);
	}
	
	private static WeightedRandom<Enchantment> getWeighted(List<Enchantment> list) {
		WeightedRandom<Enchantment> weighted = new WeightedRandom<>();
		if (list.contains(Enchantment.PROTECTION_ENVIRONMENTAL)) {//Protection
			weighted.addEntry(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
		}
		if (list.contains(Enchantment.PROTECTION_FIRE)) {//Fire Protection
			weighted.addEntry(Enchantment.PROTECTION_FIRE, 5);
		}
		if (list.contains(Enchantment.PROTECTION_FALL)) {//Feather Falling
			weighted.addEntry(Enchantment.PROTECTION_FALL, 5);
		}
		if (list.contains(Enchantment.PROTECTION_EXPLOSIONS)) {//Blast Protection
			weighted.addEntry(Enchantment.PROTECTION_EXPLOSIONS, 2);
		}
		if (list.contains(Enchantment.PROTECTION_PROJECTILE)) {//Projectile Protection
			weighted.addEntry(Enchantment.PROTECTION_PROJECTILE, 5);
		}
		if (list.contains(Enchantment.OXYGEN)) {//Respiration
			weighted.addEntry(Enchantment.OXYGEN, 2);
		}
		if (list.contains(Enchantment.WATER_WORKER)) {//Aqua Affinity
			weighted.addEntry(Enchantment.WATER_WORKER, 2);
		}
		if (list.contains(Enchantment.THORNS)) {//Thorns
			weighted.addEntry(Enchantment.THORNS, 1);
		}
		if (list.contains(Enchantment.DEPTH_STRIDER)) {//Depth Strider
			weighted.addEntry(Enchantment.DEPTH_STRIDER, 2);
		}
		if (list.contains(Enchantment.FROST_WALKER)) {//Frost Walker
			weighted.addEntry(Enchantment.FROST_WALKER, 2);
		}
		if (list.contains(Enchantment.BINDING_CURSE)) {//Curse of Binding
			weighted.addEntry(Enchantment.BINDING_CURSE, 1);
		}
		//SWORD ENCHANTMENT
		if (list.contains(Enchantment.DAMAGE_ALL)) {//Sharpness
			weighted.addEntry(Enchantment.DAMAGE_ALL, 10);
		}
		if (list.contains(Enchantment.DAMAGE_UNDEAD)) {//Smite
			weighted.addEntry(Enchantment.DAMAGE_UNDEAD, 5);
		}
		if (list.contains(Enchantment.DAMAGE_ARTHROPODS)) {//Bane of Athropods
			weighted.addEntry(Enchantment.DAMAGE_ARTHROPODS, 5);
		}
		if (list.contains(Enchantment.KNOCKBACK)) {//Knockback
			weighted.addEntry(Enchantment.KNOCKBACK, 5);
		}
		if (list.contains(Enchantment.FIRE_ASPECT)) {//Fire Aspect
			weighted.addEntry(Enchantment.FIRE_ASPECT, 2);
		}
		if (list.contains(Enchantment.LOOT_BONUS_MOBS)) {//Looting
			weighted.addEntry(Enchantment.LOOT_BONUS_MOBS, 2);
		}
		if (list.contains(Enchantment.SWEEPING_EDGE)) {//Sweeping Edge
			weighted.addEntry(Enchantment.SWEEPING_EDGE, 2);
		}
		//BOW ENCHANTMENT
		if (list.contains(Enchantment.ARROW_DAMAGE)) {//Power
			weighted.addEntry(Enchantment.ARROW_DAMAGE, 10);
		}
		if (list.contains(Enchantment.ARROW_KNOCKBACK)) {//Punch
			weighted.addEntry(Enchantment.ARROW_KNOCKBACK, 2);
		}
		if (list.contains(Enchantment.ARROW_FIRE)) {//Flame
			weighted.addEntry(Enchantment.ARROW_FIRE, 2);
		}
		if (list.contains(Enchantment.ARROW_INFINITE)) {//Infinity
			weighted.addEntry(Enchantment.ARROW_INFINITE, 1);
		}
		//TOOL ENCHANTMENT
		if (list.contains(Enchantment.DIG_SPEED)) {//Efficiency
			weighted.addEntry(Enchantment.DIG_SPEED, 10);
		}
		if (list.contains(Enchantment.SILK_TOUCH)) {//Silk Touch
			weighted.addEntry(Enchantment.SILK_TOUCH, 1);
		}
		if (list.contains(Enchantment.LOOT_BONUS_BLOCKS)) {//Fortune
			weighted.addEntry(Enchantment.LOOT_BONUS_BLOCKS, 2);
		}
		//FISHING ROD ENCHANTMENT
		if (list.contains(Enchantment.LUCK)) {//Luck of the Sea
			weighted.addEntry(Enchantment.LUCK, 2);
		}
		if (list.contains(Enchantment.LURE)) {//Lure
			weighted.addEntry(Enchantment.LURE, 2);
		}
		//"ANYTHING" ENCHANTMENT
		if (list.contains(Enchantment.DURABILITY)) {//Unbreaking
			weighted.addEntry(Enchantment.DURABILITY, 5);
		}
		if (list.contains(Enchantment.MENDING)) {//Mending
			weighted.addEntry(Enchantment.MENDING, 2);
		}
		if (list.contains(Enchantment.VANISHING_CURSE)) {//Curse of vanishing
			weighted.addEntry(Enchantment.VANISHING_CURSE, 1);
		}
		//TRIDENT ENCHANTMENT
		if (list.contains(Enchantment.CHANNELING)) {//Channeling
			weighted.addEntry(Enchantment.CHANNELING, 1);
		}
		if (list.contains(Enchantment.IMPALING)) {//Impaling
			weighted.addEntry(Enchantment.IMPALING, 2);
		}
		if (list.contains(Enchantment.LOYALTY)) {//Loyalty
			weighted.addEntry(Enchantment.LOYALTY, 5);
		}
		if (list.contains(Enchantment.RIPTIDE)) {//Riptide
			weighted.addEntry(Enchantment.RIPTIDE, 2);
		}
		//CROSSBOW ENCHANTMENT
		if (list.contains(Enchantment.MULTISHOT)) {//Multishot
			weighted.addEntry(Enchantment.MULTISHOT, 3);
		}
		if (list.contains(Enchantment.PIERCING)) {//Piercing
			weighted.addEntry(Enchantment.PIERCING, 30);
		}
		if (list.contains(Enchantment.QUICK_CHARGE)) {//Quick Charge
			weighted.addEntry(Enchantment.QUICK_CHARGE, 10);
		}
		return weighted;
	}

	private static boolean conflicts(Enchantment enchantment, ItemStack item) {
		if (item.getEnchantments().size() == 0) return false;
		for (Enchantment e: item.getEnchantments().keySet()) {
			if (enchantment.conflictsWith(e)) return true;
		}
		return false;
	}

	private static float randomFloat() {
		return new Random().nextFloat();
	}
}
