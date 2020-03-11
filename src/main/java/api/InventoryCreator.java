package api;

import com.belka.spigot.gm4.MainClass;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryCreator {

	private MainClass mc;

	public InventoryCreator(MainClass mc) {
		this.mc = mc;
	}

	public ItemStack createGuiItem(Material mat, ChatColor color, String name, boolean bold, String... lores) {
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName((bold ? (color + "" + ChatColor.BOLD) : color) + name);
		ArrayList<String> loreArr = new ArrayList<>();
		ChatColor lcc = ChatColor.GRAY;
		for(String lore : lores) {
			loreArr.add(lcc + lore);
		}
		meta.setLore(loreArr);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack createSPGuiSkull(EntityType mob, Integer amount, Player p) {
		String name = Helper.capitalize(mob.name().toLowerCase().replace('_', ' '));
		String url = "http://textures.minecraft.net/texture/";
		switch (mob) {
			case CREEPER:
				url += "f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952";
				break;
			case SKELETON:
				url += "301268e9c492da1f0d88271cb492a4b302395f515a7bbf77f4a20b95fc02eb2";
				break;
			case SPIDER:
				url += "cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1";
				break;
			case ZOMBIE:
				url += "56fc854bb84cf4b7697297973e02b79bc10698460b51a639c60e5e417734e11";
				break;
			case SLIME:
				url += "895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c";
				break;
			case ENDERMAN:
				url += "7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf";
				break;
			case WITCH:
				url += "20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa";
				break;
			case GUARDIAN:
				url += "a0bf34a71e7715b6ba52d5dd1bae5cb85f773dc9b0d457b4bfc5f9dd3cc7c94";
				break;
			case CAVE_SPIDER:
				url += "41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224";
				break;
			case SILVERFISH:
				url += "da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540";
				break;
			case ENDERMITE:
				url += "5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e";
				break;


			case GHAST:
				url += "8b6a72138d69fbbd2fea3fa251cabd87152e4f1c97e5f986bf685571db3cc0";
				break;
			case MAGMA_CUBE:
				url += "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429";
				break;
			case PIG_ZOMBIE:
				name = "Zombie Pigman";
				url += "95fb2df754c98b742d35e7b81a1eeac9d37c69fc8cfecd3e91c67983516f";
				break;
			case BLAZE:
				url += "b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0";
				break;

			case COW:
				url += "7dfa0ac37baba2aa290e4faee419a613cd6117fa568e709d90374753c032dcb0";
				break;
			case MUSHROOM_COW:
				name = "Mooshroom";
				url += "d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db";
				break;
			case SHEEP:
				url += "f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70";
				break;
			case PIG:
				url += "621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4";
				break;
			case CHICKEN:
				url += "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893";
				break;
			case RABBIT:
				url += "ffecc6b5e6ea5ced74c46e7627be3f0826327fba26386c6cc7863372e9bc";
				break;
			case HORSE:
				url += "7bb4b288991efb8ca0743beccef31258b31d39f24951efb1c9c18a417ba48f9";
				break;
			case OCELOT:
				url += "5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1";
				break;
			case WOLF:
				url += "69d1d3113ec43ac2961dd59f28175fb4718873c6c448dfca8722317d67";
				break;
			case SQUID:
				url += "01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac";
				break;
			case VILLAGER:
				url += "822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b";
				break;
		}
		ItemStack item = SkullCreator.itemFromUrl(url);
		ItemMeta meta = item.getItemMeta();
		int stat = mc.storage.data().getInt("SoulProbes." + p.getUniqueId() + "." + mob.name(), 0);
		meta.setDisplayName(ChatColor.DARK_GRAY + "" + stat + "/" + amount);
		if (stat >= amount) {
			meta.setDisplayName(ChatColor.GOLD + "" + stat + "/" + amount);
		}
		ArrayList<String> loreArr = new ArrayList<>(Arrays.asList(ChatColor.DARK_AQUA + name, ChatColor.GRAY + "Create " + name + " Spawn Egg"));
		meta.setLore(loreArr);
		item.setItemMeta(meta);
		return item;
	}
}
