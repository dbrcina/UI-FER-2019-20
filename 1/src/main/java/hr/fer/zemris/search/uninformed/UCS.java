package hr.fer.zemris.search.uninformed;

import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.CostNode;
import hr.fer.zemris.search.structure.StateCostPair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Uniform cost search algorithm(for example Dijkstra's algorithm).
 */
public class UCS<S> extends SearchAlgorithm<S> {

    @Override
    public Optional<BasicNode<S>> search(
            S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        Queue<CostNode<S>> open = new PriorityQueue<>();
        Map<S, CostNode<S>> openMap = new HashMap<>();
        Set<S> closed = new HashSet<>();

        CostNode<S> n0 = new CostNode<>(s0, null, 0.0);
        open.add(n0);
        openMap.put(s0, n0);

        while (!open.isEmpty()) {
            CostNode<S> n = open.remove();
            n = openMap.remove(n.getState());
            if (goal.test(n.getState())) return Optional.of(n);
            closed.add(n.getState());
            setStatesVisited(closed.size());

            for (StateCostPair<S> successor : succ.apply(n.getState())) {
                S state = successor.getState();
                if (closed.contains(state)) continue;
                double cost = n.getCost() + successor.getCost();
                CostNode<S> m = new CostNode<>(state, n, cost);
                if (openMap.containsKey(state)) {
                    openMap.compute(state, (k, v) -> v.compareTo(m) < 0 ? v : m);
                } else {
                    open.add(m);
                    openMap.put(state, m);
                }
            }
        }
        return Optional.empty();
    }

}
