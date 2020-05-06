package gg.steve.elemental.ce.data.types;

import gg.steve.elemental.ce.data.EnchantData;
import gg.steve.elemental.ce.data.EnchantDataType;
import gg.steve.elemental.ce.utils.CommandUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class CommandEnchantData implements EnchantData {
    private List<String> commands;
    private double baseRate, multiplier, baseAmount, amountMultiplier;

    public CommandEnchantData(ConfigurationSection section) {
        this.commands = section.getStringList("commands");
        this.baseRate = section.getDouble("base-rate");
        this.multiplier = section.getDouble("multiplier");
        this.baseAmount = section.getDouble("base-amount");
        this.amountMultiplier = section.getDouble("amount-multiplier");
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
        int amount = (int) Math.floor(this.baseAmount + (this.amountMultiplier * enchantLevel));
        CommandUtil.execute(this.commands, event.getPlayer(), amount);
    }
}
