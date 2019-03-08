package de.pauhull.uuidfetcher.common.fetcher;

import cloud.timo.TimoCloud.api.TimoCloudAPI;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UUIDCache {

    private Map<UUID, String> nameCache;
    private Map<String, UUID> uuidCache;

    public UUIDCache(TimeUnit unit, long cacheTime) {
        this.nameCache = new TimedHashMap<>(unit, cacheTime);
        this.uuidCache = new TimedHashMap<>(unit, cacheTime);
    }

    public UUIDCache() {
        this(TimeUnit.HOURS, 12);
    }

    public void save(UUID uuid, String name) {
        nameCache.put(uuid, name);
        uuidCache.put(name, uuid);
    }

    public UUID getUUID(String name) {
        if (TimoCloudAPI.getUniversalAPI().getPlayer(name) != null) {
            return TimoCloudAPI.getUniversalAPI().getPlayer(name).getUuid();
        }else {
            if (uuidCache.containsKey(name)) {
                return uuidCache.get(name);
            }else {
                return null;
            }
        }
    }

    public String getName(UUID uuid) {
        if (TimoCloudAPI.getUniversalAPI().getPlayer(uuid) != null) {
            return TimoCloudAPI.getUniversalAPI().getPlayer(uuid).getName();
        }else {
            if (nameCache.containsKey(uuid)) {
                return nameCache.get(uuid);
            }else {
                return null;
            }

        }
    }

}
