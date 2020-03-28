package hr.fer.zemris.search.heuristic;

import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.HeuristicNode;
import hr.fer.zemris.search.structure.StateCostPair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * A* search algorithm.
 */
public class Astar<S> extends SearchAlgorithm<S> {

    private ToDoubleFunction<S> heuristic;

    public Astar(ToDoubleFunction<S> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Optional<BasicNode<S>> search(
            S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        Queue<HeuristicNode<S>> open = new PriorityQueue<>(HeuristicNode.COMPARE_BY_TOTAL);
        Map<S, HeuristicNode<S>> openMap = new HashMap<>();
        Set<S> closed = new HashSet<>();

        HeuristicNode<S> n0 = new HeuristicNode<>(s0, null, 0.0, heuristic.applyAsDouble(s0));
        open.add(n0);
        openMap.put(s0, n0);

        while (!open.isEmpty()) {
            HeuristicNode<S> n = open.remove();
            n = openMap.remove(n.getState());
            if (goal.test(n.getState())) return Optional.of(n);
            closed.add(n.getState());
            setStatesVisited(closed.size());

            for (StateCostPair<S> successor : succ.apply(n.getState())) {
                S state = successor.getState();
                if (closed.contains(state)) continue;
                double cost = n.getCost() + successor.getCost();
                double total = cost + heuristic.applyAsDouble(state);
                HeuristicNode<S> m = new HeuristicNode<>(state, n, cost, total);
                if (openMap.containsKey(state)) {
                    openMap.compute(state, (k, v) ->
                            v.getTotalEstimatedCost() < m.getTotalEstimatedCost() ? v : m);
                } else {
                    open.add(m);
                    openMap.put(state, m);
                }
            }
        }
        return Optional.empty();
    }

}
