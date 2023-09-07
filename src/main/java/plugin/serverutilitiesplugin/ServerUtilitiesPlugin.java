package plugin.serverutilitiesplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import plugin.serverutilitiesplugin.Commands.AFK;
import plugin.serverutilitiesplugin.Commands.Reply;
import plugin.serverutilitiesplugin.Commands.ServerUtils;
import plugin.serverutilitiesplugin.Commands.Whisper;
import plugin.serverutilitiesplugin.Events.*;

import java.util.Objects;

public final class ServerUtilitiesPlugin extends JavaPlugin {

    public void commandRegister() {
        Objects.requireNonNull(this.getCommand("afk")).setExecutor(new AFK(this));
        Objects.requireNonNull(this.getCommand("afk")).setTabCompleter(new AFK(this));
        Objects.requireNonNull(this.getCommand("reply")).setExecutor(new Reply(this));
        Objects.requireNonNull(this.getCommand("reply")).setTabCompleter(new Reply(this));
        Objects.requireNonNull(this.getCommand("serverutils")).setExecutor(new ServerUtils(this));
        Objects.requireNonNull(this.getCommand("serverutils")).setTabCompleter(new ServerUtils(this));
        Objects.requireNonNull(this.getCommand("whisper")).setExecutor(new Whisper(this));
        Objects.requireNonNull(this.getCommand("whisper")).setTabCompleter(new Whisper(this));
    }

    private void clearAFKTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team afkTeam = scoreboard.getTeam("afk");
        if (afkTeam == null) {
            Team team = scoreboard.registerNewTeam("afk");
            team.setColor(ChatColor.GRAY);
            afkTeam = team;
        }
        for (String entry : afkTeam.getEntries()) {
            afkTeam.removeEntry(entry);
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        commandRegister();
        clearAFKTeam();
        AFKManager afkManager = new AFKManager(this);
        afkManager.checkAFKPlayers();
        Bukkit.getPluginManager().registerEvents(new AFKChecker(this),this);
        Bukkit.getPluginManager().registerEvents(new CreeperExplode(this),this);
        Bukkit.getPluginManager().registerEvents(new FarmProtect(this),this);
        Bukkit.getPluginManager().registerEvents(new PlayerSleep(this),this);
        Bukkit.getPluginManager().registerEvents(new PortalEntry(this),this);
        Bukkit.getPluginManager().registerEvents(new ServerListPing(this),this);
        Bukkit.getLogger().info("[" + this.getName() + "] Plugin Successfully Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[" + this.getName() + "] Plugin Successfully Disabled!");
    }

}
