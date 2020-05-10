package gg.steve.elemental.ce.utils;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.tokens.ElementalTokens;
import gg.steve.elemental.tokens.api.TokensApi;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class GuiItemUtil {

    public static ItemStack createItem(YamlConfiguration section, String entry, String enchantName, EnchantPlayer player) {
        boolean enchantItem = false;
        Enchant enchant = null;
        if (!enchantName.equalsIgnoreCase("none") && !enchantName.equalsIgnoreCase("close") && !enchantName.equalsIgnoreCase("back")) {
            enchantItem = true;
            enchant = EnchantManager.getEnchant(enchantName);
        }
        ItemBuilderUtil builder;
        if (section.getString(entry + ".material").startsWith("hdb")) {
            String[] parts = section.getString(entry + ".material").split("-");
            try {
                builder = new ItemBuilderUtil(new HeadDatabaseAPI().getItemHead(parts[1]));
            } catch (NullPointerException e) {
                builder = new ItemBuilderUtil(new ItemStack(Material.valueOf("SKULL_ITEM")));
            }
        } else {
            builder = new ItemBuilderUtil(section.getString(entry + ".material"), section.getString(entry + ".data"));
        }
        builder.addName(section.getString(entry + ".name"));
        if (enchantItem) {
            builder.setLorePlaceholders("{current-level}", "{max-level}", "{upgrade-price}", "{balance}");
            builder.addLore(section.getStringList(entry + ".lore"),
                    ElementalTokens.getNumberFormat().format(player.getEnchantLevel(enchant)),
                    ElementalTokens.getNumberFormat().format(enchant.getMaxLevel()),
                    ElementalTokens.getNumberFormat().format(enchant.getUpgradePrice()),
                    ElementalTokens.getNumberFormat().format(TokensApi.getTokens(player.getPlayerId(), enchant.getUpgradeType())));
        } else {
            builder.addLore(section.getStringList(entry + ".lore"));
        }
        builder.addEnchantments(section.getStringList(entry + ".enchantments"));
        builder.addItemFlags(section.getStringList(entry + ".item-flags"));
        builder.addNBT();
        return builder.getItem();
    }
}
