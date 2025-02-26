package one.mad.logging.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

public class CommonProperties {
    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("app.logging")
    public static class Logging {
        /**
         * logger name -> level
         */
        private Map<String, LogLevel> level = new HashMap<>();
    }

    @Getter
    @Setter
    @Configuration
    @ConfigurationProperties("app.logging.level")
    public static class Level {
        /**
         * Log level for the ROOT.
         */
        private LogLevel root;
    }
}

