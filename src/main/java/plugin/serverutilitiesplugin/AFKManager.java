package plugin.serverutilitiesplugin;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class AFKManager {

    public static final HashMap<Player, String> afkStatus = new HashMap<>();
    public static final HashMap<Player, Long> lastMovementTimestamps = new HashMap<>();
    private final ServerUtilitiesPlugin plugin;
    public AFKManager(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkAFKTeam(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team afkTeam = scoreboard.getTeam("afk");
        if (afkTeam == null) {
            Team team = scoreboard.registerNewTeam("afk");
            team.color(NamedTextColor.GRAY);
            afkTeam = team;
        }

        String isAFK = plugin.getConfig().getString("messages.playerIsAFK");
        String isNotAFK = plugin.getConfig().getString("messages.playerIsNotAFK");
        boolean isPlayerInTeam = afkTeam.hasEntry(player.getName());
        if (isPlayerInTeam) {
            AFKManager.afkStatus.remove(player);
            afkTeam.removeEntry(player.getName());
            if (isNotAFK == null || isNotAFK.isEmpty()) return;
            isNotAFK = isNotAFK.replace("%player%", player.getName()).replace("&", "§");
            player.sendMessage(isNotAFK);
            this.announceNotAFKToOthers(player);
        } else {
            AFKManager.afkStatus.put(player, "AFK");
            afkTeam.addEntry(player.getName());
            if (isAFK == null || isAFK.isEmpty()) return;
            isAFK = isAFK.replace("%player%", player.getName()).replace("&", "§");
            player.sendMessage(isAFK);
            this.announceAFKToOthers(player);
        }
    }

    public void setAfkStatus(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Team afkTeam = scoreboard.getTeam("afk");
        if (afkTeam == null) {
            Team team = scoreboard.registerNewTeam("afk");
            team.color(NamedTextColor.GRAY);
            afkTeam = team;
        }

        String isAFK = plugin.getConfig().getString("messages.playerIsAFK");
        AFKManager.afkStatus.put(player, "AFK");
        afkTeam.addEntry(player.getName());
        if (isAFK == null || isAFK.isEmpty()) return;
        isAFK = isAFK.replace("%player%", player.getName()).replace("&", "§");
        player.sendMessage(isAFK);
        this.announceAFKToOthers(player);
    }

    public void announceAFKToOthers(Player player) {
        String announceIsAFK = plugin.getConfig().getString("messages.announceIsAFK");
        if (announceIsAFK == null || announceIsAFK.isEmpty()) return;
        announceIsAFK = announceIsAFK.replace("%player%", player.getName()).replace("&", "§");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue;
            onlinePlayer.sendMessage(announceIsAFK);
        }
    }

    public void announceNotAFKToOthers(Player player) {
        String announceNotAFK = plugin.getConfig().getString("messages.announceIsNotAFK");
        if (announceNotAFK == null || announceNotAFK.isEmpty()) return;
        announceNotAFK = announceNotAFK.replace("%player%", player.getName()).replace("&", "§");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer == player) continue;
            onlinePlayer.sendMessage(announceNotAFK);
        }
    }

    public void checkAFKPlayers() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isAFK(player)) {
                    if (afkStatus.containsKey(player)) continue;
                    setAfkStatus(player);
                }
            }
        }, 0L, 20L);
    }

    private boolean isAFK(Player player) {
        long afkTimeout = plugin.getConfig().getLong("config.afkTime") * 1000;
        if (afkTimeout == 0) afkTimeout = 300000;
        long currentTime = System.currentTimeMillis();
        long lastMovementTime = lastMovementTimestamps.getOrDefault(player, currentTime);
        return currentTime - lastMovementTime >= afkTimeout;
    }

}
