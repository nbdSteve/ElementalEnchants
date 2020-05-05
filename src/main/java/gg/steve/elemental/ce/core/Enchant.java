package gg.steve.elemental.ce.core;

import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.types.PotionEnchantData;
import gg.steve.elemental.ce.utils.YamlFileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class Enchant {
    private String name;
    private YamlConfiguration config;
    private int maxLevel;
    private int upgradePrice;
    private String lore;
    private EnchantData data;

    public Enchant(String name, YamlFileUtil file) {
        this.name = name;
        this.config = file.get();
        this.maxLevel = this.config.getInt("max-level");
        this.upgradePrice = this.config.getInt("upgrade-price");
        this.lore = this.config.getString("lore");
        switch (this.config.getString("data.type")) {
            case "potion":
                this.data = new PotionEnchantData(this.config.getConfigurationSection("data"), this);
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
}
