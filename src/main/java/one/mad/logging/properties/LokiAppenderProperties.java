package one.mad.logging.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.logging.loki")
public class LokiAppenderProperties {
    /**
     * Enables logging to the loki.
     */
    private boolean enabled = false;

    /**
     * Updates application's name (will be used as a tag to query logs by).
     */
    private String name = "APP";

    /**
     * loki endpoint url to push logs to (check if URI needs to be updated as well)
     */
    private String url = "http://localhost/";

    /**
     * loki endpoint uri where logs will be pushed to (check URL is correct also)
     */
    private String uri = "loki/api/v1/push";
}
