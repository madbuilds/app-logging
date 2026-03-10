package com.github.mad.logging.api.properties.appender;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ConfigurationProperties("app.logging.appender.loki")
public class LokiAppenderProperties {
    public static final String APPENDER_NAME = "LOKI_APPENDER";
    public static final String DEFAULT_TAG_NAME = "APP";
    public static final String DEFAULT_URI = "loki/api/v1/push";
    public static final String DEFAULT_URL = "http://localhost/";

    /**
     * Enables logging to the loki.
     */
    private final boolean enabled;

    /**
     * Updates application's name (will be used as a tag to query logs by).
     */
    private final String name;

    /**
     * loki endpoint url to push logs to (check if URI needs to be updated as well)
     */
    private final String url;

    /**
     * loki endpoint uri where logs will be pushed to (check URL is correct also)
     */
    private final String uri;

    @ConstructorBinding
    public LokiAppenderProperties(
            @DefaultValue("false") boolean enabled,
            @DefaultValue(DEFAULT_TAG_NAME) String name,
            @DefaultValue(DEFAULT_URL) String url,
            @DefaultValue(DEFAULT_URI) String uri
    ) {
        this.enabled = enabled;
        this.name = name;
        this.url = url;
        this.uri = uri;
    }
}
