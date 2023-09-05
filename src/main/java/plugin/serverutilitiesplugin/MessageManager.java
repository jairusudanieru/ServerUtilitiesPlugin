package plugin.serverutilitiesplugin;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MessageManager {

    public static HashMap<UUID, UUID> lastReceived = new HashMap<>();

    public void senderSendMessage(Player sender, Player target, String message, ServerUtilitiesPlugin plugin) {
        String senderMessage = plugin.getConfig().getString("messages.senderMessage");
        if (senderMessage != null) {
            senderMessage = senderMessage.replace("&","§")
                    .replace("%sender%", sender.getName())
                    .replace("%target%", target.getName())
                    .replace("%message%", message);
            sender.sendMessage(senderMessage);
        }
    }

    public void targetSendMessage(Player sender, Player target, String message, ServerUtilitiesPlugin plugin) {
        String targetMessage = plugin.getConfig().getString("messages.targetMessage");
        if (targetMessage != null) {
            targetMessage = targetMessage.replace("&","§")
                    .replace("%sender%", sender.getName())
                    .replace("%target%", target.getName())
                    .replace("%message%", message);
            target.sendMessage(targetMessage);
        }
    }

}
