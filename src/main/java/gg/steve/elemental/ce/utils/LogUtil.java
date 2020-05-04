package gg.steve.elemental.ce.utils;

import gg.steve.elemental.ce.ElementalEnchants;

public class LogUtil {

    public static void info(String message) {
        ElementalEnchants.get().getLogger().info(message);
    }

    public static void warning(String message) {
        ElementalEnchants.get().getLogger().warning(message);
    }
}
