package hr.fer.zemris.ui.util;

import hr.fer.zemris.search.structure.StateCostPair;
import hr.fer.zemris.ui.uninformed.UninformedSearchDataModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileParser {

    public static UninformedSearchDataModel parseStateSearchFile(Path file) throws IOException {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file).stream()
                    .filter(l -> !l.equals("#"))
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            throw new IOException("Error occured while reading from file.");
        }

        UninformedSearchDataModel model = new UninformedSearchDataModel();
        // initial state
        model.setInitialState(lines.get(0));
        // goal states
        model.setGoalStates(new LinkedHashSet<>(Arrays.asList(lines.get(1).split("\\s+"))));

        // find all transitions
        Map<String, Set<StateCostPair<String>>> transitions = new HashMap<>();
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

}
