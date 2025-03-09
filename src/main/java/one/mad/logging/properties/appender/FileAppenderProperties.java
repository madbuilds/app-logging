package one.mad.logging.properties.appender;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ConfigurationProperties("app.logging.file")
public class FileAppenderProperties {
    /**
     * Enables logging to the file.
     */
    private final boolean enabled;

    /**
     * Updates application's file name for the logging.
     */
    private final String name;

    @ConstructorBinding
    public FileAppenderProperties(
            @DefaultValue("false") boolean enabled,
            @DefaultValue("app.log") String name
    ) {
        this.enabled = enabled;
        this.name = name;
    }
}
