package studio.avis.watchdog.killaura;

import studio.avis.watchdog.Utils;
import studio.avis.watchdog.api.WatchDog;
import studio.avis.watchdog.packet.PacketHandler;
import studio.avis.watchdog.api.packet.PacketInfo;
import studio.avis.watchdog.api.packet.PacketListener;
import studio.avis.watchdog.api.repeatable.IRepeatable;

import studio.avis.watchdog.repeatable.RepeatHandler;
import net.minecraft.server.v1_8_R3.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by Avis Network on 2017-06-20.
 */
public class KillauraWatcher extends WatchDog.Impl implements IRepeatable, PacketListener {
    
    private static final int KILL_AURA_INBDICATOR_ID = 9999999;
    
    private Set<String> viewers = new HashSet<>();
    private Map<String, Long> updates = new HashMap<>();
    
    public KillauraWatcher(JavaPlugin plugin) {
        super(plugin, "Killaura");
        
        PacketHandler.self().addPacketListener(this);
        RepeatHandler.self().addRepeatable(this);
    }
    
    Location getCoordinate(Player player) {
        Location loc = player.getLocation();
        
        float z = (float) (loc.getZ() + Math.sin(Math.toRadians(loc.getYaw() + 90)));
        float x = (float) (loc.getX() + Math.cos(Math.toRadians(loc.getYaw() + 90)));
        
        Vector trajectory = new Vector(x - loc.getX(), 0, z - loc.getZ());
        trajectory.setY(0).multiply(-4D);
        trajectory.setY(4D);
        
        return player.getLocation().add(trajectory);
    }
    
    boolean cannotBeChosen(Player player, Player other) {
        return player.equals(other) || !player.canSee(other) || player.hasPotionEffect(PotionEffectType.INVISIBILITY);
    }
    
    void tickPerform() {
        Iterator<String> iterator = suspiciouses().keySet().iterator();
        while(iterator.hasNext()) {
            String player = iterator.next();
            
            KillauraSuspicious suspicious = (KillauraSuspicious) suspiciouses().get(player);
            if(!elapsed(suspicious.timer, 5000L))
                continue;
            
            if(suspicious.stacks > 10) {
                report(player,
                        "[WATCH DOG] " + player + " attacked fake player " + suspicious.stacks + " times in 5 seconds.",
                        viewer -> viewer.hasPermission("avis.reports.killaura")
                );
                plus(player, 4);
            }
            
            if(suspicious.hits >= 50) {
                overReported(player);
            }
            iterator.remove();
        }
    }
    
    void tickReports() {
        reports().keySet().removeIf(s -> elapsed(reports().get(s).lastReport, 12000L));
    }
    
    void tickFakePlayer() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(viewers.contains(player.getName())) {
                if(!player.isOnline() || player.isDead()) {
                    viewers.remove(player.getName());
                    updates.remove(player.getName());
                    continue;
                }
                
                if(elapsed(updates.get(player.getName()), 5000L))
                {
                    updates.put(player.getName(), System.currentTimeMillis());
                    viewers.remove(player.getName());
                    
                    Utils.sendPacket(player, new PacketPlayOutEntityDestroy(KILL_AURA_INBDICATOR_ID));
                }
                
                Location coordinate = getCoordinate(player);
    
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(
                        KILL_AURA_INBDICATOR_ID,
                        MathHelper.floor(coordinate.getX() * 32d),
                        MathHelper.floor(coordinate.getY() * 32d),
                        MathHelper.floor(coordinate.getZ() * 32d),
                        (byte) MathHelper.d(coordinate.getY() * 256f / 360f),
                        (byte) MathHelper.d(coordinate.getPitch() * 256f / 360f),
                        false
                );
                Utils.sendPacket(player, packet);
            } else {
                if(Bukkit.getOnlinePlayers().size() <= 1 || !player.isOnline() || player.isDead())
                    continue;
                
                Location coordinate = getCoordinate(player);
                
                Player chosen = Bukkit.getOnlinePlayers().toArray(new Player[0])[random().nextInt(Bukkit.getOnlinePlayers().size())];
                if(cannotBeChosen(chosen, player))
                    continue;
                
                viewers.add(player.getName());
                updates.put(player.getName(), System.currentTimeMillis());
    
                PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) chosen).getHandle());
                Utils.setField(packet, "a", KILL_AURA_INBDICATOR_ID);
                Utils.setField(packet, "c", MathHelper.floor(coordinate.getX() * 32d));
                Utils.setField(packet, "d", MathHelper.floor(coordinate.getY() * 32d));
                Utils.setField(packet, "e", MathHelper.floor(coordinate.getZ() * 32d));
                
                Utils.sendPacket(player, packet);
            }
        }
    }
    
    @Override
    public void in(PacketInfo info) {
        if(info.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = (PacketPlayInUseEntity) info.getPacket();
            if(packet.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                int entityId = Utils.getField(packet, "a");
                
                // the fake entity has been attacked.
                if(entityId == KILL_AURA_INBDICATOR_ID) {
                    KillauraSuspicious suspicious = suspected(info.getOwner(), KillauraSuspicious.class);
                    suspicious.hits++;
                    if(suspicious.lastHit == -1L) {
                        suspicious.lastHit = System.currentTimeMillis();
                    } else {
                        long delay = System.currentTimeMillis() - suspicious.lastHit;
                        
                        suspicious.lastHit = System.currentTimeMillis();
                        suspicious.hitDelay = delay;
                        
                        if(suspicious.hitDelay < 100L) {
                            suspicious.stacks++;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void out(PacketInfo info) {
        // nothing do.
    }
    
    @Override
    public void onQuit(Player player) {
        invalidate(player.getName());
        suspiciouses().remove(player.getName());
        
        updates.remove(player.getName());
        viewers.remove(player.getName());
        
        Utils.sendPacket(player, new PacketPlayOutEntityDestroy(KILL_AURA_INBDICATOR_ID));
    }
    
    @Override
    public void onTick() {
        tickPerform();
        tickFakePlayer();
        tickReports();
    }
    
    @Override
    public void overReported(String playerName) {
        invalidate(playerName);
        // AvisPunish supports
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pb " + playerName + " [WATCH DOG] Killaura");
    }
    
    @Override
    public String format(String playerName, String reason) {
        return playerName + ChatColor.WHITE + " has been reported for " + ChatColor.AQUA + reason;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldChanged(PlayerChangedWorldEvent event) {
        viewers.remove(event.getPlayer().getName());
    
        Utils.sendPacket(event.getPlayer(), new PacketPlayOutEntityDestroy(KILL_AURA_INBDICATOR_ID));
    }
}