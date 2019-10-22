package api;

import javafx.util.Pair;
import mryurihi.tbnbt.TagType;
import mryurihi.tbnbt.stream.NBTInputStream;
import mryurihi.tbnbt.tag.NBTTag;
import mryurihi.tbnbt.tag.NBTTagCompound;
import mryurihi.tbnbt.tag.NBTTagList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Structure {
	private NBTTagCompound nbt;

	public Structure(File file) throws IOException {
		this.nbt = new NBTInputStream(new FileInputStream(file)).readTag().getAsTagCompound();
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
			if (tag.containsKey("pos") && tag.get("pos").getAsTagIntArray().getValue().length == 3) {
				int[] posInt = tag.get("pos").getAsTagIntArray().getValue();
				pos.setX(posInt[0]).setY(posInt[1]).setZ(posInt[2]);
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
			if (tag.containsKey("pos") && tag.get("pos").getAsTagIntArray().getValue().length == 3) {
				int[] posInt = tag.get("pos").getAsTagIntArray().getValue();
				pos.setX(posInt[0]).setY(posInt[1]).setZ(posInt[2]);
			}
			list.add(new Pair<>(pos, state));
		}
		return list;
	}
	public List<Pair<Vector, Material>> getBlocksWithMaterial() {
		List<Pair<Vector, Integer>> blocks = getBlocks();
		List<Pair<Material, HashMap<String, Boolean>>> palette = getPalette();
		List<Pair<Vector, Material>> list = new ArrayList<>();
		for (Pair<Vector, Integer> pair: blocks) {
			list.add(new Pair<>(pair.getKey(), palette.get(pair.getValue()).getKey()));
		}
		return list;
	}

	public List<Pair<Material, HashMap<String, Boolean>>> getPalette() {
		List<Pair<Material, HashMap<String, Boolean>>> list = new ArrayList<>();
		if (!nbt.containsKey("palette")) return null;
		NBTTagList palette = nbt.get("palette").getAsTagList();
		for (NBTTag nbtTag: palette.getValue()) {
			NBTTagCompound tag = nbtTag.getAsTagCompound();
			Material mat = null;
			if (tag.containsKey("Name"))
				mat = Material.matchMaterial(tag.get("Name").getAsTagString().getValue());
			HashMap<String, Boolean> map = new HashMap<>();
			if (tag.containsKey("Properties")) {
				for (Map.Entry<String, NBTTag> entry : tag.get("Properties").getAsTagCompound().getValue().entrySet()) {
					map.put(entry.getKey(), Boolean.parseBoolean(entry.getValue().getAsTagString().getValue()));
				}
			}
			list.add(new Pair<>(mat, map));
		}
		return list;
	}

	public Integer getDataVersion() {//VS int & return -1;
		if (nbt.containsKey("DataVersion")) return nbt.get("DataVersion").getAsTagInt().getValue();
		return null;
	}

	public void placeStructure(Location target, boolean includeEntities, boolean mirrorX, boolean mirrorZ) {
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
}
