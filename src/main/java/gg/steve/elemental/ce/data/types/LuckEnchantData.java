package gg.steve.elemental.ce.data.types;

import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import gg.steve.elemental.ce.utils.EnchantProcUtil;
import gg.steve.elemental.ce.utils.LogUtil;
import gg.steve.elemental.ce.utils.LuckyBlockUtil;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class LuckEnchantData implements EnchantData {
    private ConfigurationSection section;
    private double baseRate, multiplier;

    public LuckEnchantData(ConfigurationSection section) {
        this.section = section;
        this.baseRate = section.getDouble("base-rate");
        this.multiplier = section.getDouble("multiplier");
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.LUCK;
    }

    @Override
    public void remove(Player player, int enchantLevel) {

    }

    @Override
    public void onHold(Player player, int enchantLevel) {

    }

    @Override
    public void onMine(BlockBreakEvent event, int enchantLevel) {
        if (Math.random() * 100 > (this.baseRate + (this.multiplier * enchantLevel))) return;
        LuckyBlockUtil.calculateLoot(event.getPlayer());
        EnchantProcUtil.doProc(section, event.getPlayer());
    }

    @Override
    public void onBackpackSell(PreBackpackSaleEvent event, int enchantLevel) {

    }

    @Override
    public void onTokenAdd(PreTokenAddEvent event, int enchantLevel) {

    }

    @Override
    public Enchantment getVanillaEnchantment() {
        return null;
    }
}
