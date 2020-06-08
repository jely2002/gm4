package api;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.Validate;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShapedRecipe implements Recipe, Keyed {

	private final NamespacedKey key;
	private final ItemStack output;
	private String[] rows;
	private Map<Character, ItemStack> ingredients = new HashMap<>();
	private String group;

	public ShapedRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result) {
		Preconditions.checkArgument(key != null, "key");
		Preconditions.checkArgument(result.getType() != Material.AIR, "Recipe must have non-AIR result.");

		this.key = key;
		this.output = new ItemStack(result);
	}

	@NotNull
	public ShapedRecipe shape(@NotNull String... shape) {
		Validate.notNull(shape, "Must provide a shape");
		Validate.isTrue((shape.length > 0) && (shape.length < 4), "Crafting recipes should be 1, 2 or 3 rows, not ", shape.length);

		int lastLen = -1;
		String[] arrayOfString1;
		int j = (arrayOfString1 = shape).length;
		for (int i = 0; i < j; i++) {
			String row = arrayOfString1[i];
			Validate.notNull(row, "Shape cannot have null rows");
			Validate.isTrue((row.length() > 0) && (row.length() < 4), "Crafting rows should be 1, 2, or 3 characters, not ", row.length());

			Validate.isTrue((lastLen == -1) || (lastLen == row.length()), "Crafting recipes must be rectangular");
			lastLen = row.length();
		}
		this.rows = new String[shape.length];
		System.arraycopy(shape, 0, this.rows, 0, shape.length);
		HashMap<Character, ItemStack> newIngredients = new HashMap<>();
		String[] arrayOfString2;
		int k = (arrayOfString2 = shape).length;
		for (j = 0; j < k; j++) {
			String row = arrayOfString2[j];
			char[] arrayOfChar;
			int n = (arrayOfChar = row.toCharArray()).length;
			for (int m = 0; m < n; m++) {
				Character c = arrayOfChar[m];
				newIngredients.put(c, this.ingredients.getOrDefault(c, null));
			}
		}
		this.ingredients = newIngredients;

		return this;
	}

	@NotNull
	public ShapedRecipe setIngredient(char key, @NotNull ItemStack ingredient) {
		Validate.isTrue(this.ingredients.containsKey(key), "Symbol does not appear in the shape:", key);
		this.ingredients.put(key, ingredient);
		return this;
	}

	@NotNull
	public ShapedRecipe setIngredient(char key, @NotNull Material ingredient) {
		Validate.isTrue(this.ingredients.containsKey(key), "Symbol does not appear in the shape:", key);
		this.ingredients.put(key, new ItemStack(ingredient));
		return this;
	}

	@NotNull
	public Map<Character, ItemStack> getIngredientMap() {
		HashMap<Character, ItemStack> result = new HashMap<>();
		for (Map.Entry<Character, ItemStack> ingredient : this.ingredients.entrySet()) {
			if (ingredient.getValue() == null) result.put(ingredient.getKey(), null);
			else result.put(ingredient.getKey(), ingredient.getValue().clone());
		}
		return result;
	}

	@NotNull
	public String[] getShape() {
		return this.rows.clone();
	}

	@NotNull
	public ItemStack getResult() {
		return this.output.clone();
	}

	@NotNull
	public NamespacedKey getKey() {
		return this.key;
	}

	@NotNull
	public String getGroup() {
		return this.group;
	}

	public void setGroup(@NotNull String group) {
		Preconditions.checkArgument(group != null, "group");
		this.group = group;
	}
}
