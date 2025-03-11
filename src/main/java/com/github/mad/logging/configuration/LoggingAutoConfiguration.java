package com.github.mad.logging.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan({
        "com.github.mad.logging.properties"
})
@AutoConfiguration
public class LoggingAutoConfiguration {
}
