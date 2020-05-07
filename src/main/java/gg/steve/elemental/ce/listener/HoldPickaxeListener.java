package gg.steve.elemental.ce.listener;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.ce.nbt.NBTItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;
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
            removeEnchants(oldItem, player);
        }
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null
                && !event.getPlayer().getInventory().getItem(event.getNewSlot()).getType().equals(Material.AIR)) {
            newItem = new NBTItem(event.getPlayer().getInventory().getItem(event.getNewSlot()));
            applyEnchants(newItem, player);
        }
    }

    @EventHandler
    public void playerDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        NBTItem oldItem;
        if (!event.getItemDrop().getItemStack().getType().equals(Material.AIR)) {
            oldItem = new NBTItem(event.getItemDrop().getItemStack());
            removeEnchants(oldItem, player);
        }
    }

    @EventHandler
    public void playerPickup(PlayerPickupItemEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        NBTItem item;
        if (!event.getItem().getItemStack().getType().equals(Material.AIR)) {
            item = new NBTItem(event.getItem().getItemStack());
            applyEnchants(item, player);
        }
    }

    @EventHandler
    public void inventoryRemove(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!event.getCurrentItem().equals(player.getItemInHand())) return;
        NBTItem item = new NBTItem(player.getItemInHand());
        removeEnchants(item, player);
    }

    @EventHandler
    public void inventoryAdd(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() != player.getInventory().getHeldItemSlot()) return;
        NBTItem item = new NBTItem(event.getCursor());
        applyEnchants(item, player);
    }

    @EventHandler
    public void inventoryShift(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getClick().equals(ClickType.SHIFT_RIGHT) || event.getClick().equals(ClickType.SHIFT_LEFT)))
            return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getSlotType().equals(InventoryType.SlotType.QUICKBAR)) return;
        if (player.getInventory().firstEmpty() != player.getInventory().getHeldItemSlot()) return;
        NBTItem item = new NBTItem(event.getCurrentItem());
        applyEnchants(item, player);
    }

    @EventHandler
    public void inventoryNum(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!event.getClick().equals(ClickType.NUMBER_KEY)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getHotbarButton() != player.getInventory().getHeldItemSlot()) return;
        NBTItem current;
        NBTItem slot;
        if (player.getInventory().getItem(event.getHotbarButton()) != null && !player.getInventory().getItem(event.getHotbarButton()).getType().equals(Material.AIR)) {
            slot = new NBTItem(player.getInventory().getItem(event.getHotbarButton()));
            removeEnchants(slot, player);
        }
        if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
            current = new NBTItem(event.getCurrentItem());
            applyEnchants(current, player);
        }
    }

    public void applyEnchants(NBTItem item, Player player) {
        if (EnchantManager.hasEnchants(item)) {
            Map<String, Double> enchants = item.getObject("elemental-enchants", HashMap.class);
            for (String enchantName : enchants.keySet()) {
                Enchant enchant = EnchantManager.getEnchant(enchantName);
                int level = (int) Math.round(enchants.get(enchantName));
                PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, level);
            }
            for (Enchantment enchantment : item.getItem().getEnchantments().keySet()) {
                Enchant enchant;
                try {
                    enchant = EnchantManager.getVanillaEnchant(enchantment);
                } catch (Exception e) {
                    continue;
                }
                PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, item.getItem().getEnchantmentLevel(enchantment));
            }
        }
    }

    public void removeEnchants(NBTItem item, Player player) {
        if (EnchantManager.hasEnchants(item)) {
            Map<String, Double> enchants = item.getObject("elemental-enchants", HashMap.class);
            for (String enchantName : enchants.keySet()) {
                Enchant enchant = EnchantManager.getEnchant(enchantName);
                PlayerEnchantManager.removeEnchantFromPlayer(player.getUniqueId(), enchant);
            }
            for (Enchantment enchantment : item.getItem().getEnchantments().keySet()) {
                Enchant enchant;
                try {
                    enchant = EnchantManager.getVanillaEnchant(enchantment);
                } catch (Exception e) {
                    continue;
                }
                PlayerEnchantManager.removeEnchantFromPlayer(player.getUniqueId(), enchant);
            }
        }
    }
}
