package hr.fer.zemris.ui.uninformed;

import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.CostNode;
import hr.fer.zemris.search.structure.StateCostPair;
import hr.fer.zemris.search.uninformed.BFS;
import hr.fer.zemris.search.uninformed.UCS;
import hr.fer.zemris.ui.util.FileParser;
import hr.fer.zemris.ui.util.PathValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UninformedSearchSimulation {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Program expects one argument; path to definition file.");
            return;
        }
        // validate provided path
        Path file = null;
        try {
            file = PathValidator.validatePath(args[0]);
        } catch (Exception e) {
            exit(e.getMessage());
        }
        // DATA LOADING
        UninformedSearchDataModel model = dataLoading(file);
        System.out.println();
        // BREADTH FIRST SEARCH
        breadthFirstSearch(model);
        System.out.println();
        // UNIFORM COST SEARCH
        uniformCostSearch(model);
    }

    private static UninformedSearchDataModel dataLoading(Path file) {
        System.out.println("\033[1m   DATA LOADING:\033[0m");
        UninformedSearchDataModel model = null;
        try {
            model = FileParser.parseUninformedSearchFile(file);
        } catch (IOException e) {
            exit(e.getMessage());
        }
        System.out.println("Start state: " + model.getInitialState());
        System.out.println("End state(s): " + model.getGoalStates());
        System.out.println("State space size: " + model.stateSpaceSize());
        System.out.println("Total transitions: " + model.totalTransitions());
        return model;
    }

    private static void breadthFirstSearch(UninformedSearchDataModel model) {
        System.out.println("\033[1m   BREADTH FIRST SEARCH:\033[0m");
        String s0 = model.getInitialState();
        Function<String, Set<String>> succ = s -> model.getTransition(s).stream()
                .map(StateCostPair::getState)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        Predicate<String> goal = s -> model.getGoalStates().contains(s);
        SearchAlgorithm<String, String> alg = new BFS<>();
        System.out.println("Running bfs:");
        alg.search(s0, succ, goal).ifPresentOrElse(result -> {
            System.out.println("States visited = " + alg.getStatesVisited());
            System.out.println("Found path of length " + (result.depth() + 1) + ":");
            System.out.println(BasicNode.printPathTowardsNode(result));
        }, () -> exit("No solution was found."));
    }

    private static void uniformCostSearch(UninformedSearchDataModel model) {
        System.out.println("\033[1m   UNIFORM COST SEARCH:\033[0m");
        String s0 = model.getInitialState();
        Function<String, Set<StateCostPair<String>>> succ = model::getTransition;
        Predicate<String> goal = s -> model.getGoalStates().contains(s);
        SearchAlgorithm<String, StateCostPair<String>> alg = new UCS<>();
        System.out.println("Running ucs:");
        alg.search(s0, succ, goal).ifPresentOrElse(result -> {
            System.out.println("States visited = " + alg.getStatesVisited());
            System.out.println("Found path of length " + (result.depth() + 1)
                    + " with total cost " + ((CostNode<String>) result).getCost() + ":");
            System.out.println(BasicNode.printPathTowardsNode(result));
        }, () -> exit("No solution was found."));
    }

    private static void exit(String message) {
        System.out.println(message);
        System.exit(0);
    }

}
