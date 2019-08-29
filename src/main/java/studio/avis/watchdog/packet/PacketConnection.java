package studio.avis.watchdog.packet;

import net.minecraft.server.v1_8_R3.*;
import studio.avis.watchdog.api.packet.PacketInfo;
import studio.avis.watchdog.api.packet.PacketListener;

import java.util.List;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class PacketConnection extends PlayerConnection {
    
    private final List<PacketListener> listeners;
    
    public PacketConnection(List<PacketListener> listener, MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
        
        this.listeners = listener;
    }
    
    @Override
    public void sendPacket(Packet packet) {
        PacketInfo info = new PacketInfo.Impl(this.player.getBukkitEntity(), packet);
        
        for(PacketListener listener : listeners) {
            listener.out(info);
        }
        
        if(info.isCancelled()) {
            return;
        }
        
        super.sendPacket(packet);
    }
    
    public <T extends Packet> T handleIn(T packet) {
        PacketInfo info = new PacketInfo.Impl(this.player.getBukkitEntity(), packet);
        
        for(PacketListener listener : listeners) {
            listener.in(info);
        }
        
        if(info.isCancelled()) {
            return null;
        }
        return (T) info.getPacket();
    }
    
    @Override
    public void a(PacketPlayInAbilities packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInFlying packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInKeepAlive packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInUseEntity packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInBlockPlace packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInUpdateSign packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInSetCreativeSlot packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInHeldItemSlot packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInSteerVehicle packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInSettings packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInClientCommand packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInTabComplete packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInChat packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInArmAnimation packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInCustomPayload packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInCloseWindow packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInWindowClick packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInEnchantItem packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
    
    @Override
    public void a(PacketPlayInTransaction packet) {
        if((packet = handleIn(packet)) != null) {
            super.a(packet);
        }
    }
}
