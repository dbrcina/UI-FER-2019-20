package hr.fer.zemris.ui.util;

import hr.fer.zemris.search.structure.StateCostPair;
import hr.fer.zemris.ui.model.HeuristicModel;
import hr.fer.zemris.ui.model.StateSpaceModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class which provides some methods for parsing simple files.
 */
public class FileParser {

    // helper method used for parsing provided file
    // into a list of lines
    // '#' signs are filtered
    private static List<String> parse(Path file) throws IOException {
        try {
            return Files.readAllLines(file).stream()
                    .filter(l -> !l.equals("#"))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new IOException("Error occured while reading '" + file.toAbsolutePath() + "'.");
        }
    }

    /**
     * Parses provided <i>file</i> which contains definition of state space.
     *
     * @param file state space definition.
     * @return an instance of {@link StateSpaceModel} that contains all state space data.
     * @throws IOException if some error occurs while reading from a file.
     */
    public static StateSpaceModel parseStateSpaceFile(Path file) throws IOException {
        List<String> lines = parse(file);
        StateSpaceModel model = new StateSpaceModel();
        // initial state
        model.setInitialState(lines.get(0));
        // goal states
        model.setGoalStates(new LinkedHashSet<>(Arrays.asList(lines.get(1).split("\\s+"))));
        // find all transitions
        Map<String, Set<StateCostPair<String>>> transitions = new LinkedHashMap<>();
        for (int i = 2; i < lines.size(); i++) { // skip 0(initial), 1(goal)
            String[] parts = lines.get(i).split(":");
            String state = parts[0].trim();
            // if state doesn't have any successors
            if (parts.length == 1) {
                transitions.put(state, Set.of());
                continue;
            }
            // (1)split successors by space
            // (2)perform stream on result array
            // (3)map each element of that array to StateCostPair instance
            // (4)collect everything to set
            Set<StateCostPair<String>> successors = Arrays.stream(parts[1].trim().split("\\s+"))
                    .map(successor -> {
                        String[] succParts = successor.split(",");
                        return new StateCostPair<>(succParts[0], Double.parseDouble(succParts[1]));
                    })
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            transitions.put(state, successors);
        }
        model.setTransitions(transitions);
        return model;
    }

    /**
     * Parses provided <i>file</i> which contains heuristic definition.
     *
     * @param file heuristic definition.
     * @return an instance of {@link HeuristicModel} that contains all heuristic data.
     * @throws IOException if some error occurs while reading from a file.
     */
    public static HeuristicModel parseHeuristicFile(Path file) throws IOException {
        List<String> lines = parse(file);
        HeuristicModel model = new HeuristicModel();
        // definition of heuristic file is:
        // state(String): heuristic(double)
        model.setHeuristicValues(lines.stream()
                .map(line -> {
                    String[] parts = line.split(": ");
                    return new SimpleEntry<>(parts[0], Double.parseDouble(parts[1]));
                })
                .collect(Collectors.toUnmodifiableMap(SimpleEntry::getKey, SimpleEntry::getValue)));
        return model;
    }

}
