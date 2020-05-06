package gg.steve.elemental.ce.core;

import gg.steve.elemental.ce.nbt.NBTItem;
import gg.steve.elemental.ce.utils.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEnchantManager implements Listener {
    private static Map<UUID, EnchantPlayer> players;

    public static void initialise() {
        players = new HashMap<>();
    }

    public static void addEnchantPlayer(UUID playerId) {
        if (players.containsKey(playerId)) return;
        players.put(playerId, new EnchantPlayer(playerId));
        // load enchants from current item if they are holding one
        if (Bukkit.getPlayer(playerId) != null) {
            Player player = Bukkit.getPlayer(playerId);
            if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) return;
            NBTItem item = new NBTItem(player.getItemInHand());
            if (EnchantManager.hasEnchants(item)) {
                Map<String, Double> enchants = item.getObject("elemental-enchants", HashMap.class);
                for (String enchantName : enchants.keySet()) {
                    Enchant enchant = EnchantManager.getEnchant(enchantName);
                    int level = (int) Math.round(enchants.get(enchantName));
                    enchant.onHold(player, level);
                    PlayerEnchantManager.addEnchantToPlayer(player.getUniqueId(), enchant, level);
                }
            }
        }
    }

    public static void removeEnchantPlayer(UUID playerId) {
        if (!players.containsKey(playerId)) return;
        players.get(playerId).removeAllEnchants();
        players.remove(playerId);
    }

    public static void addEnchantToPlayer(UUID playerId, Enchant enchant, Integer enchantLevel) {
        if (!players.containsKey(playerId)) {
            addEnchantPlayer(playerId);
        }
        players.get(playerId).addEnchant(enchant, enchantLevel);
    }

    public static void removeEnchantFromPlayer(UUID playerId, Enchant enchant) {
        if (!players.containsKey(playerId)) return;
        players.get(playerId).removeEnchant(enchant);
    }

    public static boolean isEnchantActive(UUID playerId, Enchant enchant) {
        if (!players.containsKey(playerId)) return false;
        return players.get(playerId).isEnchantActive(enchant);
    }

    public static int getEnchantLevel(UUID playerId, Enchant enchant) {
        if (!players.containsKey(playerId)) return 0;
        return players.get(playerId).getEnchantLevel(enchant);
    }

    public static EnchantPlayer getEnchantPlayer(UUID playerId) {
        if (!players.containsKey(playerId)) addEnchantPlayer(playerId);
        return players.get(playerId);
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        // check the players hand to see if enchants should be applied.
        addEnchantPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        removeEnchantPlayer(event.getPlayer().getUniqueId());
        LogUtil.info("Removing disconnecting player " + event.getPlayer().getName() + " from the enchant map.");
    }
}
