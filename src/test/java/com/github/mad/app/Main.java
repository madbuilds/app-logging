package com.github.mad.app;

import com.github.mad.logging.api.annotation.LoggingField;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.github.mad.logging.api.Marker.FILTERED;

@Slf4j
@SpringBootApplication
public class Main {
    @Autowired
    OtherBean otherBean;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        otherBean.test("test");

        for (int i = 1; i < 100; i++) {
            log.info(FILTERED, "MESSAGE TO BE TESTED: {}", i);
        }

        log.trace("TEST TRACE 0");
        log.debug("TEST DEBUG 1");
        log.info( "TEST INFO  2");
        log.warn( "TEST WARN  3");
        log.error("TEST ERROR 4");

        Marker EXAMPLE = MarkerFactory.getMarker("EXAMPLE");
        for (int i = 1; i < 100; i++) {
            log.info(EXAMPLE, "MESSAGE TO BE VERIFIED: {}", i);
        }

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (int i = 1; i < 100; i++) {
            log.info(FILTERED, "MESSAGE TO BE TESTED: {}", i);
        }
    }

    @Slf4j
    @Component
    @LoggingField(entries = {
            @LoggingField.Entry(key = "example", value = "#text"),
            @LoggingField.Entry(key = "field", value = "value")
    })
    public static class OtherBean {

        public void test(
                String text
        ) {
            log.info("TEST {}", text);
            example();
        }

        public void example() {
            log.info("EXAMPLE");
        }
    }
}
