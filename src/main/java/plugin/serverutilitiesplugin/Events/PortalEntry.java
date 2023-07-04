package plugin.serverutilitiesplugin.Events;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class PortalEntry implements Listener {

    private final JavaPlugin plugin;
    private final Map<Player, Long> messageCooldown = new HashMap<>();
    public PortalEntry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNetherPortalEntry(PlayerPortalEvent event) {
        boolean netherEnabled = plugin.getConfig().getBoolean("config.world_nether");
        if (netherEnabled) return;
        Player player = event.getPlayer();
        String playerName = player.getName();

        String message = plugin.getConfig().getString("messages.netherDisabled");
        if (message == null) return;
        message = message.replace("%player%",playerName).replace("&","§");

        if (event.getCause() != TeleportCause.NETHER_PORTAL) return;
        event.setCancelled(true);
        player.sendMessage(Component.text(message));
    }

    @EventHandler
    public void onEndPortalEntry(PlayerPortalEvent event) {
        boolean endEnabled = plugin.getConfig().getBoolean("config.world_the_end");
        if (endEnabled) return;
        Player player = event.getPlayer();
        String playerName = player.getName();

        String message = plugin.getConfig().getString("messages.endDisabled");
        if (message == null) return;
        message = message.replace("%player%",playerName).replace("&","§");

        Location worldSpawn = player.getWorld().getSpawnLocation();
        Location bedSpawn = player.getBedSpawnLocation();

        if (event.getCause() != TeleportCause.END_PORTAL) return;
        event.setCancelled(true);
        String finalMessage = message;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (bedSpawn != null) {
                player.teleport(bedSpawn);
            } else {
                player.teleport(worldSpawn);
            }
            if (messageCooldown.containsKey(player)) {
                long lastSentTime = messageCooldown.get(player);
                long currentTime = System.currentTimeMillis();
                long cooldown = 5000;
                if (currentTime - lastSentTime < cooldown) return;
            }
            messageCooldown.put(player, System.currentTimeMillis());
            player.sendMessage(Component.text(finalMessage));
        }, 1);
    }

    @EventHandler
    public void onEndPortalActivate(PlayerInteractEvent event) {
        boolean endEnabled = plugin.getConfig().getBoolean("config.world_the_end");
        if (endEnabled) return;
        Player player = event.getPlayer();
        String playerName = player.getName();

        String message = plugin.getConfig().getString("config.endDisabled");
        if (message == null) return;
        message = message.replace("%player%",playerName).replace("&","§");

        Action action = event.getAction();
        Block block = event.getClickedBlock();
        ItemStack itemStack = event.getItem();
        if (itemStack == null || block == null || action.isLeftClick()) return;

        Material clickedBlock = block.getType();
        Material holdingItem = itemStack.getType();
        if (clickedBlock == Material.END_PORTAL_FRAME && holdingItem == Material.ENDER_EYE) {
            event.setCancelled(true);
            player.sendMessage(Component.text(message));
        }
    }

}
