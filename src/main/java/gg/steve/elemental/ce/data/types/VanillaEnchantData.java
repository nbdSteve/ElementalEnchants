package gg.steve.elemental.ce.data.types;

import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class VanillaEnchantData implements EnchantData {
    private Enchantment enchantment;

    public VanillaEnchantData(ConfigurationSection section) {
        this.enchantment = Enchantment.getByName(section.getString("enchantment").toUpperCase());
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.VANILLA;
    }

    @Override
    public void remove(Player player, int enchantLevel) {

    }

    @Override
    public void onHold(Player player, int enchantLevel) {

    }

    @Override
    public void onMine(BlockBreakEvent event, int enchantLevel) {

    }

    @Override
    public void onTokenDrop(Player player, int enchantLevel) {

    }

    @Override
    public Enchantment getVanillaEnchantment() {
        return this.enchantment;
    }
}
