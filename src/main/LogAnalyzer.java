package dev.loglens;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class LogAnalyzer {
    public record Report(int totalLines, int parsedLines, int ignoredLines,
                         Map<LogEntry.Level, Long> counts, List<LogEntry> errors) {}

    public Report analyze(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file);
        Map<LogEntry.Level, Long> counts = new EnumMap<>(LogEntry.Level.class);
        for (LogEntry.Level level : LogEntry.Level.values()) counts.put(level, 0L);
        int parsed = 0;
        List<LogEntry> errors = new ArrayList<>();
        for (String line : lines) {
            var result = LogEntry.parse(line);
            if (result.isEmpty()) continue;
            LogEntry entry = result.get();
            parsed++;
            counts.merge(entry.level(), 1L, Long::sum);
            if (entry.level() == LogEntry.Level.ERROR) errors.add(entry);
        }
        return new Report(lines.size(), parsed, lines.size() - parsed,
                Map.copyOf(counts), List.copyOf(errors));
    }
}
