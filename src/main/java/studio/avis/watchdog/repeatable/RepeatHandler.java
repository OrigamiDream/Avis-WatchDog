package studio.avis.watchdog.repeatable;

import studio.avis.watchdog.api.repeatable.IRepeatable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class RepeatHandler {
    
    private static RepeatHandler instance;
    
    private List<IRepeatable> repeatableList = new ArrayList<IRepeatable>();
    
    public RepeatHandler(JavaPlugin plugin) {
        instance = this;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for(IRepeatable repeatable : repeatableList) {
                repeatable.onTick();
            }
        },0L, 1L);
    }
    
    public static RepeatHandler self() {
        return instance;
    }
    
    public void addRepeatable(IRepeatable repeatable) {
        repeatableList.add(repeatable);
    }
    
    public void removeRepeatable(IRepeatable repeatable) {
        repeatableList.remove(repeatable);
    }
}
