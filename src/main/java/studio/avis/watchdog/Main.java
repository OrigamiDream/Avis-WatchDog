package studio.avis.watchdog;

import studio.avis.watchdog.packet.PacketHandler;
import studio.avis.watchdog.repeatable.RepeatHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class Main extends JavaPlugin {
    
    @Override
    public void onEnable() {
        
        new RepeatHandler(this);
        new PacketHandler(this);
        
        WatchDogFactory.loadDogs(this);
    }
    
    @Override
    public void onDisable() {
    
    }
}
