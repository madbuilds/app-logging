package com.github.mad.logging.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan({
        "com.github.mad.logging.api.properties"
})
@AutoConfiguration
public class LoggingAutoConfiguration {
}
