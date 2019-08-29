package studio.avis.watchdog.api;

import studio.avis.watchdog.api.suspicious.Reportable;
import studio.avis.watchdog.api.suspicious.Suspicious;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Avis Network on 2017-06-20.
 */
public interface WatchDog {
    
    public Random random();
    
    public String getName();
    
    public JavaPlugin getPlugin();
    
    public boolean elapsed(long from, long required);
    
    public abstract class Impl extends Reportable.Impl implements WatchDog {
        
        private JavaPlugin plugin;
        private String watchDog;
        private Random random;
        
        private Map<String, Suspicious> suspiciouses = new ConcurrentHashMap<>();
    
        public Impl(JavaPlugin plugin, String watchDog) {
            this.plugin = plugin;
            this.watchDog = watchDog;
            this.random = new Random();
        }
        
        @Override
        public Random random() {
            return random;
        }
        
        @Override
        public String getName() {
            return watchDog;
        }
    
        @Override
        public JavaPlugin getPlugin() {
            return plugin;
        }
    
        @Override
        public boolean elapsed(long from, long required) {
            return System.currentTimeMillis() - from > required;
        }
    
        public Map<String, Suspicious> suspiciouses() {
            return this.suspiciouses;
        }
        
        public <T extends Suspicious> void suspect(Player player, Class<T> suspicious) {
            if(!suspiciouses.containsKey(player.getName())) {
                try {
                    T t = suspicious.newInstance();
                    suspiciouses.put(player.getName(), t);
                } catch(InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        
        public <T extends Suspicious> T suspected(Player player, Class<T> suspicious) {
            suspect(player, suspicious);
            return (T) suspiciouses.get(player.getName());
        }
        
        public void onQuit(Player player) {
        
        }
        
        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            suspiciouses.remove(event.getPlayer());
            
            onQuit(event.getPlayer());
        }
    }
    
}
