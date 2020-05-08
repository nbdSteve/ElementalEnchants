package gg.steve.elemental.ce.utils;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SoundUtil {

    public static void playSound(ConfigurationSection section, Player player) {
        if (section.getBoolean("sound.enabled")) {
            player.playSound(player.getLocation(),
                    Sound.valueOf(section.getString("sound.type").toUpperCase()),
                    section.getInt("sound.volume"),
                    section.getInt("sound.pitch"));
        }
    }
}
