package studio.avis.watchdog.api.suspicious;

import studio.avis.watchdog.commons.Reports;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by Avis Network on 2017-06-20.
 */
public interface Reportable {
    
    public void report(String playerName, String reason, Predicate<Player> predicate);
    
    public Map<String, Reports> reports();
    
    public abstract class Impl implements Reportable {
        
        private Map<String, Reports> reports = new HashMap<>();
        
        @Override
        public void report(String playerName, String reason, Predicate<Player> predicate) {
            for(Player online : Bukkit.getOnlinePlayers()) {
                if(predicate.test(online)) {
                    online.sendMessage(format(playerName, reason));
                }
            }
        }
    
        @Override
        public Map<String, Reports> reports() {
             return reports;
        }
    
        public void plus(String playerName, int limit) {
            if(!reports.containsKey(playerName)) {
                reports.put(playerName, new Reports());
            }
            
            Reports report = reports.get(playerName);
            report.limit = limit;
            report.reports++;
            report.lastReport = System.currentTimeMillis();
            
            if(report.reports >= report.limit) {
                overReported(playerName);
            }
        }
        
        public abstract void overReported(String player);
        
        public void invalidate(String playerName) {
            reports.remove(playerName);
        }
        
        public abstract String format(String playerName, String reason);
    }
    
}
