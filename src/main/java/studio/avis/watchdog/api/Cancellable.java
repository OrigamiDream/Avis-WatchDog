package studio.avis.watchdog.api;

/**
 * Created by Avis Network on 2017-06-20.
 */
public interface Cancellable {
    
    public void setCancelled(boolean cancelled);
    
    public boolean isCancelled();
    
}
