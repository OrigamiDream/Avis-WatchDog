package studio.avis.watchdog.autoclick;

import studio.avis.watchdog.api.suspicious.Suspicious;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class AutoClickSuspicious implements Suspicious {
    
    public long lastHit = -1l;
    public long hitDelay = -1l;
}
