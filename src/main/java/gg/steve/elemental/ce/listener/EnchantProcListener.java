package gg.steve.elemental.ce.listener;

import gg.steve.elemental.bps.event.PreBackpackSaleEvent;
import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.ce.managers.Files;
import gg.steve.elemental.ce.nbt.NBTItem;
import gg.steve.elemental.tokens.api.TokensApi;
import gg.steve.elemental.tokens.core.TokenType;
import gg.steve.elemental.tokens.event.AddMethodType;
import gg.steve.elemental.tokens.event.PreTokenAddEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class EnchantProcListener implements Listener {

    @EventHandler
    public void onMine(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        EnchantPlayer player = PlayerEnchantManager.getEnchantPlayer(event.getPlayer().getUniqueId());
        if (player.getActiveEnchants().isEmpty()) return;
        if (Files.doTokenDrop()) {
            Bukkit.getPluginManager().callEvent(new PreTokenAddEvent(TokensApi.getTokenPlayer(player.getPlayerId()), TokenType.TOKEN, 1, AddMethodType.MINE));
        }
        for (Enchant enchant : player.getActiveEnchants().keySet()) {
            enchant.onMine(event, player.getEnchantLevel(enchant));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void packSell(PreBackpackSaleEvent event) {
        if (event.isCancelled()) return;
        EnchantPlayer player = PlayerEnchantManager.getEnchantPlayer(event.getOwner().getPlayer().getUniqueId());
        if (player.getActiveEnchants().isEmpty()) return;
        for (Enchant enchant : player.getActiveEnchants().keySet()) {
            enchant.onBackpackSell(event, player.getEnchantLevel(enchant));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void tokenAdd(PreTokenAddEvent event) {
        if (event.isCancelled()) return;
        EnchantPlayer player = PlayerEnchantManager.getEnchantPlayer(event.getPlayer().getPlayerId());
        if (player.getActiveEnchants().isEmpty()) return;
        for (Enchant enchant : player.getActiveEnchants().keySet()) {
            enchant.onTokenAdd(event, player.getEnchantLevel(enchant));
        }
    }

    @EventHandler
    public void itemDamage(PlayerItemDamageEvent event) {
        if (event.isCancelled()) return;
        if (EnchantManager.isEnchantable(event.getItem())) event.setCancelled(true);
    }
}