package com.github.mad.logging.api.properties.appender;

import com.github.mad.logging.api.properties.appender.enums.Layout;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ConfigurationProperties("app.logging.appender.file")
public class FileAppenderProperties {
    public static final String APPENDER_NAME = "ASYNC_FILE_APPENDER";
    public static final String DEFAULT_FILE_NAME = "app.log";
    public static final String DEFAULT_LAYOUT = "default";

    /**
     * Enables logging to the file.
     */
    private final boolean enabled;

    /**
     * Updates application's file name for the logging.
     */
    private final String name;

    /**
     * Changes output log layout format.
     */
    private final Layout layout;

    @ConstructorBinding
    public FileAppenderProperties(
            @DefaultValue("false") boolean enabled,
            @DefaultValue(DEFAULT_FILE_NAME) String name,
            @DefaultValue(DEFAULT_LAYOUT) Layout layout
    ) {
        this.enabled = enabled;
        this.name = name;
        this.layout = layout;
    }
}
