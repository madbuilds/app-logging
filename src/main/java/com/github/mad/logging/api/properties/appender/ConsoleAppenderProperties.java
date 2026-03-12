package com.github.mad.logging.api.properties.appender;

import com.github.mad.logging.api.properties.appender.enums.Layout;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ConfigurationProperties("app.logging.appender.console")
public class ConsoleAppenderProperties {
    public static final String APPENDER_NAME = "ASYNC_CONSOLE_APPENDER";
    public static final String DEFAULT_LAYOUT = "default";

    /**
     * Enables logging to the file.
     */
    private final boolean enabled;

    /**
     * Changes output log layout format.
     */
    private final Layout layout;

    public ConsoleAppenderProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue(DEFAULT_LAYOUT) Layout layout
    ) {
        this.enabled = enabled;
        this.layout = layout;
    }
}
