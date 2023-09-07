package plugin.serverutilitiesplugin.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import plugin.serverutilitiesplugin.AFKManager;
import plugin.serverutilitiesplugin.ServerUtilitiesPlugin;

public class AFKChecker implements Listener {

    private final ServerUtilitiesPlugin plugin;
    private final AFKManager manager;
    public AFKChecker(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
        this.manager = new AFKManager(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        AFKManager.lastMovementTimestamps.remove(player);
        if (!AFKManager.afkStatus.containsKey(player)) return;
        AFKManager.afkStatus.remove(player);
        manager.checkAFKTeam(player);
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.hasChangedOrientation()) return;
        AFKManager.lastMovementTimestamps.put(player, System.currentTimeMillis());
        if (!AFKManager.afkStatus.containsKey(player)) return;
        AFKManager.afkStatus.remove(player);
        manager.checkAFKTeam(player);
    }

}
