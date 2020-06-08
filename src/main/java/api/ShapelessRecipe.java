package api;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.Validate;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShapelessRecipe implements Recipe, Keyed {

	private final NamespacedKey key;
	private final ItemStack output;
	private final List<ItemStack> ingredients = new ArrayList<>();
	private String group = "";

	@Deprecated
	public ShapelessRecipe(@NotNull ItemStack result) {
		Preconditions.checkArgument(result.getType() != Material.AIR, "Recipe must have non-AIR result.");
		this.key = NamespacedKey.randomKey();
		this.output = new ItemStack(result);
	}

	public ShapelessRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result) {
		Preconditions.checkArgument(result.getType() != Material.AIR, "Recipe must have non-AIR result.");
		this.key = key;
		this.output = new ItemStack(result);
	}

	@NotNull
	public ShapelessRecipe addIngredient(@NotNull Material ingredient) {
		return addIngredient(1, ingredient);
	}

	@NotNull
	public ShapelessRecipe addIngredient(int count, @NotNull Material ingredient) {
		return addIngredient(new ItemStack(ingredient, count));
	}

	@NotNull
	public ShapelessRecipe addIngredient(@NotNull ItemStack ingredient) {
		Validate.isTrue(this.ingredients.size() + 1 <= 9, "Shapeless recipes cannot have more than 9 ingredients");

		this.ingredients.add(ingredient);
		return this;
	}

	@NotNull
	public ShapelessRecipe removeIngredient(@NotNull ItemStack ingredient) {
		this.ingredients.remove(ingredient);

		return this;
	}

	@NotNull
	public ShapelessRecipe removeIngredient(@NotNull Material ingredient) {
		return removeIngredient(ingredient, 0);
	}

	@NotNull
	public ShapelessRecipe removeIngredient(@NotNull MaterialData ingredient) {
		return removeIngredient(ingredient.getItemType(), ingredient.getData());
	}

	@NotNull
	public ShapelessRecipe removeIngredient(int count, @NotNull Material ingredient) {
		return removeIngredient(count, ingredient, 0);
	}

	@NotNull
	public ShapelessRecipe removeIngredient(int count, @NotNull MaterialData ingredient) {
		return removeIngredient(count, ingredient.getItemType(), ingredient.getData());
	}

	@NotNull
	@Deprecated
	public ShapelessRecipe removeIngredient(@NotNull Material ingredient, int rawdata) {
		return removeIngredient(1, ingredient, rawdata);
	}

	@NotNull
	@Deprecated
	public ShapelessRecipe removeIngredient(int count, @NotNull Material ingredient, int rawdata) {
		Iterator<ItemStack> iterator = this.ingredients.iterator();
		while ((count > 0) && (iterator.hasNext()))
		{
			ItemStack stack = iterator.next();
			if ((stack.getType() == ingredient) && (stack.getDurability() == rawdata))
			{
				iterator.remove();
				count--;
			}
		}
		return this;
	}

	@NotNull
	public ItemStack getResult() {
		return this.output.clone();
	}

	@NotNull
	public List<ItemStack> getIngredientList() {
		ArrayList<ItemStack> result = new ArrayList<>(this.ingredients.size());
		for (ItemStack ingredient : this.ingredients) {
			result.add(ingredient.clone());
		}
		return result;
	}

	@NotNull
	public List<ItemStack> getChoiceList() {
		List<ItemStack> result = new ArrayList<>(this.ingredients.size());
		for (ItemStack ingredient : this.ingredients) {
			result.add(ingredient.clone());
		}
		return result;
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
