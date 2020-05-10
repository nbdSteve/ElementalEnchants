package gg.steve.elemental.ce.cmd.sub;

import gg.steve.elemental.ce.message.CommandDebug;
import gg.steve.elemental.ce.message.MessageType;
import gg.steve.elemental.ce.permission.PermissionNode;
import org.bukkit.command.CommandSender;

public class HelpCmd {

    public static void help(CommandSender sender) {
        if (!PermissionNode.HELP.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.HELP.get());
            return;
        }
        MessageType.HELP.message(sender);
    }
}
