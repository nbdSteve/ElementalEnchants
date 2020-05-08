package gg.steve.elemental.ce.data.types;

import gg.steve.elemental.bps.event.BackpackSellEvent;
import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.bps.event.SellMethodType;
import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import gg.steve.elemental.ce.utils.EnchantProcUtil;
import gg.steve.elemental.tokens.event.AddMethodType;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import gg.steve.elemental.tokens.event.TokenAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class TokenatorEnchantData implements EnchantData {
    private ConfigurationSection section;
    double baseRate, multiplier;

    public TokenatorEnchantData(ConfigurationSection section) {
        this.section = section;
        this.baseRate = section.getDouble("base-rate");
        this.multiplier = section.getDouble("multiplier");
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.TOKENATOR;

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
    public void onBackpackSell(PreBackpackSaleEvent event, int enchantLevel) {

    }

    @Override
    public void onTokenAdd(PreTokenAddEvent event, int enchantLevel) {
        if (Math.random() * 100 > (this.baseRate + (this.multiplier * enchantLevel))) return;
        event.setCancelled(true);
        Bukkit.getPluginManager().callEvent(new TokenAddEvent(event.getPlayer(), event.getType(), event.getAmount() * 2, AddMethodType.TOKENATOR));
        EnchantProcUtil.doProc(section, event.getPlayer().getPlayer().getPlayer());
    }

    @Override
    public Enchantment getVanillaEnchantment() {
        return null;
    }
}
