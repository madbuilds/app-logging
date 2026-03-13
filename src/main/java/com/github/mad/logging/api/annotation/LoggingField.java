package com.github.mad.logging.api.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggingField {
    String key() default "field";
    String value() default "";

    Entry[] entries() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @interface Entry {
        String key() default "field";
        String value();
    }
}
