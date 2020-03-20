package hr.fer.zemris.ui.util;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathValidator {

    public static Path validatePath(String path) {
        Path file;
        try {
            file = Paths.get(path);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Provided path cannot be converted to a real path.");
        }
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Provided path '" + file.toAbsolutePath() + "' doesn't exist.");
        } else if (Files.isDirectory(file)) {
            throw new IllegalArgumentException("'" + file.toAbsolutePath() + "' is a directory and program " +
                    "expects a file.");
        } else if (!Files.isReadable(file)) {
            throw new IllegalArgumentException("File with path '" + file.toAbsolutePath() + "' is not " +
                    "readable.");
        }
        return file;
    }

}
