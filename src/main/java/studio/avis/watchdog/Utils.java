package studio.avis.watchdog;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class Utils {
    
    public static <T> T getField(Object from, String name) {
        if(from == null)
            throw new IllegalArgumentException("from cannot be null");
        
        if(name == null)
            throw new IllegalArgumentException("name cannot be null");
        
        Class<?> checkClass = from.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                return (T) field.get(from);
            } catch(NoSuchFieldException | IllegalAccessException e) {
            }
        }
        while(checkClass.getSuperclass() != Object.class && ((checkClass = checkClass.getSuperclass()) != null));
        return null;
    }
    
    public static void setField(Object object, String name, Object value) {
        if(object == null)
            throw new IllegalArgumentException("from cannot be null");
        
        if(name == null)
            throw new IllegalArgumentException("name cannot be null");
        
        Class<?> checkClass = object.getClass();
        do {
            try {
                Field field = checkClass.getDeclaredField(name);
                field.setAccessible(true);
                field.set(object, value);
            } catch(NoSuchFieldException | IllegalAccessException e) {
            }
        }
        while(checkClass.getSuperclass() != Object.class && ((checkClass = checkClass.getSuperclass()) != null));
    }
    
    public static void sendPacket(Player player, Packet packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
    
}
