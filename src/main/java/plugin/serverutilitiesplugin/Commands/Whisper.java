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

public class Whisper implements CommandExecutor, TabCompleter {

    private final MessageManager manager = new MessageManager();
    private final ServerUtilitiesPlugin plugin;
    public Whisper(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> players = new ArrayList<>();
        if (args.length <= 1) {
            Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
            return players;
        }
        return players;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 2) {
            String message = plugin.getConfig().getString("messages.whisperUsage");
            if (message == null) return true;
            message = message.replaceFirst("%s", label);
            message = message.replace("&","§");
            sender.sendMessage(message);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            String message = plugin.getConfig().getString("messages.playerNotFound");
            if (message == null) return true;
            message = message.replace("&","§");
            sender.sendMessage(message);
            return true;
        }

        String message = StringUtils.join(args, " ");
        message = message.trim();
        message = message.substring(args[0].length() + 1);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            manager.senderSendMessage(player, target, message, plugin);
            manager.targetSendMessage(player, target, message, plugin);
            MessageManager.lastReceived.put(player.getUniqueId(), target.getUniqueId());
            MessageManager.lastReceived.put(target.getUniqueId(), player.getUniqueId());
        } else {
            MessageManager.lastReceived.put(target.getUniqueId(), null);
        }
        return true;
    }

}
