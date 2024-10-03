package me.hsgamer.grimyatpa;

import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.events.FlagEvent;
import me.hsgamer.yatpa.event.PostTeleportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class GrimYATPA extends JavaPlugin implements Listener {
    private static final long EXEMPT_TIME = 2000L; // 2 seconds
    private final Map<UUID, Long> lastExemptMap = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler
    public void onTeleport(PostTeleportEvent event) {
        lastExemptMap.put(event.getRequestEntry().requester, System.currentTimeMillis());
    }

    @EventHandler
    public void onFlag(FlagEvent event) {
        GrimUser user = event.getPlayer();
        long lastExempt = lastExemptMap.getOrDefault(user.getUniqueId(), 0L);
        if (System.currentTimeMillis() - lastExempt < EXEMPT_TIME) {
            event.setCancelled(true);
        }
    }
}
