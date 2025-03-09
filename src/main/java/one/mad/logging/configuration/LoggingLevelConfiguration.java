package one.mad.logging.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

@Slf4j
public class LoggingLevelConfiguration implements GenericApplicationListener {
    private static final ConfigurationPropertyName LOGGING_LEVEL = ConfigurationPropertyName.of("app.logging.level");
    private static final Bindable<Map<String, LogLevel>> STRING_LOGLEVEL_MAP = Bindable.mapOf(String.class, LogLevel.class);

    private static final Class<?>[] EVENT_TYPES = new Class[] {
            ApplicationStartingEvent.class,
            ApplicationEnvironmentPreparedEvent.class,
            ApplicationPreparedEvent.class,
            ContextClosedEvent.class,
            ApplicationFailedEvent.class
    };

    private LoggingSystem loggingSystem;

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        Class<?> eventClass = eventType.getRawClass();
        if (eventClass == null) {
            return false;
        }

        return Arrays.stream(EVENT_TYPES).anyMatch( supportedType ->
                supportedType.isAssignableFrom(eventClass)
        );
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent environmentPreparedEvent) {
            onApplicationEnvironmentPreparedEvent(environmentPreparedEvent);
        }
    }

    private void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent environmentPreparedEvent) {
        SpringApplication application = environmentPreparedEvent.getSpringApplication();
        if (this.loggingSystem == null) {
            this.loggingSystem = LoggingSystem.get(application.getClassLoader());
        }

        init(environmentPreparedEvent.getEnvironment(), application.getClassLoader());
    }

    private void init(Environment environment, ClassLoader classLoader) {
        initializeLoggingLevels(environment, loggingSystem);
    }

    private void initializeLoggingLevels(Environment environment, LoggingSystem loggingSystem) {
        setLogLevels(environment, loggingSystem);
    }

    private void setLogLevels(Environment environment, LoggingSystem loggingSystem) {
        BiConsumer<String, LogLevel> logLevelBiConsumer = getLogLevelConfigurer(loggingSystem);
        Binder binder = Binder.get(environment);
        Map<String, LogLevel> levels = binder.bind(LOGGING_LEVEL, STRING_LOGLEVEL_MAP).orElseGet(Collections::emptyMap);
        levels.forEach((name, level) -> this.configureLevel(name, level, logLevelBiConsumer));
    }

    private BiConsumer<String, LogLevel> getLogLevelConfigurer(LoggingSystem system) {
        return (name, level) -> {
            try {
                name = name.equalsIgnoreCase("ROOT") ? null : name;
                system.setLogLevel(name, level);
            } catch (RuntimeException exception) {
                log.error("Cannot set level {} for {}", level, name, exception);
            }
        };
    }

    private void configureLevel(String name, LogLevel level, BiConsumer<String, LogLevel> configurer) {
        configurer.accept(name, level);
    }
}
