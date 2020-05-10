package gg.steve.elemental.ce.utils;

import gg.steve.elemental.ce.managers.Files;
import gg.steve.elemental.ce.message.MessageType;
import gg.steve.elemental.tokens.ElementalTokens;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class LuckyBlockUtil {
    private static List<Double> percentages;
    private static Map<Double, List<String>> lootTable;

    public static void initialise() {
        lootTable = new HashMap<>();
        percentages = new ArrayList<>();
        for (String percent : Files.CONFIG.get().getConfigurationSection("lucky-block-loot").getKeys(false)) {
            percentages.add(Double.parseDouble(percent));
            lootTable.put(Double.parseDouble(percent), new ArrayList<>(Files.CONFIG.get().getStringList("lucky-block-loot." + percent)));
        }
        LogUtil.info("Successfully loaded all lucky block loot tables.");
    }

    public static void calculateLoot(Player player) {
        double chance = Math.random() * percentages.get(0);
        double percentage = -1;
        for (int i = 0; i < percentages.size(); i++) {
            if (chance <= percentages.get(i) && i < percentages.size() - 1 && chance > percentages.get(i + 1)) {
                percentage = percentages.get(i);
            }
        }
        int drop = 0;
        if (lootTable.get(percentage).size() > 1) {
            drop = new Random().nextInt(lootTable.get(percentage).size() - 1);
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lootTable.get(percentage).get(drop).replace("{player}", player.getName()));
        MessageType.LUCKY_BLOCK_DROP.message(player, ElementalTokens.getNumberFormat().format(percentage));
    }
}
