package studio.avis.watchdog.cheatengine;

import studio.avis.watchdog.api.WatchDog;
import studio.avis.watchdog.api.packet.PacketInfo;
import studio.avis.watchdog.api.packet.PacketListener;
import studio.avis.watchdog.packet.PacketHandler;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Avis Network on 2017-07-10.
 */
public class CheatEngineWatcher extends WatchDog.Impl implements PacketListener {
    
    public CheatEngineWatcher(JavaPlugin plugin) {
        super(plugin, "Cheat Engine");
    
        PacketHandler.self().addPacketListener(this);
    }
    
    @Override
    public void overReported(String player) {
        invalidate(player);
        // AvisPunish supports
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + player + " [WATCH DOG] Cheat Engine");
    }
    
    @Override
    public String format(String playerName, String reason) {
        return playerName + ChatColor.WHITE + " has been reported for " + ChatColor.AQUA + reason;
    }
    
    @Override
    public void onQuit(Player player) {
        invalidate(player.getName());
        suspiciouses().remove(player.getName());
    }
    
    @Override
    public void in(PacketInfo info) {
        if(!info.getOwner().isOnline()) {
            return;
        }
        Player player = info.getOwner();
        if(info.getPacket() instanceof PacketPlayInFlying) {
            CheatEngineSuspicious suspicious = suspected(player, CheatEngineSuspicious.class);
            if(suspicious.updatePerSecond == -1l || suspicious.updatePerTick == -1l) {
                suspicious.updatePerTick = System.currentTimeMillis();
                suspicious.updatePerSecond = System.currentTimeMillis();
                return;
            }
            
            long diff = System.currentTimeMillis() - suspicious.updatePerSecond;
            if(diff == 0) {
                suspicious.count++;
                if(suspicious.count % 100 == 0) {
                    suspicious.stack++;
                }
            }
            
            if(elapsed(suspicious.updatePerSecond, 5000l)) {
                if(suspicious.stack >= 3) {
                    report(player.getName(),
                            "[WATCH DOG] " + player + " attacked fake player " + suspicious.stack + " times in 5 seconds.",
                            viewer -> viewer.hasPermission("avis.reports.cheatengine"));
                } else if(suspicious.stack >= 5) {
                    overReported(player.getName());
                }
                suspicious.count = 0;
                suspicious.stack = 0;
                suspicious.updatePerSecond = System.currentTimeMillis();
            }
            suspicious.updatePerTick = System.currentTimeMillis();
        }
    }
    
    @Override
    public void out(PacketInfo info) {
        // nothing do.
    }
}
