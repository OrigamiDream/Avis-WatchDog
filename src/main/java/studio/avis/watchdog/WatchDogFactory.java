package studio.avis.watchdog;

import studio.avis.watchdog.autoclick.AutoClickWatcher;
import studio.avis.watchdog.cheatengine.CheatEngineWatcher;
import studio.avis.watchdog.fastbow.FastbowWatcher;
import studio.avis.watchdog.killaura.KillauraWatcher;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class WatchDogFactory {
    
    public static void loadDogs(JavaPlugin plugin) {
        new KillauraWatcher(plugin);
        new FastbowWatcher(plugin);
        new AutoClickWatcher(plugin);
        new CheatEngineWatcher(plugin);
    }
}