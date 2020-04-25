package ui.util;

import ui.command.AddCommand;
import ui.command.Command;
import ui.command.QueryCommand;
import ui.command.RemoveCommand;
import ui.model.CNFClause;
import ui.model.Literal;
import ui.model.PLModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Utility class which provides simple parsing methods.
 */
public class FileParser {

    private static final String SPLIT_PATTER = "\\s+v\\s+";

    public static PLModel parseClausesFile(Path file, boolean testing) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(file)) {
            List<CNFClause> clauses = new LinkedList<>();
            String line;
            int index = 1;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty() || line.startsWith("#")) continue;
                Collection<Literal> literals = Arrays.stream(line.split(SPLIT_PATTER))
                        .map(Literal::new)
                        .collect(Collectors.toList());
                clauses.add(new CNFClause(literals, index++));
            }
            return new PLModel(clauses,
                    testing ? clauses.get(clauses.size() - 1) : clauses.remove(clauses.size() - 1));
        }
    }

    public static Collection<Entry<CNFClause, Command>> parseCommandsFile(
            Path file, PLModel model, boolean verbose) throws IOException {
        int index = model.getGoalClause().getIndex() + 1;
        try (BufferedReader br = Files.newBufferedReader(file)) {
            Collection<Entry<CNFClause, Command>> cnfCommands = new LinkedList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int lastIndexOfSpace = line.lastIndexOf(' ');
                Command command = null;
                switch (line.substring(lastIndexOfSpace + 1)) {
                    case "+":
                        command = new AddCommand(model, true);
                        break;
                    case "-":
                        command = new RemoveCommand(model, true);
                        break;
                    case "?":
                        command = new QueryCommand(model, true, verbose);
                }
                Collection<Literal> literals = Arrays.stream(
                        line.substring(0, lastIndexOfSpace).split(SPLIT_PATTER))
                        .map(Literal::new)
                        .collect(Collectors.toList());
                cnfCommands.add(new AbstractMap.SimpleEntry<>(
                        new CNFClause(literals, index++), command));
            }
            return cnfCommands;
        }
    }

}
