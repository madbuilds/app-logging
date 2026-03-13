module app.logging {
    exports com.github.mad.logging.api;
    exports com.github.mad.logging.api.properties;
    exports com.github.mad.logging.api.properties.appender;
    exports com.github.mad.logging.api.properties.filter;
    exports com.github.mad.logging.api.properties.appender.enums;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires org.slf4j;
    requires lombok;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
}