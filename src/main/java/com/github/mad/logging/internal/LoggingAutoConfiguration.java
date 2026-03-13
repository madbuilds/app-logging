package com.github.mad.logging.internal;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationPropertiesScan({
        "com.github.mad.logging.api.properties"
})
@ComponentScan({
        "com.github.mad.logging.internal.aspect"
})
@AutoConfiguration
public class LoggingAutoConfiguration {
}
