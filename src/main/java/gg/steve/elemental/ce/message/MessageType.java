package gg.steve.elemental.ce.message;

import gg.steve.elemental.ce.managers.Files;
import gg.steve.elemental.ce.utils.ColorUtil;
import gg.steve.elemental.tokens.ElementalTokens;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum MessageType {
    RELOAD("reload"),
    HELP("help"),
    ADD_ENCHANT_ADDER("add-enchant-adder", "{player}", "{enchant}", "{level}"),
    ADD_ENCHANT_RECEIVER("add-enchant-receiver", "{enchant}", "{level}"),
    UNENCHANTABLE_ITEM("unenchantable-item"),
    ENCHANT_MAX_LEVEL("enchant-max-level"),
    UPGRADE_SUCCESS("upgrade-success", "{enchant}", "{current-level}", "{max-level}"),
    LUCKY_BLOCK_DROP("lucky-block-drop", "{percent}"),
    INSUFFICIENT_TOKENS("insufficient-tokens", "{token-type}");

    private String path;
    private List<String> placeholders;

    MessageType(String path, String... placeholders) {
        this.path = path;
        this.placeholders = Arrays.asList(placeholders);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void message(Player receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.MESSAGES.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public void message(CommandSender receiver, String... replacements) {
        List<String> data = Arrays.asList(replacements);
        for (String line : Files.MESSAGES.get().getStringList(this.path)) {
            for (int i = 0; i < this.placeholders.size(); i++) {
                line = line.replace(this.placeholders.get(i), data.get(i));
            }
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public static void doMessage(Player receiver, List<String> lines) {
        for (String line : lines) {
            receiver.sendMessage(ColorUtil.colorize(line));
        }
    }

    public static void doMessage(Player receiver, List<String> lines, int amount) {
        for (String line : lines) {
            receiver.sendMessage(ColorUtil.colorize(line).replace("{amount}", ElementalTokens.getNumberFormat().format(amount)));
        }
    }
}