package dev.loglens;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record LogEntry(LocalDateTime timestamp, Level level, String message) {
    private static final Pattern FORMAT = Pattern.compile(
        "^(\\d{4}-\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+(INFO|WARN|WARNING|ERROR|DEBUG)\\s+-\\s+(.*)$",
        Pattern.CASE_INSENSITIVE
    );
    public enum Level { DEBUG, INFO, WARN, ERROR }

    public static Optional<LogEntry> parse(String line) {
        Matcher matcher = FORMAT.matcher(line.trim());
        if (!matcher.matches()) return Optional.empty();
        try {
            LocalDateTime time = LocalDateTime.parse(matcher.group(1).replace(' ', 'T'));
            String raw = matcher.group(2).toUpperCase(Locale.ROOT);
            Level level = raw.equals("WARNING") ? Level.WARN : Level.valueOf(raw);
            return Optional.of(new LogEntry(time, level, matcher.group(3)));
        } catch (RuntimeException exception) {
            return Optional.empty();
        }
    }
}
