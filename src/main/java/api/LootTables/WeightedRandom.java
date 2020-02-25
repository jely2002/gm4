package api.LootTables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandom<T> {
	private class Entry {
		int weight;
		T object;
	}

	private List<Entry> entries = new ArrayList<>();

	public void addEntry(T object, int weight) {
		Entry e = new Entry();
		e.object = object;
		e.weight = weight;
		entries.add(e);
	}

	public T getRandom() {
		List<T> list = new ArrayList<>();
		for (Entry entry: entries) {
			for (int i = 0; i < entry.weight; i++) {
				list.add(entry.object);
			}
		}
		return list.get(new Random().nextInt(list.size()));
	}
}
