package com.belka.spigot.gm4.interfaces;

import com.belka.spigot.gm4.MainClass;

public interface Module {
    public default void init(MainClass mc) {}
    public default void reload() {}
}

