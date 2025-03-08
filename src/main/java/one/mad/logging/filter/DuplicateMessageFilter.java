package one.mad.logging.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static one.mad.logging.properties.filter.DuplicateMessageFilterProperties.*;

/**
 * Custom DuplicateMessageFilter that can be used in spring logback.xml file to enable log message filtering.
 * Filter will skip messages that repeats more than allowedRepetitions in expireAfterWriteSeconds time.
 *
 * @see ch.qos.logback.classic.turbo.DuplicateMessageFilter
 */
@Getter
public class DuplicateMessageFilter extends TurboFilter {
    @Setter private int expireAfterWriteSeconds = DEFAULT_EXPIRE_AFTER_WRITE_SECONDS;
    @Setter private int allowedRepetitions = DEFAULT_ALLOWED_REPETITIONS;
    @Setter private String includeMarkers = DEFAULT_MARKERS;
    @Setter private int cacheSize = DEFAULT_CACHE_SIZE;

    @Getter(AccessLevel.NONE) private Set<Marker> includeMarkersList = new HashSet<>();
    @Getter(AccessLevel.NONE) private SimpleCache<String, Integer> cache;

    @Override
    public void start() {
        includeMarkersList = parseMarkers(includeMarkers);
        this.cache = new SimpleCache<>(
                cacheSize,
                expireAfterWriteSeconds
        );
        super.start();
    }

    @Override
    public void stop() {
        cache.invalidateAll();
        cache = null;

        super.stop();
    }

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] objects, Throwable throwable) {
        if (marker != null && includeMarkersList.contains(marker)) {
            return decideBasedOnRepeatsCount(format);
        }

        return FilterReply.NEUTRAL;
    }

    private FilterReply decideBasedOnRepeatsCount(String format) {
        int count = 0;
        if (format != null && !format.isEmpty()) {
            final String key = format.substring(0, Math.min(DEFAULT_MAX_KEY_LENGTH, format.length()));
            final Integer cachedCount = cache.get(key);

            if (cachedCount != null) {
                count = cachedCount + 1;
            }
            cache.put(key, count);
        }

        return (count < allowedRepetitions) ? FilterReply.NEUTRAL : FilterReply.DENY;
    }

    private Set<Marker> parseMarkers(String markers) {
        final List<String> parsedMarkers = Arrays.asList(markers.split("[,\\s]"));
        return parsedMarkers.stream().filter(strring -> !strring.equalsIgnoreCase(""))
                .map(String::trim)
                .map(MarkerFactory::getMarker)
                .collect(Collectors.toSet());
    }

    private static class SimpleCache<K, V> {
        private final ConcurrentHashMap<K, V> cache;
        private final ConcurrentHashMap<K, Long> expiryMap;
        private final int cacheSize;
        private final int expireAfterWriteSeconds;

        public SimpleCache(int cacheSize, int expireAfterWriteSeconds) {
            this.cache = new ConcurrentHashMap<>(cacheSize);
            this.expiryMap = new ConcurrentHashMap<>(cacheSize);
            this.cacheSize = cacheSize;
            this.expireAfterWriteSeconds = expireAfterWriteSeconds;
        }

        public void put(K key, V value) {
            if (cache.size() >= cacheSize) {
                removeOldestEntry();
            }
            cache.put(key, value);
            expiryMap.put(key, System.currentTimeMillis() + expireAfterWriteSeconds * 1000L);
        }

        public V get(K key) {
            Long expiryTime = expiryMap.get(key);
            if (expiryTime != null && System.currentTimeMillis() > expiryTime) {
                cache.remove(key);
                expiryMap.remove(key);
                return null;
            }
            return cache.get(key);
        }

        public void invalidateAll() {
            for (K key : expiryMap.keySet()) {
                cache.remove(key);
                expiryMap.remove(key);
            }
        }

        private void removeOldestEntry() {
            K oldestKey = null;
            long oldestExpiry = Long.MAX_VALUE;

            for (K key : expiryMap.keySet()) {
                long expiryTime = expiryMap.get(key);
                if (expiryTime < oldestExpiry) {
                    oldestExpiry = expiryTime;
                    oldestKey = key;
                }
            }

            if (oldestKey != null) {
                cache.remove(oldestKey);
                expiryMap.remove(oldestKey);
            }
        }
    }
}