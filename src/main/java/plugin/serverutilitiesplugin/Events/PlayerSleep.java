package plugin.serverutilitiesplugin.Events;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerSleep implements Listener {

    private final JavaPlugin plugin;
    public PlayerSleep(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSleep(PlayerDeepSleepEvent event) {
        boolean onePlayerSleep = plugin.getConfig().getBoolean("config.onePlayerSleep");
        if (!onePlayerSleep) return;
        Player player = event.getPlayer();
        String playerName = player.getName();
        boolean isDayTime = player.getWorld().isDayTime();
        boolean isThundering = player.getWorld().isThundering();
        boolean isSleeping = player.isSleeping();
        boolean anyOtherPlayerSleeping = Bukkit.getOnlinePlayers().stream()
                .filter(otherPlayer -> !otherPlayer.equals(player))
                .anyMatch(Player::isSleeping);
        if (anyOtherPlayerSleeping) return;

        String message = plugin.getConfig().getString("messages.sleepMessage");
        if (message != null) message = message.replace("%player%",playerName).replace("&","§");

        if (isDayTime && !isThundering || !isSleeping) return;
        player.getWorld().setTime(1000);
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (message == null) return;
            onlinePlayers.sendMessage(Component.text(message));
        }
    }

}
