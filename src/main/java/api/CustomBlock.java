package api;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Module;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SerializableAs("CustomBlock")
public class CustomBlock implements ConfigurationSerializable, Module {

	private static MainClass mc;
	public CustomBlock(MainClass mc) {
		CustomBlock.mc = mc;
	}

	private CustomBlockType type;//Required
	private Location location;//Required
	private UUID uuid;
	private BlockFace direction;
	private Boolean active;

	@NotNull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();

		map.put("type", type.getId());
		map.put("location", location);
		if (uuid != null) map.put("uuid", uuid.toString());
		if (direction != null) map.put("direction", direction.name());
		if (active != null) map.put("active", active);

		return map;
	}
	public static CustomBlock deserialize(Map<String, Object> map) {
		return new CustomBlock(map);
	}

	public void init(MainClass mc) {
		if (mc.getStorage().data().contains("CustomBlocks")) {
			List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
			assert customBlockList != null;
			for (CustomBlock cb : customBlockList) {
				if (cb.hasEntity()) {
					Entity e = Bukkit.getEntity(cb.getUuid());
					if (e != null)
						e.setFireTicks(Integer.MAX_VALUE);
//					Bukkit.broadcastMessage(cb.getType().getName() + " " + cb.getLocation().toString() + " " + cb.getUuid().toString());
				}
			}
		}
	}

	public CustomBlock(@NotNull CustomBlockType type, @NotNull Location location) {
		this.type = type;
		this.location = location;
	}
	public CustomBlock(@NotNull CustomBlockType type, @NotNull Location location, @Nullable UUID uuid) {
		this.type = type;
		this.location = location;
		this.uuid = uuid;
	}
	public CustomBlock(Map<String, Object> map) {
		this.type = CustomBlockType.getById((String) map.get("type"));
		this.location = (Location) map.get("location");
		if (map.containsKey("uuid")) this.uuid = UUID.fromString((String) map.get("uuid"));
		if (map.containsKey("direction")) this.direction = BlockFace.valueOf((String) map.get("direction"));
		if (map.containsKey("active")) this.active = (boolean) map.get("active");
	}

	public CustomBlockType getType() {
		return type;
	}

	public Location getLocation() {
		return location;
	}

	public Block getBlock() {
		return location.getBlock();
	}

	public boolean hasEntity() {
		return uuid != null;
	}
	public UUID getUuid() {
		return uuid;
	}

	public boolean hasDirection() {
		return direction != null;
	}
	public BlockFace getDirection() {
		return direction;
	}
	public void setDirection(BlockFace direction) {
		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		assert customBlockList != null;
		customBlockList.remove(this);

		this.direction = direction;

		customBlockList.add(this);
		mc.getStorage().data().set("CustomBlocks", customBlockList);
		mc.getStorage().saveData();
	}

	public boolean hasActive() {
		return active != null;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		assert customBlockList != null;
		customBlockList.remove(this);

		this.active = active;

		customBlockList.add(this);
		mc.getStorage().data().set("CustomBlocks", customBlockList);
		mc.getStorage().saveData();
	}

	public static CustomBlock create(String type, Location location) {
		CustomBlockType cbt = CustomBlockType.getById(type);
		if (cbt != null) return create(cbt, location);
		else return null;
	}
	public static CustomBlock create(CustomBlockType type, Location location) {
		CustomBlock cb;
		if (type.hasArmorStand()) {
			ArmorStand as = (ArmorStand) Objects.requireNonNull(location.getWorld()).spawnEntity(location.clone().add(type.getOffset()), EntityType.ARMOR_STAND);
			as.setVisible(false);
			as.setSmall(true);
			as.setGravity(false);
			as.setCanPickupItems(false);
			as.setCustomNameVisible(false);
			as.setRemoveWhenFarAway(false);
			as.setMarker(true);
			as.setFireTicks(Integer.MAX_VALUE);
			as.addScoreboardTag("gm4");
			as.setCustomName(type.getId());
			if (type.hasHelmet())
				Objects.requireNonNull(as.getEquipment()).setHelmet(type.getHelmet());

			if (type.hasHeadPose())
				as.setHeadPose(type.getHeadPose());

			handleBlock(location, type);

			cb = new CustomBlock(type, location, as.getUniqueId());
		}
		else {
			handleBlock(location, type);
			cb = new CustomBlock(type, location);
		}

		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		assert customBlockList != null;
		customBlockList.add(cb);
		mc.getStorage().data().set("CustomBlocks", customBlockList);
		mc.getStorage().saveData();

		return cb;
	}

	public void convert(CustomBlockType type) {
		Location loc = this.getLocation().clone();

		if (hasEntity()) {
			Entity e = Bukkit.getEntity(this.getUuid());
			if (e != null) {
				if (e instanceof ArmorStand) {
					ArmorStand as = (ArmorStand) e;
					if (type.hasHeadPose()) {
						as.setHeadPose(type.getHeadPose());
					}
					Objects.requireNonNull(as.getEquipment()).setHelmet(type.getHelmet());

					if (type.hasHeadPose())
						as.setHeadPose(type.getHeadPose());
				}
				e.teleport(loc.clone().add(type.getOffset()));
			}
		}

		handleBlock(loc, type);

		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		assert customBlockList != null;
		customBlockList.remove(this);
		this.type = type;
		customBlockList.add(this);
		mc.getStorage().data().set("CustomBlocks", customBlockList);
		mc.getStorage().saveData();
//		destroy(false);
//		CustomBlock.create(type, loc);
	}

	public void move(Location loc) {
		CustomBlock cb = this;

		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		assert customBlockList != null;
		customBlockList.remove(cb);
		if (hasEntity()) {
			Entity e = Bukkit.getEntity(cb.getUuid());
			if (e != null)
			e.teleport(e.getLocation().add(loc.clone().subtract(this.location.clone())));
		}
		this.location = loc;
		customBlockList.add(cb);

		mc.getStorage().data().set("CustomBlocks", customBlockList);
		mc.getStorage().saveData();
	}

	public void destroy(boolean dropItems) {
		CustomBlock cb = this;
		if (hasEntity())
			Objects.requireNonNull(Bukkit.getEntity(cb.getUuid())).remove();

		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		assert customBlockList != null;
		customBlockList.remove(cb);
		mc.getStorage().data().set("CustomBlocks", customBlockList);
		mc.getStorage().saveData();

		if (dropItems) {
			Location drop = cb.getLocation().clone().add(0.5, 0.5, 0.5);
			for (ItemStack item: cb.getType().getDrops()) {
				if (item != null && drop.getWorld() != null)
					drop.getWorld().dropItem(drop, item);
			}
		}
	}

	public static CustomBlock get(Location location) {
		List<CustomBlock> customBlockList = (List<CustomBlock>) mc.getStorage().data().getList("CustomBlocks");
		if (customBlockList == null) return null;
		return customBlockList.stream().filter(o -> o.getLocation().equals(location)).findFirst().orElse(null);
	}

	private static void handleBlock(Location loc, CustomBlockType type) {
		Block b = loc.getBlock();
		if (b.getType() != type.getType()) b.setType(type.getType());

		if (type.hasConsumer())
			type.getConsumer().accept(b);

		if (b.getState() instanceof Container) {
			Container container = (Container) b.getState();
			container.setCustomName(type.getName());
			container.update();
		}
		if (b.getState() instanceof InventoryHolder) {
			InventoryHolder inventoryHolder = (InventoryHolder) b.getState();
			inventoryHolder.getInventory().clear();
		}
		if (b.getBlockData() instanceof Directional && type != CustomBlockType.ENDER_HOPPER) {
			Directional blockData = (Directional) b.getBlockData();
			blockData.setFacing(BlockFace.DOWN);
			b.setBlockData(blockData);
		}
	}
}
