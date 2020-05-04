package gg.steve.elemental.ce.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantPlayer {
    private UUID playerId;
    private Map<Enchant, Integer> activeEnchants;

    public EnchantPlayer(UUID playerId) {
        this.playerId = playerId;
        this.activeEnchants = new HashMap<>();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }

    public void addEnchant(Enchant enchant, int enchantLevel) {
        this.activeEnchants.put(enchant, enchantLevel);
    }

    public void removeEnchant(Enchant enchant) {
        if (!this.activeEnchants.containsKey(enchant)) return;
        enchant.remove(this.getPlayer(), this.getEnchantLevel(enchant));
        this.activeEnchants.remove(enchant);
    }

    public boolean isEnchantActive(Enchant enchant) {
        return this.activeEnchants.containsKey(enchant);
    }

    public int getEnchantLevel(Enchant enchant) {
        return this.activeEnchants.get(enchant);
    }
}
