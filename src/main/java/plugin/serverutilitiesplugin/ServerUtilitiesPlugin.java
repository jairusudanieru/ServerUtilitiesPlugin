package plugin.serverutilitiesplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.serverutilitiesplugin.Commands.Reply;
import plugin.serverutilitiesplugin.Commands.ServerUtils;
import plugin.serverutilitiesplugin.Commands.Whisper;
import plugin.serverutilitiesplugin.Events.*;

import java.util.Objects;

public final class ServerUtilitiesPlugin extends JavaPlugin {

    public void commandRegister() {
        Objects.requireNonNull(this.getCommand("serverutils")).setExecutor(new ServerUtils(this));
        Objects.requireNonNull(this.getCommand("serverutils")).setTabCompleter(new ServerUtils(this));
        Objects.requireNonNull(this.getCommand("whisper")).setExecutor(new Whisper(this));
        Objects.requireNonNull(this.getCommand("whisper")).setTabCompleter(new Whisper(this));
        Objects.requireNonNull(this.getCommand("reply")).setExecutor(new Reply(this));
        Objects.requireNonNull(this.getCommand("reply")).setTabCompleter(new Reply(this));
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        commandRegister();
        Bukkit.getPluginManager().registerEvents(new CreeperExplode(this),this);
        Bukkit.getPluginManager().registerEvents(new FarmProtect(this),this);
        Bukkit.getPluginManager().registerEvents(new PlayerSleep(this),this);
        Bukkit.getPluginManager().registerEvents(new PortalEntry(this),this);
        Bukkit.getLogger().info("[" + this.getName() + "] Plugin Successfully Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[" + this.getName() + "] Plugin Successfully Disabled!");
    }

}
