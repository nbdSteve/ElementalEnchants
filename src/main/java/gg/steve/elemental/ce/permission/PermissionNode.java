package gg.steve.elemental.ce.permission;

import gg.steve.elemental.ce.managers.Files;
import org.bukkit.command.CommandSender;

public enum PermissionNode {
    ADD_ENCHANT("command.add-enchant"),
    PE_GUI("command.gui.prestige"),
    TE_GUI("command.gui.token"),
    RELOAD("command.reload"),
    HELP("command.help");

    private String path;

    PermissionNode(String path) {
        this.path = path;
    }

    public String get() {
        return Files.PERMISSIONS.get().getString(this.path);
    }

    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(get());
    }
}
