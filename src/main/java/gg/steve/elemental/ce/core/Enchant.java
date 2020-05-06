package gg.steve.elemental.ce.core;

import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.types.CommandEnchantData;
import gg.steve.elemental.ce.data.types.PotionEnchantData;
import gg.steve.elemental.ce.utils.YamlFileUtil;
import gg.steve.elemental.tokens.core.TokenType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class Enchant {
    private String name;
    private YamlConfiguration config;
    private int maxLevel;
    private int upgradePrice;
    private TokenType upgradeType;
    private String lore;
    private EnchantData data;

    public Enchant(String name, YamlFileUtil file) {
        this.name = name;
        this.config = file.get();
        this.maxLevel = this.config.getInt("max-level");
        this.upgradePrice = this.config.getInt("upgrade.price");
        this.upgradeType = TokenType.valueOf(this.config.getString("upgrade.type").toUpperCase());
        this.lore = this.config.getString("lore");
        ConfigurationSection section = this.config.getConfigurationSection("data");
        switch (this.config.getString("data.type")) {
            case "potion":
                this.data = new PotionEnchantData(section, this);
                break;
            case "command":
                this.data = new CommandEnchantData(section);
                break;
        }
    }

    public void remove(Player player, int enchantLevel) {
        this.data.remove(player, enchantLevel);
    }

    public void onHold(Player player, int enchantLevel) {
        this.data.onHold(player, enchantLevel);
    }

    public void onMine(BlockBreakEvent event, int enchantLevel) {
        this.data.onMine(event, enchantLevel);
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getUpgradePrice() {
        return upgradePrice;
    }

    public TokenType getUpgradeType() {
        return upgradeType;
    }

    public EnchantData getData() {
        return data;
    }
}
