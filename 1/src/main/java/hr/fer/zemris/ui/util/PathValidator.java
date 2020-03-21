package hr.fer.zemris.ui.util;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class which provides some methods used to validate given path.
 */
public class PathValidator {

    /**
     * Validates provided <i>file</i>. Checks whether file exists, is directory, is readable etc.
     *
     * @param file file to be validate.
     * @return {@link Path} representation of file if the file is valid.
     * @throws IllegalArgumentException if provided <i>file</i> is invalid.
     */
    public static Path validateFile(String file) {
        Path path;
        try {
            path = Paths.get(file);
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("Provided path cannot be converted to a real path.");
        }
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("Provided path '" + path.toAbsolutePath() + "' doesn't " +
                    "exist.");
        } else if (Files.isDirectory(path)) {
            throw new IllegalArgumentException("'" + path.toAbsolutePath() + "' is a directory and " +
                    "program expects a file.");
        } else if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("File with path '" + path.toAbsolutePath() + "' is not " +
                    "readable.");
        }
        return path;
    }

}
