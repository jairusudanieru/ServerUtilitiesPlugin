package plugin.serverutilitiesplugin.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.MoistureChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import plugin.serverutilitiesplugin.ServerUtilitiesPlugin;

import java.util.Arrays;
import java.util.List;

public class FarmProtect implements Listener {

    private final ServerUtilitiesPlugin plugin;
    public FarmProtect(ServerUtilitiesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCropsBreaking(EntityChangeBlockEvent event) {
        boolean noDryFarmland = plugin.getConfig().getBoolean("config.noDryFarmland");
        if (!noDryFarmland) return;
        Material material = event.getBlock().getType();
        if (material == Material.FARMLAND) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFarmlandMoisture(MoistureChangeEvent event) {
        boolean noDryFarmland = plugin.getConfig().getBoolean("config.noDryFarmland");
        if (!noDryFarmland) return;
        Block block = event.getBlock();
        Location aboveLocation = block.getLocation().add(0,1,0);
        Block blockAbove = aboveLocation.getBlock();
        if (blockAbove.getType() == Material.AIR) return;
        if (block.getType() == Material.FARMLAND) {
            BlockData blockData = block.getBlockData();
            Farmland farmland = (Farmland) blockData;
            if (farmland.getMoisture() <= 7) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFarmlandCreate(PlayerInteractEvent event) {
        boolean noDryFarmland = plugin.getConfig().getBoolean("config.noDryFarmland");
        if (!noDryFarmland) return;
        Action action = event.getAction();
        Block block = event.getClickedBlock();
        ItemStack itemStack = event.getItem();
        if (action.isLeftClick() || block == null || itemStack == null) return;

        List<Material> seeds = Arrays.asList(
                Material.BEETROOT_SEEDS,
                Material.CARROT,
                Material.MELON_SEEDS,
                Material.POTATO,
                Material.PUMPKIN_SEEDS,
                Material.WHEAT_SEEDS

        );

        Material blockType = block.getType();
        Material itemType = itemStack.getType();
        Location aboveLocation = block.getLocation().add(0,1,0);
        Block blockAbove = aboveLocation.getBlock();
        if (blockAbove.getType() != Material.AIR) return;
        if (blockType == Material.FARMLAND && seeds.contains(itemType)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                BlockData blockData = block.getBlockData();
                Farmland farmland = (Farmland) blockData;
                if (blockAbove.getType() == Material.AIR) return;
                farmland.setMoisture(7);
                block.setBlockData(farmland);
            }, 1);
        }

    }

}
