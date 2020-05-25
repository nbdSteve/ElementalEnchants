package gg.steve.elemental.ce.data.types;

import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import gg.steve.elemental.ce.utils.EnchantProcUtil;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;
import java.util.Random;

public class CommandEnchantData implements EnchantData {
    private ConfigurationSection section;
    private List<String> commands;
    private double baseRate, multiplier;

    public CommandEnchantData(ConfigurationSection section) {
        this.section = section;
        this.commands = section.getStringList("commands");
        this.baseRate = section.getDouble("base-rate");
        this.multiplier = section.getDouble("multiplier");
    }

    @Override
    public EnchantDataType getType() {
        return EnchantDataType.COMMAND;
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
        int drop = 0;
        if (this.commands.size() > 1) {
            drop = new Random().nextInt(this.commands.size());
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.commands.get(drop).replace("{player}", event.getPlayer().getName()));
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
