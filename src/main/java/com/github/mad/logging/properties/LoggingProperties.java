package com.github.mad.logging.properties;

import lombok.Getter;
import com.github.mad.logging.properties.appender.FileAppenderProperties;
import com.github.mad.logging.properties.appender.LokiAppenderProperties;
import com.github.mad.logging.properties.filter.DuplicateMessageFilterProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.logging.LogLevel;

import java.util.Map;

@Getter
@ConfigurationProperties("app.logging")
public class LoggingProperties {
    private final Map<String, LogLevel> level;

    private final FilterProperties filter;
    private final FileAppenderProperties file;
    private final LokiAppenderProperties loki;

    @ConstructorBinding
    public LoggingProperties(
            Map<String, LogLevel> level,
            FilterProperties filter,
            FileAppenderProperties file,
            LokiAppenderProperties loki
    ) {
        this.level = level;
        this.filter = filter;
        this.file = file;
        this.loki = loki;
    }

    @Getter
    @ConfigurationProperties("app.logging.level")
    public static class LevelProperties {
        /**
         * Log level for the ROOT.
         */
        private final  LogLevel root;

        @ConstructorBinding
        public LevelProperties(
                LogLevel root
        ) {
            this.root = root;
        }
    }

    @Getter
    @ConfigurationProperties("app.logging.filter")
    public static class FilterProperties {
        private final DuplicateMessageFilterProperties duplicateMessageFilter;

        @ConstructorBinding
        public FilterProperties(
                DuplicateMessageFilterProperties duplicateMessageFilter
        ) {
            this.duplicateMessageFilter = duplicateMessageFilter;
        }
    }
}

