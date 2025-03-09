package one.mad.logging.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan({
        "one.mad.logging.properties"
})
@AutoConfiguration
public class LoggingAutoConfiguration {
}
