package gg.steve.elemental.ce.listener;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        EnchantPlayer player = PlayerEnchantManager.getEnchantPlayer(event.getPlayer().getUniqueId());
        if (player.getActiveEnchants().isEmpty()) return;
        for (Enchant enchant : player.getActiveEnchants().keySet()) {
            enchant.onMine(event, player.getEnchantLevel(enchant));
        }
    }
}
