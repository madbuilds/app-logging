module app.logging {
    exports com.github.mad.logging.api;
    exports com.github.mad.logging.api.properties;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires org.slf4j;
    requires lombok;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
}