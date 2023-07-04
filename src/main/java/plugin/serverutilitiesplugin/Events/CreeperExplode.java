package plugin.serverutilitiesplugin.Events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CreeperExplode implements Listener {

    private final JavaPlugin plugin;
    public CreeperExplode(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        boolean creeperGriefing = plugin.getConfig().getBoolean("config.creeperGriefing");
        if (creeperGriefing) return;
        EntityType entityType = event.getEntityType();
        if (entityType == EntityType.CREEPER) {
            event.setCancelled(true);
        }
    }
}
