package com.github.mad.logging.internal.fields;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

import java.util.Map;

public class LoggingFieldsConverter extends CompositeConverter<ILoggingEvent> {
    private static final String EMPTY = "";

    @Override
    protected String transform(ILoggingEvent iLoggingEvent, String s) {
        Map<?, ?> fields = iLoggingEvent.getMDCPropertyMap();
        return (fields == null || fields.isEmpty()) ?
                EMPTY : fields.toString();
    }
}
