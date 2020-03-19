package hr.fer.zemris.ui.statesearch;

import hr.fer.zemris.search.BFS;
import hr.fer.zemris.search.BasicNode;
import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.StateCostPair;
import hr.fer.zemris.ui.util.FileParser;
import hr.fer.zemris.ui.util.PathValidator;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StateSearchSimulation {

    private enum SearchAlgorithms {BFS, UCS, ASTAR}

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Program expects two arguments; path to definition file and search alg.");
            return;
        }
        Path file = PathValidator.validatePath(args[0]);
        SearchAlgorithms algorithm = checkAlgorithm(args[1].toUpperCase());
        StateSearchDataModel model = FileParser.parseStateSearchFile(file);
        Predicate<String> goal = s -> model.getGoalStates().contains(s);
        switch (algorithm) {
            case BFS:
                Function<String, Set<String>> succ = s -> model.getTransition(s).stream()
                        .map(StateCostPair::getState)
                        .collect(Collectors.toUnmodifiableSet());
                bfsAlg(model.getInitialState(), succ, goal);
                break;
            case UCS:
        }
    }

    private static void bfsAlg(String s0, Function<String, Set<String>> succ, Predicate<String> goal) {
        SearchAlgorithm<String, String> alg = new BFS<>();
        alg.search(s0, succ, goal).ifPresentOrElse(result -> {
            System.out.println("States visited = " + alg.getStatesVisited());
            System.out.println("Found path of length " + (result.depth() + 1) + ":");
            System.out.println(BasicNode.printPathTowardsNode(result));
        }, () -> exit("No solution was found."));
    }

    private static SearchAlgorithms checkAlgorithm(String algorithm) {
        SearchAlgorithms alg = null;
        try {
            alg = SearchAlgorithms.valueOf(algorithm);
        } catch (IllegalArgumentException e) {
            exit("Valid search algorithms are: " + EnumSet.allOf(SearchAlgorithms.class) + " case insensitive.");
        }
        return alg;
    }

    public static void exit(String message) {
        System.out.println(message);
        System.exit(0);
    }

}
