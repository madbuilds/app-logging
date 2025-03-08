package one.mad.logging.properties;

import lombok.Getter;
import lombok.Setter;
import one.mad.logging.properties.appender.FileAppenderProperties;
import one.mad.logging.properties.appender.LokiAppenderProperties;
import one.mad.logging.properties.filter.DuplicateMessageFilterProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.logging")
public class LoggingProperties {
    private Map<String, LogLevel> level;

    private FilterProperties filter;
    private FileAppenderProperties file;
    private LokiAppenderProperties loki;

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("app.logging.level")
    public static class LevelProperties {
        /**
         * Log level for the ROOT.
         */
        private LogLevel root;
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("app.logging.filter")
    public static class FilterProperties {
        DuplicateMessageFilterProperties duplicateMessageFilter;
    }
}

