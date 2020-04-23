package ui.util;

import ui.model.CNFClause;
import ui.model.Literal;
import ui.model.PLModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class which provides simple parsing method.
 */
public class FileParser {

    public static PLModel parseClausesFile(Path file) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(file)) {
            List<CNFClause> clauses = new LinkedList<>();
            String line;
            int index = 1;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty() || line.startsWith("#")) continue;
                Collection<Literal> literals = Arrays.stream(line.split("\\sv\\s"))
                        .map(Literal::new)
                        .collect(Collectors.toList());
                clauses.add(new CNFClause(literals, index++));
            }
            CNFClause goalClause = clauses.remove(clauses.size() - 1);
            return new PLModel(clauses, goalClause);
        }
    }

}
