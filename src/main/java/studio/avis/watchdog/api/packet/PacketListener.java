package studio.avis.watchdog.api.packet;

/**
 * Created by Avis Network on 2017-06-20.
 */
public interface PacketListener {
    
    public void in(PacketInfo info);
    
    public void out(PacketInfo info);
    
}
