package api;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Recipe {
	@NotNull
	ItemStack getResult();

	@NotNull
	NamespacedKey getKey();
}
