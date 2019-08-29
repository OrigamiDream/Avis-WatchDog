package studio.avis.watchdog.killaura;

import studio.avis.watchdog.api.suspicious.Suspicious;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class KillauraSuspicious implements Suspicious {
    
    public int hits;
    public long timer = System.currentTimeMillis();
    
    public long lastHit = -1L;
    public long hitDelay = -1L;
    
    public int stacks = 0;
    
}
