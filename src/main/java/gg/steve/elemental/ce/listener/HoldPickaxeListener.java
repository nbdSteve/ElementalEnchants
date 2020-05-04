package gg.steve.elemental.ce.listener;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.EnchantPlayer;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.ce.nbt.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.Map;

public class HoldPickaxeListener implements Listener {

    @EventHandler
    public void playerHold(PlayerItemHeldEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        NBTItem newItem;
        NBTItem oldItem;
        if (event.getPlayer().getInventory().getItem(event.getPreviousSlot()) != null
                && !event.getPlayer().getInventory().getItem(event.getPreviousSlot()).getType().equals(Material.AIR)) {
            oldItem = new NBTItem(event.getPlayer().getInventory().getItem(event.getPreviousSlot()));
            if (EnchantManager.hasEnchants(oldItem)) {
                Map<Enchant, Integer> enchants = oldItem.getObject("elemental-enchants", Map.class);
                for (Enchant enchant : enchants.keySet()) {
                    PlayerEnchantManager.removeEnchantFromPlayer(player.getUniqueId(), enchant);
                }
            }
        }
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null
                && !event.getPlayer().getInventory().getItem(event.getNewSlot()).getType().equals(Material.AIR)) {
            newItem = new NBTItem(event.getPlayer().getInventory().getItem(event.getNewSlot()));
            if (EnchantManager.hasEnchants(newItem)) {
                Map<Enchant, Integer> enchants = newItem.getObject("elemental-enchants", Map.class);
                for (Enchant enchant : enchants.keySet()) {
                    enchant.onHold(event, enchants.get(enchant));
                    PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, enchants.get(enchant));
                }
            }
        }
    }
}
