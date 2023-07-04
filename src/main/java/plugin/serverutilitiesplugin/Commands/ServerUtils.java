package plugin.serverutilitiesplugin.Commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ServerUtils implements TabCompleter, CommandExecutor {

    private final JavaPlugin plugin;
    public ServerUtils(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        boolean isOperator = sender.isOp();
        boolean isConsole = sender instanceof ConsoleCommandSender;
        if (!command.getName().equalsIgnoreCase("serverutils")) return Collections.emptyList();
        if (!isOperator && !isConsole) return Collections.emptyList();
        if (args.length == 1) {
            List<String> args1 = new ArrayList<>();
            args1.add("reload");
            return args1;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String message = plugin.getConfig().getString("messages.reloadMessage");
        if (!command.getName().equalsIgnoreCase("serverutils")) return false;
        if (args.length == 1 && args[0].equals("reload")) {
            plugin.reloadConfig();
            if (message == null) return true;
            message = message.replace("&","§");
            sender.sendMessage(Component.text(message));
            return true;
        }
        return false;
    }

}
