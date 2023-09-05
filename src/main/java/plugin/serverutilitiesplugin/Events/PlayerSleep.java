package plugin.serverutilitiesplugin.Events;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.serverutilitiesplugin.ServerUtilitiesPlugin;

public class PlayerSleep implements Listener {

    private final ServerUtilitiesPlugin plugin;
    public PlayerSleep(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerSleep(PlayerDeepSleepEvent event) {
        boolean onePlayerSleep = plugin.getConfig().getBoolean("config.onePlayerSleep");
        if (!onePlayerSleep) return;
        Player player = event.getPlayer();
        World world = player.getWorld();
        String playerName = player.getName();

        boolean isDayTime = player.getWorld().isDayTime();
        boolean isThundering = player.getWorld().isThundering();
        boolean isSleeping = player.isSleeping();
        boolean anyOtherPlayerSleeping = Bukkit.getOnlinePlayers().stream()
                .filter(otherPlayer -> !otherPlayer.equals(player))
                .anyMatch(Player::isSleeping);
        if (anyOtherPlayerSleeping) return;
        if (isDayTime && !isThundering || !isSleeping) return;

        String message = plugin.getConfig().getString("messages.sleepMessage");
        if (message != null) {
            message = message.replace("%player%",playerName).replace("&","§");
        }

        boolean playerSleepWarp = plugin.getConfig().getBoolean("config.playerSleepWarp");
        if (playerSleepWarp) {
            player.wakeup(false);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> sleepWarpEnabled(player, world), 0);
        } else {
            player.getWorld().setTime(0);
        }

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (message == null) return;
            onlinePlayers.sendMessage(Component.text(message));
        }
    }

    private void sleepWarpEnabled(Player player, World world) {
        timeAccelerate(world);
        world.setStorm(false);
        world.setThundering(false);
        player.setStatistic(Statistic.TIME_SINCE_REST, 0);
    }

    private void timeAccelerate(World world) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (world.getTime() < 12010) {
                    cancel();
                    return;
                }
                world.setTime(world.getTime() + 100);
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
