package dev.loglens;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogEntryTest {
    @Test void parsesErrorLine() {
        var entry = LogEntry.parse("2026-11-14 09:30:00 ERROR - Database unavailable").orElseThrow();
        assertEquals(LogEntry.Level.ERROR, entry.level());
        assertEquals("Database unavailable", entry.message());
    }
    @Test void normalizesWarning() {
        assertEquals(LogEntry.Level.WARN,
            LogEntry.parse("2026-11-14 09:30:00 WARNING - Slow request").orElseThrow().level());
    }
    @Test void rejectsMalformedLine() {
        assertTrue(LogEntry.parse("not a valid log").isEmpty());
    }
}
