package plugin.serverutilitiesplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.serverutilitiesplugin.Commands.ServerUtils;
import plugin.serverutilitiesplugin.Events.*;

public final class ServerUtilitiesPlugin extends JavaPlugin {

    public void commandRegister() {
        PluginCommand pluginCommand = Bukkit.getPluginCommand("serverutils");
        if (pluginCommand == null) return;
        pluginCommand.setExecutor(new ServerUtils(this));
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        commandRegister();
        Bukkit.getPluginManager().registerEvents(new CreeperExplode(this),this);
        Bukkit.getPluginManager().registerEvents(new FarmProtect(this),this);
        Bukkit.getPluginManager().registerEvents(new PlayerSleep(this),this);
        Bukkit.getPluginManager().registerEvents(new PortalEntry(this),this);
        Bukkit.getLogger().info("[ServerUtilitiesPlugin] Plugin Successfully Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ServerUtilitiesPlugin] Plugin Successfully Disabled!");
    }

}
