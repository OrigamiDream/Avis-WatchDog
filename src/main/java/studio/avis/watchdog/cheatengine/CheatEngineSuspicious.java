package studio.avis.watchdog.cheatengine;

import studio.avis.watchdog.api.suspicious.Suspicious;

/**
 * Created by Avis Network on 2017-07-10.
 */
public class CheatEngineSuspicious implements Suspicious {
    
    public long updatePerSecond = -1l;
    public long updatePerTick = -1l;
    
    public int count = 0;
    public int stack = 0;
    
}
