package hr.fer.zemris.search;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Unified cost search algorithm.
 */
public class UCS<S> extends SearchAlgorithm<S, StateCostPair<S>> {

    @Override
    public Optional<CostNode<S>> search(
            S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        // prepare open and closed
        Queue<CostNode<S>> open = new PriorityQueue<>();
        Set<S> closed = new HashSet<>();
        open.add(new CostNode<>(s0, null, 0.0));

        while (!open.isEmpty()) {

            // check for head
            CostNode<S> n = open.remove();
            if (goal.test(n.getState())) return Optional.of(n);
            closed.add(n.getState());
            setStatesVisited(closed.size());

            // go through all successors
            for (StateCostPair<S> successor : succ.apply(n.getState())) {
                // skip closed ones
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
                    if (successorCost > temp.getCost()) {
                        successorIsCheaper = false;
                    } else {
                        it.remove();
                    }
                    break;
                }

                // insert successor into open collection if path to it is cheaper
                if (successorIsCheaper) {
                    open.add(new CostNode<>(successor.getState(), n, successorCost));
                }

            }
        }
        return Optional.empty();
    }

}
