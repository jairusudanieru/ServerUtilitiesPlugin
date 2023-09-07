package plugin.serverutilitiesplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import plugin.serverutilitiesplugin.AFKManager;
import plugin.serverutilitiesplugin.ServerUtilitiesPlugin;

import java.util.ArrayList;
import java.util.List;

public class AFK implements CommandExecutor, TabCompleter {

    private final ServerUtilitiesPlugin plugin;
    private final AFKManager manager;
    public AFK(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
        this.manager = new AFKManager(plugin);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        manager.checkAFKTeam(player);
        return true;
    }

}
