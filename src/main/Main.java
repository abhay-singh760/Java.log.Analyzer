package dev.loglens;

import java.nio.file.Path;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        if (args.length != 1 || args[0].equals("--help")) {
            System.out.println("LogLens - lightweight application log analyzer");
            System.out.println("Usage: java -jar target/loglens-1.0.0.jar <log-file>");
            return;
        }
        try {
            var report = new LogAnalyzer().analyze(Path.of(args[0]));
            System.out.println("\n=== LOGLENS REPORT ===");
            System.out.printf("Total lines: %d | Parsed: %d | Ignored: %d%n",
                    report.totalLines(), report.parsedLines(), report.ignoredLines());
            System.out.println("\nLog levels:");
            for (var level : LogEntry.Level.values())
                System.out.printf("  %-5s %d%n", level, report.counts().get(level));
            System.out.println("\nErrors:");
            if (report.errors().isEmpty()) System.out.println("  No errors found.");
            else report.errors().forEach(e -> System.out.printf("  [%s] %s%n", e.timestamp(), e.message()));
        } catch (Exception e) {
            System.err.println("Could not analyze file: " + e.getMessage());
            System.exit(1);
        }
    }
}
