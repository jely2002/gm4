package api;

import de.tr7zw.nbtapi.NBTFile;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class Structure {
	private NBTFile nbtFile;

	public Structure(File file) throws IOException {
		this.nbtFile = new NBTFile(file);
	}

	public void placeStructure(Location target, boolean mirrorX, boolean mirrorZ) {
	}
}
