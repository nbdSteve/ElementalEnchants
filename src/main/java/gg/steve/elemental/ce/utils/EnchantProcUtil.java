package gg.steve.elemental.ce.utils;

import gg.steve.elemental.ce.message.MessageType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class EnchantProcUtil {

    public static void doProc(ConfigurationSection section, Player player) {
        if (section.getBoolean("message.enabled")) {
            MessageType.doMessage(player, section.getStringList("message.text"));
        }
        SoundUtil.playSound(section, player);
    }

    public static void doProc(ConfigurationSection section, Player player, int amount) {
        if (section.getBoolean("message.enabled")) {
            MessageType.doMessage(player, section.getStringList("message.text"), amount);
        }
        SoundUtil.playSound(section, player);
    }

    public static void doRemove(ConfigurationSection section, Player player) {
        if (section.getBoolean("message.enabled")) {
            MessageType.doMessage(player, section.getStringList("message.remove"));
        }
    }
}
