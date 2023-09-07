package plugin.serverutilitiesplugin.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import plugin.serverutilitiesplugin.ServerUtilitiesPlugin;

public class ServerListPing implements Listener {

    private final ServerUtilitiesPlugin plugin;
    public ServerListPing(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerPing(ServerListPingEvent event) {
        int maxPlayers = plugin.getConfig().getInt("config.maxPlayers");
        event.setMaxPlayers(maxPlayers);
    }

}
