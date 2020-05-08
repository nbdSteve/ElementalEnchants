package gg.steve.elemental.ce.data;

import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public interface EnchantData {

    EnchantDataType getType();

    void remove(Player player, int enchantLevel);

    void onHold(Player player, int enchantLevel);

    void onMine(BlockBreakEvent event, int enchantLevel);

    void onBackpackSell(PreBackpackSaleEvent event, int enchantLevel);

    void onTokenAdd(PreTokenAddEvent event, int enchantLevel);

    Enchantment getVanillaEnchantment();
}
