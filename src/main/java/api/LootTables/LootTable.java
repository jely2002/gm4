package api.LootTables;

import java.io.File;
import java.util.List;

public class LootTable {
	private String type;
	private List<Pool> pools;

	public LootTable(File file) {

	}
	public LootTable(List<Pool> pools) {
		this.pools = pools;
	}
	public LootTable(String type, List<Pool> pools) {
		this.type = type;
		this.pools = pools;
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
}
