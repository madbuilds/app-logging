package one.mad.logging.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
        "one.mad.logging.properties"
})
@AutoConfiguration
public class LoggingAutoConfiguration {
}


