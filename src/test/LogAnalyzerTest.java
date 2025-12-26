package dev.loglens;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class LogAnalyzerTest {
    @TempDir Path tempDir;
    @Test void createsAccurateSummary() throws Exception {
        Path log = tempDir.resolve("app.log");
        Files.writeString(log, String.join("\n",
            "2026-11-14 09:00:00 INFO - Server started",
            "2026-11-14 09:01:00 WARN - High memory usage",
            "2026-11-14 09:02:00 ERROR - Database unavailable",
            "garbage line"));
        var report = new LogAnalyzer().analyze(log);
        assertEquals(4, report.totalLines());
        assertEquals(3, report.parsedLines());
        assertEquals(1, report.ignoredLines());
        assertEquals(1L, report.counts().get(LogEntry.Level.ERROR));
        assertEquals(1, report.errors().size());
    }
}
