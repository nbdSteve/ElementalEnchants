package gg.steve.elemental.ce.cmd;

import gg.steve.elemental.ce.core.PlayerEnchantManager;
import gg.steve.elemental.ce.message.CommandDebug;
import gg.steve.elemental.ce.permission.PermissionNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            CommandDebug.ONLY_PLAYERS_ACCESSIBLE.message(sender);
            return true;
        }
        if (!PermissionNode.TE_GUI.hasPermission(sender)) {
            CommandDebug.INSUFFICIENT_PERMISSION.message(sender, PermissionNode.TE_GUI.get());
            return true;
        }
        Player player = (Player) sender;
        PlayerEnchantManager.getEnchantPlayer(player.getUniqueId()).openTokenEnchantsGui();
        return true;
    }
}
