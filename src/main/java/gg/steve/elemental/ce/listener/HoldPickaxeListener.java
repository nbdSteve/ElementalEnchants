package gg.steve.elemental.ce.listener;

import gg.steve.elemental.ce.core.Enchant;
import gg.steve.elemental.ce.core.EnchantManager;
import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.ce.nbt.NBTItem;
import gg.steve.elemental.ce.utils.LogUtil;
import org.bukkit.Material;
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
            if (EnchantManager.hasEnchants(oldItem)) {
                Map<String, Double> enchants = oldItem.getObject("elemental-enchants", HashMap.class);
                for (String enchantName : enchants.keySet()) {
                    Enchant enchant = EnchantManager.getEnchant(enchantName);
                    PlayerEnchantManager.removeEnchantFromPlayer(player.getUniqueId(), enchant);
                }
            }
        }
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) != null
                && !event.getPlayer().getInventory().getItem(event.getNewSlot()).getType().equals(Material.AIR)) {
            newItem = new NBTItem(event.getPlayer().getInventory().getItem(event.getNewSlot()));
            if (EnchantManager.hasEnchants(newItem)) {
                Map<String, Double> enchants = newItem.getObject("elemental-enchants", HashMap.class);
                for (String enchantName : enchants.keySet()) {
                    Enchant enchant = EnchantManager.getEnchant(enchantName);
                    int level = (int) Math.round(enchants.get(enchantName));
                    PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, level);
                }
            }
        }
    }

    @EventHandler
    public void playerDrop(PlayerDropItemEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        NBTItem oldItem;
        if (!event.getItemDrop().getItemStack().getType().equals(Material.AIR)) {
            oldItem = new NBTItem(event.getItemDrop().getItemStack());
            if (EnchantManager.hasEnchants(oldItem)) {
                Map<String, Double> enchants = oldItem.getObject("elemental-enchants", HashMap.class);
                for (String enchantName : enchants.keySet()) {
                    Enchant enchant = EnchantManager.getEnchant(enchantName);
                    PlayerEnchantManager.removeEnchantFromPlayer(player.getUniqueId(), enchant);
                }
            }
        }
    }

    @EventHandler
    public void playerPickup(PlayerPickupItemEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        NBTItem newitem;
        if (!event.getItem().getItemStack().getType().equals(Material.AIR)) {
            newitem = new NBTItem(event.getItem().getItemStack());
            if (EnchantManager.hasEnchants(newitem)) {
                Map<String, Double> enchants = newitem.getObject("elemental-enchants", HashMap.class);
                for (String enchantName : enchants.keySet()) {
                    Enchant enchant = EnchantManager.getEnchant(enchantName);
                    int level = (int) Math.round(enchants.get(enchantName));
                    enchant.onHold(player, level);
                    PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, level);
                }
            }
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
        if (EnchantManager.hasEnchants(item)) {
            Map<String, Double> enchants = item.getObject("elemental-enchants", HashMap.class);
            for (String enchantName : enchants.keySet()) {
                Enchant enchant = EnchantManager.getEnchant(enchantName);
                PlayerEnchantManager.removeEnchantFromPlayer(player.getUniqueId(), enchant);
            }
        }
    }

    @EventHandler
    public void inventoryAdd(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() != player.getInventory().getHeldItemSlot()) return;
        NBTItem item = new NBTItem(event.getCursor());
        if (EnchantManager.hasEnchants(item)) {
            Map<String, Double> enchants = item.getObject("elemental-enchants", HashMap.class);
            for (String enchantName : enchants.keySet()) {
                Enchant enchant = EnchantManager.getEnchant(enchantName);
                int level = (int) Math.round(enchants.get(enchantName));
                PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, level);
            }
        }
    }

    @EventHandler
    public void inventoryShift(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getClick().equals(ClickType.SHIFT_RIGHT) || event.getClick().equals(ClickType.SHIFT_LEFT))) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (event.getSlotType().equals(InventoryType.SlotType.QUICKBAR)) return;
        if (player.getInventory().firstEmpty() != player.getInventory().getHeldItemSlot()) return;
        NBTItem item = new NBTItem(event.getCurrentItem());
        if (EnchantManager.hasEnchants(item)) {
            Map<String, Double> enchants = item.getObject("elemental-enchants", HashMap.class);
            for (String enchantName : enchants.keySet()) {
                Enchant enchant = EnchantManager.getEnchant(enchantName);
                int level = (int) Math.round(enchants.get(enchantName));
                PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, level);
            }
        }
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
            if (EnchantManager.hasEnchants(slot)) {
                Map<String, Double> enchants = slot.getObject("elemental-enchants", HashMap.class);
                for (String enchantName : enchants.keySet()) {
                    Enchant enchant = EnchantManager.getEnchant(enchantName);
                    PlayerEnchantManager.removeEnchantFromPlayer(player.getUniqueId(), enchant);
                }
            }
        }
        if (event.getCurrentItem() != null && !event.getCurrentItem().getType().equals(Material.AIR)) {
            current = new NBTItem(event.getCurrentItem());
            if (EnchantManager.hasEnchants(current)) {
                Map<String, Double> enchants = current.getObject("elemental-enchants", HashMap.class);
                for (String enchantName : enchants.keySet()) {
                    Enchant enchant = EnchantManager.getEnchant(enchantName);
                    int level = (int) Math.round(enchants.get(enchantName));
                    PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, level);
                }
            }
        }
    }
}
