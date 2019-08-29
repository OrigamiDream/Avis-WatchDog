package studio.avis.watchdog.fastbow;

import studio.avis.watchdog.api.suspicious.Suspicious;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class FastbowSuspicious implements Suspicious {
    
    public int shots = 0;
    public long timer = System.currentTimeMillis();
    
    public long lastShot = -1l;
    public long shotDelay = -1l;
    
    public int stacks = 0;
}
