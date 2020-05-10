package gg.steve.elemental.ce.cmd.sub;

import gg.steve.elemental.bps.permission.PermissionNode;
import gg.steve.elemental.ce.ElementalEnchants;
import gg.steve.elemental.ce.managers.Files;
import gg.steve.elemental.ce.message.CommandDebug;
import gg.steve.elemental.ce.message.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ReloadCmd {

    public static void reload(CommandSender sender) {
        if (!PermissionNode.RELOAD.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.RELOAD.get());
            return;
        }
        Files.reload();
        Bukkit.getPluginManager().disablePlugin(ElementalEnchants.get());
        Bukkit.getPluginManager().enablePlugin(ElementalEnchants.get());
        MessageType.RELOAD.message(sender);
    }
}
