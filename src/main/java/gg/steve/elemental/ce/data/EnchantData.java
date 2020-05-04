package gg.steve.elemental.ce.data;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public interface EnchantData {

    EnchantDataType getType();
    void remove(Player player, int enchantLevel);
    void onHold(PlayerItemHeldEvent event, int enchantLevel);
    void onMine(BlockBreakEvent event, int enchantLevel);
}
