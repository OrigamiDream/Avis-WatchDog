package studio.avis.watchdog.commons;

import studio.avis.watchdog.api.suspicious.Suspicious;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class Reports implements Suspicious {
    
    public int limit = 0;
    
    public int reports = 0;
    public long lastReport = -1L;
}
