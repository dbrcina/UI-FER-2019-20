package hr.fer.zemris.ui.util;

import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.structure.CostNode;
import hr.fer.zemris.search.structure.StateCostPair;
import hr.fer.zemris.search.uninformed.UCS;
import hr.fer.zemris.ui.model.HeuristicModel;
import hr.fer.zemris.ui.model.StateSpaceModel;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility class used to validate heuristic.
 */
public class HeuristicChecks {

    /**
     * Checks whether heuristic is optimistic. Feedback is given through string text.
     *
     * @param model  state space model.
     * @param hmodel heuristic model.
     * @return string representation of this check.
     */
    public static String checkOptimistic(StateSpaceModel model, HeuristicModel hmodel) {
        StringBuilder sb = new StringBuilder();
        Function<String, Set<StateCostPair<String>>> succ = model::getTransition;
        Predicate<String> goal = s -> model.getGoalStates().contains(s);
        SearchAlgorithm<String> alg = new UCS<>();
        boolean optimistic = true;
        int counter = 0;
        for (String state : model.states()) {
            CostNode<String> result = (CostNode<String>) alg.search(state, succ, goal).get();
            double cost = result.getCost();
            double heuristicValue = hmodel.heuristicValue(state);
            if (cost < heuristicValue) {
                counter++;
                optimistic = false;
                sb.append("\t[ERR] h(").append(state).append(") > h*: ");
                sb.append(heuristicValue).append(" > ").append(cost).append("\n");
            }
        }
        if (counter >= 50) {
            sb.setLength(0);
            sb.append("\t[ERR] ").append(counter).append(" errors, ommiting output.").append("\n");
        }
        sb.append("Heuristic is ").append(optimistic ? "" : "not ").append("optimistic.");
        return sb.toString();
    }

    /**
     * Checks whether heuristic is optimistic. Feedback is given through string text.
     *
     * @param model  state space model.
     * @param hmodel heuristic model.
     * @return string representation of this check.
     */
    public static String checkOptimisticEnhanced(StateSpaceModel model, HeuristicModel hmodel) {
        StringBuilder sb = new StringBuilder();
        model.toBidirectionalGraph();
        boolean optimistic = true;
        int counter = 0;
        for (String goalState : model.getGoalStates()) {
            Queue<StateCostPair<String>> open = new PriorityQueue<>();
            open.add(new StateCostPair<>(goalState, 0.0));
            Set<String> states = new HashSet<>(model.states());
            Set<StateCostPair<String>> results = new HashSet<>();
            while (!open.isEmpty()) {
                StateCostPair<String> n = open.remove();
                for (StateCostPair<String> succ : model.getTransition(n.getState())) {
                    if (!states.remove(succ.getState())) continue;
                    open.add(new StateCostPair<>(succ.getState(), n.getCost() + succ.getCost()));
                }
            }
            for (StateCostPair<String> pair : open) {
                String state = pair.getState();
                double cost = pair.getCost();
                double heuristicValue = hmodel.heuristicValue(state);
                if (cost < heuristicValue) {
                    counter++;
                    optimistic = false;
                    sb.append("\t[ERR] h(").append(state).append(") > h*: ");
                    sb.append(heuristicValue).append(" > ").append(cost).append("\n");
                }
            }
        }
        if (counter >= 50) {
            sb.setLength(0);
            sb.append("\t[ERR] ").append(counter).append(" errors, ommiting output.").append("\n");
        }
        sb.append("Heuristic is ").append(optimistic ? "" : "not ").append("optimistic.");
        return sb.toString();
    }

    /**
     * Checks whether heuristic is consistent. Feedback is given through string text.
     *
     * @param model  state space model.
     * @param hmodel heuristic model.
     * @return string representation of this check.
     */
    public static String checkConsistent(StateSpaceModel model, HeuristicModel hmodel) {
        StringBuilder sb = new StringBuilder();
        boolean consistent = true;
        int counter = 0;
        for (String s1 : model.states()) {
            double h1 = hmodel.heuristicValue(s1);
            for (StateCostPair<String> stateCostPair : model.getTransition(s1)) {
                String s2 = stateCostPair.getState();
                double c = stateCostPair.getCost();
                double h2 = hmodel.heuristicValue(s2);
                if (h1 > h2 + c) {
                    counter++;
                    consistent = false;
                    sb.append("\t[ERR] h(").append(s1).append(") > h(").append(s2).append(") + c: ");
                    sb.append(h1).append(" > ").append(h2).append(" + ").append(c).append("\n");
                }
            }
        }
        if (counter >= 50) {
            sb.setLength(0);
            sb.append("\t[ERR] ").append(counter).append(" errors, ommiting output.").append("\n");
        }
        sb.append("Heuristic is ").append(consistent ? "" : "not ").append("consistent.");
        return sb.toString();
    }

}
