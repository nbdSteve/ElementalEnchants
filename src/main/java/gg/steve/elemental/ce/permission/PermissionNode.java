package gg.steve.elemental.ce.permission;

import gg.steve.elemental.ce.managers.Files;
import org.bukkit.command.CommandSender;

public enum PermissionNode {
    PURCHASE("purchase.node"),
    GUI("command.gui"),
    RELOAD("command.reload"),
    GIVE("command.give"),
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
