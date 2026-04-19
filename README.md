# app-logging

A Spring Boot auto-configuration library that provides structured, flexible logging via **Logback**. 
Supports console, file, and Loki appenders — each configurable through `application.yml`
With a built-in filters:
- duplicate message filter.

---

## Features

- ✅ **Console appender** — plain text or JSON (Logstash) layout, async
- ✅ **File appender** — plain text or JSON layout, rolling by day, async
- ✅ **Loki appender** — push logs to Grafana Loki via `loki4j`
- ✅ **Duplicate message filter** — suppresses repeated log messages by marker
- ✅ **Root log level** — configurable per-package log levels
- ✅ **Spring Boot auto-configuration** — zero boilerplate, just add dependency

---

## Requirements

| Tool | Version |
|------|---------|
| Java | 25 |
| Spring Boot | 4.x |
| Logback | bundled via Spring Boot |

---

## Installation
Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.github.mad.lib</groupId>
    <artifactId>logging</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

Configure the GitHub Packages repository:

```xml
<repositories>
    <repository>
        <id>github-repo</id>
        <url>https://maven.pkg.github.com/madbuilds/repo</url>
    </repository>
</repositories>
```

## Configuration
All settings live under the app.logging prefix in application.yml.
```yaml
app:
  logging:
    level:
      root: info                # Root log level
      com.example: debug        # Per-package log level (e.g. com.example: debug)

    filter:
      duplicate-message-filter: # Suppresses repeated log messages that are marked with a configured SLF4J marker.
        enabled: true           # Enable the filter
        markers: FILTERED       # Whitespace-separated list of markers to watch
        repeats: 5              # Allowed repetitions before suppression
        expire: 60              # Seconds after which the repeat counter resets
        cache-size: 100         # Max unique messages tracked simultaneously

    appender:
      console:
        enabled: true           # Enable console output
        layout: default         # default | json - Output format
      file:
        enabled: false          # Enable file output
        name: app.log           # Output file name
        layout: default         # default | json - Output format
      loki:
        enabled: false          # Enable Loki push
        name: MY_APP            # App label used in Loki tags
        url: http://localhost:3100/
        uri: loki/api/v1/push
```

The library exposes a Marker class with a predefined marker:
You can define additional custom markers using SLF4J's MarkerFactory and add them to the markers list in configuration.
```java
import com.github.mad.logging.api.Marker;

log.warn(Marker.FILTERED, "This message will be filtered after N repeats");
```