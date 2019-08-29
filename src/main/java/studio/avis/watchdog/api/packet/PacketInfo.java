package studio.avis.watchdog.api.packet;

import studio.avis.watchdog.api.Cancellable;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

/**
 * Created by Avis Network on 2017-06-20.
 */
public interface PacketInfo extends Cancellable {
    
    public Player getOwner();
    
    public Packet getPacket();
    
    public void setPacket(Packet packet);
    
    public class Impl implements PacketInfo {
        
        private Player owner;
        private Packet packet;
        private boolean cancelled = false;
        
        public Impl(Player owner, Packet packet) {
            this.owner = owner;
            this.packet = packet;
        }
    
        @Override
        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }
    
        @Override
        public boolean isCancelled() {
            return cancelled;
        }
    
        @Override
        public Player getOwner() {
            return owner;
        }
    
        @Override
        public Packet getPacket() {
            return packet;
        }
    
        @Override
        public void setPacket(Packet packet) {
            this.packet = packet;
        }
    }
    
}
