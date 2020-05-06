package gg.steve.elemental.ce.utils;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.tokens.ElementalTokens;
import gg.steve.elemental.tokens.api.TokensApi;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class GuiItemUtil {

    public static ItemStack createItem(YamlConfiguration section, String entry, String enchantName, EnchantPlayer player) {
        boolean enchantItem = false;
        Enchant enchant = null;
        if (!enchantName.equalsIgnoreCase("none") && !enchantName.equalsIgnoreCase("close")) {
            enchantItem = true;
            enchant = EnchantManager.getEnchant(enchantName);
        }
        ItemBuilderUtil builder = new ItemBuilderUtil(section.getString(entry + ".item"), section.getString(entry + ".data"));
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
        return builder.getItem();
    }
}
