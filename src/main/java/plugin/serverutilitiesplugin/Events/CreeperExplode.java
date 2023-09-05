package plugin.serverutilitiesplugin.Events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import plugin.serverutilitiesplugin.ServerUtilitiesPlugin;

public class CreeperExplode implements Listener {

    private final ServerUtilitiesPlugin plugin;
    public CreeperExplode(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        boolean antiCreeperGrief = plugin.getConfig().getBoolean("config.antiCreeperGrief");
        if (!antiCreeperGrief) return;
        EntityType entityType = event.getEntityType();
        if (entityType == EntityType.CREEPER) {
            event.setCancelled(true);
        }
    }
}
