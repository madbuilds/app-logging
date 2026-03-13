package com.github.mad.logging.api.properties;

import com.github.mad.logging.api.properties.appender.ConsoleAppenderProperties;
import com.github.mad.logging.api.properties.appender.FileAppenderProperties;
import com.github.mad.logging.api.properties.appender.LokiAppenderProperties;
import com.github.mad.logging.api.properties.filter.DuplicateMessageFilterProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.logging.LogLevel;

import java.util.Map;

@Getter
@ConfigurationProperties("app.logging")
public class LoggingProperties {
    private final Map<String, LogLevel> level;

    private final FilterProperties filter;
    private final AppenderProperties appender;

    @ConstructorBinding
    public LoggingProperties(
            Map<String, LogLevel> level,
            FilterProperties filter,
            AppenderProperties appender
    ) {
        this.level = level;
        this.filter = filter;
        this.appender = appender;
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

    @Getter
    @ConfigurationProperties("app.logging.appender")
    public static class AppenderProperties {
        private final ConsoleAppenderProperties console;
        private final FileAppenderProperties file;
        private final LokiAppenderProperties loki;

        @ConstructorBinding
        public AppenderProperties(
                ConsoleAppenderProperties console,
                FileAppenderProperties file,
                LokiAppenderProperties loki
        ) {
            this.console = console;
            this.file = file;
            this.loki = loki;
        }
    }
}

