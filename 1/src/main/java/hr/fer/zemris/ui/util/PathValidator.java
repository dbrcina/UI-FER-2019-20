package hr.fer.zemris.ui.util;

import hr.fer.zemris.ui.statesearch.StateSearchSimulation;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathValidator {

    public static Path validatePath(String path) {
        Path file = null;
        try {
            file = Paths.get(path);
        } catch (InvalidPathException e) {
            StateSearchSimulation.exit("Provided path cannot be converted to a real path.");
        }
        if (!Files.exists(file)) {
            StateSearchSimulation.exit("Provided path '" + file.toAbsolutePath() + "' doesn't exist.");
        } else if (Files.isDirectory(file)) {
            StateSearchSimulation.exit("'" + file.toAbsolutePath() + "' is a directory and program " +
                    "expects a file.");
        } else if (!Files.isReadable(file)) {
            StateSearchSimulation.exit("File with path '" + file.toAbsolutePath() + "' is not " +
                    "readable.");
        }
        return file;
    }

}
