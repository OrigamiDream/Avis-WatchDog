package studio.avis.watchdog.packet;

import studio.avis.watchdog.api.packet.PacketListener;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class PacketHandler implements Listener {
    
    private static PacketHandler instance;
    
    private List<PacketListener> listeners = new ArrayList<PacketListener>();
    
    public PacketHandler(JavaPlugin plugin) {
        instance = this;
        
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public static PacketHandler self() {
        return instance;
    }
    
    public void addPacketListener(PacketListener listener) {
        listeners.add(listener);
    }
    
    public void removePacketListener(PacketListener listener) {
        listeners.remove(listener);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        EntityPlayer player = ((CraftPlayer) event.getPlayer()).getHandle();
        PlayerConnection original = player.playerConnection;
        // Replace to dummy
        player.playerConnection = new PacketConnection(this.listeners, player.server, original.networkManager, player);
    }
    
}
