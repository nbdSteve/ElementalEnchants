package gg.steve.elemental.ce.core;

import gg.steve.elemental.ce.ElementalEnchants;
import gg.steve.elemental.ce.managers.Files;
import gg.steve.elemental.ce.nbt.NBTItem;
import gg.steve.elemental.ce.utils.ColorUtil;
import gg.steve.elemental.ce.utils.LogUtil;
import gg.steve.elemental.ce.utils.YamlFileUtil;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class EnchantManager {
    private static Map<String, Enchant> enchants;

    public static void loadEnchants() {
        enchants = new HashMap<>();
        for (String enchant : Files.CONFIG.get().getStringList("loaded-enchants")) {
            YamlFileUtil file = new YamlFileUtil("enchants" + File.separator + enchant + ".yml", ElementalEnchants.get());
            enchants.put(enchant, new Enchant(enchant, file));
            LogUtil.info("Successfully loaded enchantment: " + enchant + ", into the plugins internal map.");
        }
    }

    public static void applyEnchant(Player player, NBTItem nbtItem, Enchant enchant, int enchantLevel) {
        Map<Enchant, Integer> enchants;
        if (nbtItem.getObject("elemental-enchants", Map.class) == null) {
            enchants = new HashMap<>();
        } else {
            enchants = nbtItem.getObject("elemental-enchants", Map.class);
        }
        enchants.put(enchant, enchantLevel);
        nbtItem.setObject("elemental-enchants", enchants);
        List<String> lore;
        try {
            lore = nbtItem.getItem().getItemMeta().getLore();
        } catch (Exception e) {
            lore = new ArrayList<>();
        }
        lore.add(ColorUtil.colorize(enchant.getLore().replace("{level}", String.valueOf(enchantLevel))));
        nbtItem.getItem().getItemMeta().setLore(lore);
        player.setItemInHand(nbtItem.getItem());
    }

    public static boolean hasEnchants(NBTItem nbtItem) {
        return nbtItem.getObject("elemental-enchants", Map.class) != null;
    }

    public static Map<String, Enchant> getEnchants() {
        return enchants;
    }

    public static Enchant getEnchant(String name) {
        return enchants.get(name);
    }
}
