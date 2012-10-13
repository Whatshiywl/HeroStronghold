package multitallented.redcastlemedia.bukkit.herostronghold.listeners;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import multitallented.redcastlemedia.bukkit.herostronghold.HeroStronghold;
import multitallented.redcastlemedia.bukkit.herostronghold.region.SuperRegion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author Multitallented
 */
public class SendMessageThread implements Runnable {
    private final Map<Player, String> channels;
    private final Player player;
    private final String channel;
    private final String message;
    private final String title;
    private final HeroStronghold plugin;

    public SendMessageThread(HeroStronghold plugin, String channel, Map<Player, String> channels, String title, Player player, String message) {
        this.channels = channels;
        this.player = player;
        this.channel = channel;
        this.message = message;
        this.plugin = plugin;
        if (title == null || title.equals("")) {
            this.title = "";
        } else {
            this.title = title + ", ";
        }
    }

    @Override
    public void run() {
        int i=0;
        Set<Player> removeThese = new HashSet<Player>();
        /*for (Player p : channels.keySet()) {
            if (!p.isOnline()) {
                removeThese.add(p);
            } else if (channels.get(p).equals(channel)) {
                p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + channel + ChatColor.GRAY + "]" + title + player.getDisplayName()
                        + ": " + message);
                i++;
                sent.add(p);
            }
        }*/
        SuperRegion sr = plugin.getRegionManager().getSuperRegion(channel);
        if (sr != null) {
            for (String s : sr.getAllMembers().keySet()) {
                Player p = plugin.getServer().getPlayer(s);
                if (p != null) {
                    p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + channel + ChatColor.GRAY + "]" + title + player.getDisplayName()
                            + ": " + message);
                    i++;
                }
            }
            for (String s : sr.getAllOwners()) {
                Player p = plugin.getServer().getPlayer(s);
                if (p != null) {
                    p.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + channel + ChatColor.GRAY + "]" + title + player.getDisplayName()
                            + ": " + message);
                    i++;
                }
            }
        }
        for (Player p : removeThese) {
            channels.remove(p);
        }
        if (i<=1) {
            player.sendMessage(ChatColor.GOLD + "[" + channel + "] No hears you. You are alone in this channel.");
        }
    }
    
}
