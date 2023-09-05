package plugin.serverutilitiesplugin.Commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.serverutilitiesplugin.MessageManager;
import plugin.serverutilitiesplugin.ServerUtilitiesPlugin;

import java.util.ArrayList;
import java.util.List;

public class Reply implements CommandExecutor, TabCompleter {

    private final MessageManager manager = new MessageManager();
    private final ServerUtilitiesPlugin plugin;
    public Reply(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            String message = plugin.getConfig().getString("messages.cantReply");
            if (message == null) return true;
            message = message.replace("&","§");
            sender.sendMessage(message);
            return true;
        }

        Player player = (Player) sender;
        if (!MessageManager.lastReceived.containsKey(player.getUniqueId()) || MessageManager.lastReceived.get(player.getUniqueId()) == null) {
            String message = plugin.getConfig().getString("messages.noReply");
            if (message == null) return true;
            message = message.replace("&","§");
            player.sendMessage(message);
            return true;
        }

        Player target = Bukkit.getPlayer(MessageManager.lastReceived.get(player.getUniqueId()));
        if (target == null) {
            String message = plugin.getConfig().getString("messages.playerNotFound");
            if (message == null) return true;
            message = message.replace("&","§");
            player.sendMessage(message);
            return true;
        }

        if (args.length < 1) {
            String message = plugin.getConfig().getString("messages.whisperUsage");
            if (message == null) return true;
            message = message.replace("&","§");
            sender.sendMessage(message);
            return true;
        }

        String message = StringUtils.join(args, " ");
        message = message.trim();
        manager.senderSendMessage(player, target, message, plugin);
        manager.targetSendMessage(player, target, message, plugin);
        MessageManager.lastReceived.put(player.getUniqueId(), target.getUniqueId());
        MessageManager.lastReceived.put(target.getUniqueId(), player.getUniqueId());
        return true;
    }

}
