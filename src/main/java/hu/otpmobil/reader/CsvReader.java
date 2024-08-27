package hu.otpmobil.reader;

import hu.otpmobil.logger.LoggerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CsvReader {
    private static final String SEPARATOR = ";";

    public <T> List<T> readCsv(String fileName, Function<String, T> parser, Function<T, Boolean> validator) throws IOException {
        List<T> validItems = new ArrayList<>();
        Path path = getResourcePath(fileName);

        try (var lines = Files.lines(path)) {
            lines.forEach(line -> {
                try {
                    T item = parser.apply(line);
                    if (validator.apply(item)) {
                        validItems.add(item);
                    } else {
                        LoggerService.warning("Invalid data: " + line);
                    }
                } catch (Exception e) {
                    LoggerService.warning("Error parsing data: " + line + ". Error: " + e.getMessage());
                }
            });
        }

        return validItems;
    }

    public String[] splitLine(String line) {
        return line.split(SEPARATOR);
    }

    private Path getResourcePath(String fileName) {
        return Paths.get("src", "main", "resources", fileName);
    }
}
