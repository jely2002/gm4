package api;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTEntity;
import javafx.util.Pair;
import mryurihi.tbnbt.TagType;
import mryurihi.tbnbt.stream.NBTInputStream;
import mryurihi.tbnbt.tag.NBTTag;
import mryurihi.tbnbt.tag.NBTTagCompound;
import mryurihi.tbnbt.tag.NBTTagList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Structure {
	private File file;
	private NBTTagCompound nbt;
	private String name;
	private Vector offset = new Vector();
	private List<EntityType> ignoredEntityTypes = new ArrayList<>();

	public Structure(File file) throws IOException {
		this.file = file;
		this.name = file.getName();
		FileInputStream fileInputStream = new FileInputStream(file);
		NBTInputStream inputStream = new NBTInputStream(fileInputStream);
		this.nbt = inputStream.readTag().getAsTagCompound();
	}
	public Structure(File file, String name) throws IOException {
		this.file = file;
		this.name = name;
		FileInputStream fileInputStream = new FileInputStream(file);
		NBTInputStream inputStream = new NBTInputStream(fileInputStream);
		this.nbt = inputStream.readTag().getAsTagCompound();
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setOffset(Vector offset) {
		this.offset = offset;
	}
	public void setOffset(int offsetX, int offsetY, int offsetZ) {
		this.offset = new Vector(offsetX, offsetY, offsetZ);
	}
	public Vector getOffset() {
		return offset;
	}

	public void setIgnoredEntityTypes(List<EntityType> ignoredEntityTypes) {
		this.ignoredEntityTypes = ignoredEntityTypes;
	}
	public void addIgnoredEntityTypes(List<EntityType> ignoredEntityTypes) {
		this.ignoredEntityTypes.addAll(ignoredEntityTypes);
	}
	public List<EntityType> getIgnoredEntityTypes() {
		return ignoredEntityTypes;
	}

	public Vector getSize() {
		if (nbt.containsKey("size") && nbt.get("size").getAsTagIntArray().getValue().length == 3) {
			int[] size = nbt.get("size").getAsTagIntArray().getValue();
			return new Vector(size[0], size[1], size[2]);
		}
		return null;
	}

	public List<Pair<Vector, HashMap<String, Object>>> getEntities() {
		List<Pair<Vector, HashMap<String, Object>>> list = new ArrayList<>();
		if (!nbt.containsKey("entities")) return null;
		NBTTagList blocks = nbt.get("entities").getAsTagList();
		for (NBTTag nbtTag: blocks.getValue()) {
			NBTTagCompound tag = nbtTag.getAsTagCompound();
			Vector pos = new Vector();
			if (tag.containsKey("pos") && tag.get("pos").getAsTagList().getValue().size() == 3) {
				List<Double> posInt = tag.get("pos").getAsTagList().getValue().stream().map(nbtTag1 -> nbtTag1.getAsTagDouble().getValue()).collect(Collectors.toList());
				pos.setX(posInt.get(0)).setY(posInt.get(1)).setZ(posInt.get(2));
			}
			HashMap<String, Object> map = new HashMap<>();
			if (tag.containsKey("nbt")) {
				for (Map.Entry<String, NBTTag> entry : tag.get("nbt").getAsTagCompound().getValue().entrySet()) {
					map.put(entry.getKey(), getTagValue(entry.getValue()));
				}
			}
			list.add(new Pair<>(pos, map));
		}
		return list;
	}

	public List<Pair<Vector, Integer>> getBlocks() {
		List<Pair<Vector, Integer>> list = new ArrayList<>();
		if (!nbt.containsKey("blocks")) return null;
		NBTTagList blocks = nbt.get("blocks").getAsTagList();
		for (NBTTag nbtTag: blocks.getValue()) {
			NBTTagCompound tag = nbtTag.getAsTagCompound();
			int state = -1;
			if (tag.containsKey("state"))
				state = tag.get("state").getAsTagInt().getValue();
			Vector pos = new Vector();
			if (tag.containsKey("pos") && tag.get("pos").getAsTagList().getValue().size() == 3) {
				List<Integer> posInt = tag.get("pos").getAsTagList().getValue().stream().map(nbtTag1 -> nbtTag1.getAsTagInt().getValue()).collect(Collectors.toList());
				pos.setX(posInt.get(0)).setY(posInt.get(1)).setZ(posInt.get(2));
			}
			list.add(new Pair<>(pos, state));
		}
		return list;
	}
	public List<Pair<Vector, Material>> getBlocksWithMaterial() {
		List<Pair<Vector, Integer>> blocks = getBlocks();
		List<Pair<Material, HashMap<String, Object>>> palette = getPalette();
		List<Pair<Vector, Material>> list = new ArrayList<>();
		for (Pair<Vector, Integer> pair: blocks) {
			list.add(new Pair<>(pair.getKey(), palette.get(pair.getValue()).getKey()));
		}
		return list;
	}

	public List<Pair<Material, HashMap<String, Object>>> getPalette() {
		List<Pair<Material, HashMap<String, Object>>> list = new ArrayList<>();
		if (!nbt.containsKey("palette")) return null;
		NBTTagList palette = nbt.get("palette").getAsTagList();
		for (NBTTag nbtTag: palette.getValue()) {
			NBTTagCompound tag = nbtTag.getAsTagCompound();
			Material mat = null;
			if (tag.containsKey("Name"))
				mat = Material.matchMaterial(tag.get("Name").getAsTagString().getValue());
			HashMap<String, Object> map = new HashMap<>();
			if (tag.containsKey("Properties")) {
				for (Map.Entry<String, NBTTag> entry : tag.get("Properties").getAsTagCompound().getValue().entrySet()) {
					map.put(entry.getKey(), getTagValue(entry.getValue()));
				}
			}
			list.add(new Pair<>(mat, map));
		}
		return list;
	}

	public Integer getDataVersion() {
		if (nbt.containsKey("DataVersion")) return nbt.get("DataVersion").getAsTagInt().getValue();
		return null;
	}

	public void place(Location target, boolean includeEntities) {
		Location loc = target.add(offset);
		List<Pair<Material, HashMap<String, Object>>> palette = getPalette();
		for (Pair<Vector, Integer> block: getBlocks()) {
			Block b = loc.clone().add(block.getKey()).getBlock();
			b.setType(palette.get(block.getValue()).getKey(), true);
			HashMap<String, Object> map = palette.get(block.getValue()).getValue();
			if (!palette.get(block.getValue()).getValue().isEmpty()) {
				setBlockData(b, map);
			}
		}
		if (includeEntities) {
			for (Pair<Vector, HashMap<String, Object>> entityPair: getEntities()) {
				HashMap<String, Object> map = entityPair.getValue();
				map.remove("Pos");
				if (!map.containsKey("id")) continue;
				EntityType entityType = getEntityByName(map.get("id").toString().replace("minecraft:", ""));
				map.remove("id");
				if (entityType == null || ignoredEntityTypes.contains(entityType)) continue;
				Entity entity = loc.getWorld().spawnEntity(loc.clone().add(entityPair.getKey()), entityType);
				NBTEntity nbtEntity = new NBTEntity(entity);
				String nbt = "{";
				int i = 0;
				for (Map.Entry<String, Object> entry: map.entrySet()) {
					if (entry.getValue() instanceof String) nbt += entry.getKey() + ":\"" + entry.getValue() + "\"";
					else nbt += entry.getKey() + ":" + entry.getValue();
					i++;
					if (i != map.size()) nbt += ",";
				}
				nbt += "}";
				nbtEntity.mergeCompound(new NBTContainer(nbt));
			}
		}
	}

	private void setBlockData(Block block, HashMap<String, Object> map) {
//		BlockData blockData = block.getBlockData();
//		for (Map.Entry<String, Object> entry: map.entrySet()) {
//			if (blockData instanceof Directional) {
//				if (entry.getKey().equalsIgnoreCase("facing")) {
//					((Directional) blockData).setFacing(BlockFace.valueOf(entry.getValue().toString().toUpperCase()));
//				}
//			}
//			if (blockData instanceof Waterlogged) {
//				if (entry.getKey().equalsIgnoreCase("waterlogged")) {
//					((Waterlogged) blockData).setWaterlogged(Boolean.parseBoolean(entry.getValue().toString()));
//				}
//			}
//			if (blockData instanceof Orientable) {
//				if (entry.getKey().equalsIgnoreCase("axis")) {
//					((Orientable) blockData).setAxis(Axis.valueOf(entry.getValue().toString().toUpperCase()));
//				}
//			}
//			if (blockData instanceof Levelled) {
//				if (entry.getKey().equalsIgnoreCase("axis")) {
//					((Levelled) blockData).setLevel(Integer.parseInt(entry.getValue().toString()));
//				}
//			}
//			if (blockData instanceof MultipleFacing) {
//				if (entry.getKey().equalsIgnoreCase("north")) {
//					((MultipleFacing) blockData).setFace(BlockFace.NORTH, Boolean.parseBoolean(entry.getValue().toString()));
//				}
//				else if (entry.getKey().equalsIgnoreCase("east")) {
//					((MultipleFacing) blockData).setFace(BlockFace.EAST, Boolean.parseBoolean(entry.getValue().toString()));
//				}
//				else if (entry.getKey().equalsIgnoreCase("south")) {
//					((MultipleFacing) blockData).setFace(BlockFace.SOUTH, Boolean.parseBoolean(entry.getValue().toString()));
//				}
//				else if (entry.getKey().equalsIgnoreCase("west")) {
//					((MultipleFacing) blockData).setFace(BlockFace.WEST, Boolean.parseBoolean(entry.getValue().toString()));
//				}
//				else if (entry.getKey().equalsIgnoreCase("up")) {
//					((MultipleFacing) blockData).setFace(BlockFace.UP, Boolean.parseBoolean(entry.getValue().toString()));
//				}
//				else if (entry.getKey().equalsIgnoreCase("down")) {
//					((MultipleFacing) blockData).setFace(BlockFace.DOWN, Boolean.parseBoolean(entry.getValue().toString()));
//				}
//			}
//
//			if (blockData instanceof Stairs) {
//				if (entry.getKey().equalsIgnoreCase("half")) {
//					((Stairs) blockData).setHalf(Stairs.Half.valueOf(entry.getValue().toString().toUpperCase()));
//				}
//				else if (entry.getKey().equalsIgnoreCase("shape")) {
//					((Stairs) blockData).setShape(Stairs.Shape.valueOf(entry.getValue().toString().toUpperCase()));
//				}
//			}
//			if (blockData instanceof Slab) {
//				if (entry.getKey().equalsIgnoreCase("half")) {
//					((Slab) blockData).setType(Slab.Type.valueOf(entry.getValue().toString().toUpperCase()));
//				}
//			}
//			if (blockData instanceof Chest) {
//				if (entry.getKey().equalsIgnoreCase("type")) {
//					((Chest) blockData).setType(Chest.Type.valueOf(entry.getValue().toString().toUpperCase()));
//				}
//			}
//		}
		String data = "[";
		int i = 0;
		for (Map.Entry<String, Object> entry: map.entrySet()) {
			data += entry.getKey() + "=" + entry.getValue();
			i++;
			if (i != map.size()) data += ",";
		}
		data += "]";
		block.setBlockData(block.getType().createBlockData(data));
	}

	private Object getTagValue(NBTTag tag) {
		TagType tt = tag.getTagType();
		switch (tt) {
			case BYTE:
				return tag.getAsTagByte().getValue();
			case BYTE_ARRAY:
				return tag.getAsTagByteArray().getValue();
			case COMPOUND:
				return tag.getAsTagCompound().getValue();
			case DOUBLE:
				return tag.getAsTagDouble().getValue();
			case FLOAT:
				return tag.getAsTagFloat().getValue();
			case INT:
				return tag.getAsTagInt().getValue();
			case INT_ARRAY:
				return tag.getAsTagIntArray().getValue();
			case LIST:
				List<Object> list = new ArrayList<>();
				tag.getAsTagList().getValue().forEach(nbtTag -> list.add(getTagValue(nbtTag)));
				return list;
			case LONG:
				return tag.getAsTagLong().getValue();
			case LONG_ARRAY:
				return tag.getAsTagLongArray().getValue();
			case SHORT:
				return tag.getAsTagShort().getValue();
			case STRING:
				return tag.getAsTagString().getValue();
		}
		return null;
	}

	public EntityType getEntityByName(String name) {//TODO Private access
		for (EntityType type : EntityType.values()) {
			if(type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
