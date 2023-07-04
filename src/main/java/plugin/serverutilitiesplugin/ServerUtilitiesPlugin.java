package plugin.serverutilitiesplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.serverutilitiesplugin.Commands.ServerUtils;
import plugin.serverutilitiesplugin.Events.*;

public final class ServerUtilitiesPlugin extends JavaPlugin {

    public void onCommandRegister() {
        PluginCommand pluginCommand = Bukkit.getPluginCommand("serverutils");
        if (pluginCommand == null) return;
        pluginCommand.setExecutor(new ServerUtils(this));
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        onCommandRegister();
        Bukkit.getPluginManager().registerEvents(new CreeperExplode(),this);
        Bukkit.getPluginManager().registerEvents(new FarmProtect(this),this);
        Bukkit.getPluginManager().registerEvents(new PlayerSleep(this),this);
        Bukkit.getPluginManager().registerEvents(new PortalEntry(this),this);
        Bukkit.getLogger().info("Server Utilities successfully Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Server Utilities successfully Disabled!");
    }

}
