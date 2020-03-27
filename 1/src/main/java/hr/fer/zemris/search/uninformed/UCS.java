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
        Set<S> closed = new HashSet<>();

        open.add(new CostNode<>(s0, null, 0.0));

        while (!open.isEmpty()) {
            CostNode<S> n = open.remove();
            if (goal.test(n.getState())) return Optional.of(n);
            closed.add(n.getState());
            setStatesVisited(closed.size());

            for (StateCostPair<S> successor : succ.apply(n.getState())) {
                if (closed.contains(successor.getState())) continue;

                // go through open ones and check whether current successor
                // already exists and if it exists update it with the
                // better cost
                double successorCost = n.getCost() + successor.getCost();
                boolean successorIsCheaper = true;
                Iterator<CostNode<S>> it = open.iterator();
                while (it.hasNext()) {
                    CostNode<S> temp = it.next();
                    if (!temp.getState().equals(successor.getState())) continue;
                    if (successorCost > temp.getCost()) successorIsCheaper = false;
                    else it.remove();
                    break;
                }

                // insert successor into open collection if path to it is cheaper
                // than original one or if successor doesn't exist in open collection
                if (successorIsCheaper) {
                    open.add(new CostNode<>(successor.getState(), n, successorCost));
                }
            }
        }
        return Optional.empty();
    }

}
