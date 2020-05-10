package gg.steve.elemental.ce.data.types;

import gg.steve.elemental.bps.api.BackpacksApi;
import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.bps.event.SellMethodType;
import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import gg.steve.elemental.ce.utils.EnchantProcUtil;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class BackpackBusterEnchantData implements EnchantData {
    private ConfigurationSection section;
    private String group;
    private double baseRate, multiplier, baseAmount, amountMultiplier;

    public BackpackBusterEnchantData(ConfigurationSection section) {
        this.section = section;
        this.group = section.getString("sell-group");
        this.baseRate = section.getDouble("base-rate");
        this.multiplier = section.getDouble("multiplier");
        this.baseAmount = section.getDouble("base-amount");
        this.amountMultiplier = section.getDouble("amount-multiplier");
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.BACKPACK_BUSTER;
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
        int amount = (int) Math.floor(this.baseAmount + ((this.amountMultiplier * this.baseAmount) * enchantLevel));
        BackpacksApi.sellBackpack(event.getPlayer(), this.group, amount, SellMethodType.BUSTER);
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
