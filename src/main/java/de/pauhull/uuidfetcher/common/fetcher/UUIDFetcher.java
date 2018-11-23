package de.pauhull.uuidfetcher.common.fetcher;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Created by Paul
 * on 23.11.2018
 *
 * @author pauhull
 */
public interface UUIDFetcher {

    static UUID parseUUIDFromString(String uuidAsString) {
        String[] parts = {
                "0x" + uuidAsString.substring(0, 8),
                "0x" + uuidAsString.substring(8, 12),
                "0x" + uuidAsString.substring(12, 16),
                "0x" + uuidAsString.substring(16, 20),
                "0x" + uuidAsString.substring(20, 32)
        };

        long mostSigBits = Long.decode(parts[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(parts[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(parts[2]).longValue();

        long leastSigBits = Long.decode(parts[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(parts[4]).longValue();

        return new UUID(mostSigBits, leastSigBits);
    }

    void fetchUUIDAsync(String playerName, Consumer<UUID> consumer);

    void fetchNameAsync(UUID uuid, Consumer<String> consumer);

    void getNameCaseSensitive(String name, Consumer<String> consumer);

}
