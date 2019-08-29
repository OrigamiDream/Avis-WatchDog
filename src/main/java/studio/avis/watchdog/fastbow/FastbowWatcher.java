package studio.avis.watchdog.fastbow;

import studio.avis.watchdog.api.WatchDog;
import studio.avis.watchdog.api.repeatable.IRepeatable;
import studio.avis.watchdog.repeatable.RepeatHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class FastbowWatcher extends WatchDog.Impl implements IRepeatable {
    
    public FastbowWatcher(JavaPlugin plugin) {
        super(plugin, "Fastbow");
        
        RepeatHandler.self().addRepeatable(this);
    }
    
    @Override
    public void onQuit(Player player) {
        invalidate(player.getName());
        suspiciouses().remove(player.getName());
    }
    
    @Override
    public void onTick() {
        tickCalculate();
        tickReports();
    }
    
    void tickCalculate() {
        Iterator<String> iterator = suspiciouses().keySet().iterator();
        while(iterator.hasNext()) {
            String player = iterator.next();
            
            FastbowSuspicious suspicious = (FastbowSuspicious) suspiciouses().get(player);
            if(!elapsed(suspicious.timer, 5000L))
                continue;
            
            if(suspicious.stacks > 5) {
                if(suspicious.stacks < 10) {
                    report(player,
                            "[WATCH DOG] " + player + " shot " + suspicious.stacks + " times in 5 seconds.",
                            viewer -> viewer.hasPermission("avis.reports.fastbow")
                    );
                    plus(player, 4);
                } else {
                    overReported(player);
                }
            }
            
            if(suspicious.shots >= 50) {
                overReported(player);
            }
            iterator.remove();
        }
    }
    
    void tickReports() {
        reports().keySet().removeIf(s -> elapsed(reports().get(s).lastReport, 12000L));
    }
    
    @Override
    public void overReported(String player) {
        invalidate(player);
        // AvisPunish supports
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + player + " [WATCH DOG] Fastbow");
    }
    
    @Override
    public String format(String playerName, String reason) {
        return playerName + ChatColor.WHITE + " has been reported " + ChatColor.AQUA + reason;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onShootbow(EntityShootBowEvent event) {
        if(!(event.getEntity() instanceof Player) || event.isCancelled() || event.getForce() != -1F)
            return;
        
        Player player = (Player) event.getEntity();
        FastbowSuspicious suspicious = suspected(player, FastbowSuspicious.class);
        
        suspicious.shots++;
        if(suspicious.lastShot != -1l) {
            suspicious.lastShot = System.currentTimeMillis();
        } else {
            long delay = System.currentTimeMillis() - suspicious.lastShot;
            
            suspicious.lastShot = System.currentTimeMillis();
            suspicious.shotDelay = delay;
            
            if(suspicious.shotDelay < 100l) {
                suspicious.stacks++;
            }
        }
    }
}
