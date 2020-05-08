package gg.steve.elemental.ce.core;

import gg.steve.elemental.ce.ElementalEnchants;
import gg.steve.elemental.ce.managers.Files;
import gg.steve.elemental.ce.nbt.NBTItem;
import gg.steve.elemental.ce.utils.ColorUtil;
import gg.steve.elemental.ce.utils.ItemBuilderUtil;
import gg.steve.elemental.ce.utils.LogUtil;
import gg.steve.elemental.ce.utils.YamlFileUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantManager {
    private static Map<String, Enchant> enchants;
    private static List<Material> enchantableItems;

    public static void loadEnchants() {
        enchants = new HashMap<>();
        for (String enchant : Files.CONFIG.get().getStringList("loaded-enchants")) {
            YamlFileUtil file = new YamlFileUtil("enchants" + File.separator + enchant + ".yml", ElementalEnchants.get());
            enchants.put(enchant, new Enchant(enchant, file));
            LogUtil.info("Successfully loaded enchantment: " + enchant + ", into the plugins internal map.");
        }
        enchantableItems = new ArrayList<>();
        for (String material : Files.CONFIG.get().getStringList("enchantable-items")) {
            enchantableItems.add(Material.valueOf(material.toUpperCase()));
        }
    }

    public static boolean hasEnchants(NBTItem nbtItem) {
        return nbtItem.getObject("elemental-enchants", HashMap.class) != null;
    }

    public static Map<String, Enchant> getEnchants() {
        return enchants;
    }

    public static Enchant getEnchant(String name) {
        return enchants.get(name);
    }

    public static Enchant getVanillaEnchant(Enchantment enchantment) {
        for (Enchant enchant : enchants.values()) {
            if (enchant.getData().getVanillaEnchantment() == null) continue;
            if (enchant.getData().getVanillaEnchantment().equals(enchantment)) return enchant;
        }
        return null;
    }

    public static void applyEnchant(Player player, NBTItem nbtItem, Enchant enchant, int enchantLevel) {
        Map<String, Double> enchants;
        if (nbtItem.getObject("elemental-enchants", Map.class) == null) {
            enchants = new HashMap<>();
        } else {
            enchants = nbtItem.getObject("elemental-enchants", HashMap.class);
        }
        boolean upgrade = enchants.containsKey(enchant.getName());
        int currentLevel = 0;
        if (upgrade) {
            currentLevel = (int) Math.round(enchants.get(enchant.getName()));
        }
        enchants.put(enchant.getName(), (double) enchantLevel);
        nbtItem.setObject("elemental-enchants", enchants);
        ItemBuilderUtil builderUtil = new ItemBuilderUtil(nbtItem.getItem());
        if (upgrade) {
            if (!builderUtil.getLore().isEmpty()) {
                for (int i = 0; i < builderUtil.getLore().size(); i++) {
                    if (builderUtil.getLore().get(i).equalsIgnoreCase(ColorUtil.colorize(enchant.getLore().replace("{level}", String.valueOf(currentLevel))))) {
                        builderUtil.getLore().remove(i);
                    }
                }
            }
        }
        builderUtil.addLoreLine(enchant.getLore().replace("{level}", String.valueOf(enchantLevel)));
        nbtItem.getItem().setItemMeta(builderUtil.getItemMeta());
        player.setItemInHand(nbtItem.getItem());
        PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, enchantLevel);
    }

    public static void applyVanilla(Player player, NBTItem nbtItem, Enchant enchant, int enchantLevel) {
        ItemMeta meta = nbtItem.getItem().getItemMeta();
        meta.addEnchant(enchant.getData().getVanillaEnchantment(), enchantLevel, true);
        nbtItem.getItem().setItemMeta(meta);
        player.setItemInHand(nbtItem.getItem());
        PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, enchantLevel);
    }

    public static boolean isEnchantable(ItemStack item) {
        return enchantableItems.contains(item.getType());
    }
}
