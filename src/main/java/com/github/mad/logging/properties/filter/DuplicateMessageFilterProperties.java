package com.github.mad.logging.properties.filter;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Getter
@ConfigurationProperties("app.logging.filter.duplicate-message-filter")
public class DuplicateMessageFilterProperties {
    public static final String DEFAULT_MARKERS = "FILTERED";
    public static final int DEFAULT_ALLOWED_REPETITIONS = 5;
    public static final int DEFAULT_EXPIRE_AFTER_WRITE_SECONDS = 60;
    public static final int DEFAULT_CACHE_SIZE = 100;
    public static final int DEFAULT_MAX_KEY_LENGTH = 100;

    /**
     * Enables DuplicateMessageFilter.
     * @see com.github.mad.logging.filter.DuplicateMessageFilter
     */
    private final boolean enabled;

    /**
     * Marker that will be used to Filter same log message on repeats.
     */
    private final String markers;

    /**
     * Number of repeats allowed for log to be filtered.
     */
    private final int repeats;

    /**
     * Seconds after which log will be counted back for repeats.
     */
    private final int expire;

    /**
     * How much unique messages can be stored in cache.
     */
    private final int cacheSize;

    @ConstructorBinding
    public DuplicateMessageFilterProperties(
            @DefaultValue("false") boolean enabled,
            @DefaultValue(DEFAULT_MARKERS) String markers,
            @DefaultValue("5") int repeats,
            @DefaultValue("60") int expire,
            @DefaultValue("100") int cacheSize
    ) {
        this.enabled = enabled;
        this.markers = markers;
        this.repeats = repeats;
        this.expire = expire;
        this.cacheSize = cacheSize;
    }

    public List<String> getMarkersAsList() {
        return List.of(markers.split("\\s"));
    }
}
