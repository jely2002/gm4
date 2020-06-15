package com.belka.spigot.gm4.interfaces;

import api.Setting;
import com.belka.spigot.gm4.MainClass;

public interface Module {
    default void init(MainClass mc) {}
    default void reload() {}
	default Setting getSetting() {
		return null;
	}
	default boolean hasSetting() {
    	return getSetting() != null;
	}
}

