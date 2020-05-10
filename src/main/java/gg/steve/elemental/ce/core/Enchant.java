package gg.steve.elemental.ce.core;

import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.types.*;
import gg.steve.elemental.ce.utils.YamlFileUtil;
import gg.steve.elemental.tokens.core.TokenType;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
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
            case "vanilla":
                this.data = new VanillaEnchantData(section);
                break;
            case "buster":
                this.data = new BackpackBusterEnchantData(section);
                break;
            case "merchant":
                this.data = new MerchantEnchantData(section);
                break;
            case "tokenator":
                this.data = new TokenatorEnchantData(section);
                break;
            case "luck":
                this.data = new LuckEnchantData(section);
                break;
            case "regional":
                this.data = new RegionalEnchantData(section);
                break;
            case "explosive":
                this.data = new ExplosiveEnchantData(section);
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

    public void onTokenAdd(PreTokenAddEvent event, int enchantLevel) {
        this.data.onTokenAdd(event, enchantLevel);
    }

    public void onBackpackSell(PreBackpackSaleEvent event, int enchantLevel) {
        this.data.onBackpackSell(event, enchantLevel);
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
