package gg.steve.elemental.ce.data;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public interface EnchantData {

    EnchantDataType getType();

    void remove(Player player, int enchantLevel);

    void onHold(Player player, int enchantLevel);

    void onMine(BlockBreakEvent event, int enchantLevel);

    void onTokenDrop(Player player, int enchantLevel);

    Enchantment getVanillaEnchantment();
}
