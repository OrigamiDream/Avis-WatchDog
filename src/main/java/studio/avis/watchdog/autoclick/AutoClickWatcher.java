package studio.avis.watchdog.autoclick;

import studio.avis.watchdog.api.WatchDog;
import studio.avis.watchdog.api.packet.PacketInfo;
import studio.avis.watchdog.api.packet.PacketListener;

import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class AutoClickWatcher extends WatchDog.Impl implements PacketListener {
    
    private static final int LIMIT_CLICK_PER_SECOND = 16;
    
    public AutoClickWatcher(JavaPlugin plugin) {
        super(plugin, "Auto clicking");
    }
    
    @Override
    public void in(PacketInfo info) {
        if(info.isCancelled())
            return;
        
        if(info.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = (PacketPlayInUseEntity) info.getPacket();
            if(packet.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                AutoClickSuspicious suspicious = suspected(info.getOwner(), AutoClickSuspicious.class);
                if(suspicious.lastHit == -1L) {
                    suspicious.lastHit = System.currentTimeMillis();
                } else {
                    long delay = System.currentTimeMillis() - suspicious.lastHit;
                    
                    suspicious.lastHit = System.currentTimeMillis();
                    suspicious.hitDelay = delay;
                    
                    if(suspicious.hitDelay < LIMIT_CLICK_PER_SECOND * 50) {
                        info.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @Override
    public void out(PacketInfo info) {
        // nothing do.
    }
    
    @Override
    public void overReported(String player) {
        // nothing do.
    }
    
    @Override
    public void onQuit(Player player) {
        suspiciouses().remove(player.getName());
    }
    
    @Override
    public String format(String playerName, String reason) {
        return null;
    }
}
