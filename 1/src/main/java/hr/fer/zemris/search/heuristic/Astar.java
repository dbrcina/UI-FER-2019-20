package hr.fer.zemris.search.heuristic;

import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.HeuristicNode;
import hr.fer.zemris.search.structure.StateCostPair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A* search algorithm.
 */
public class Astar<S> extends SearchAlgorithm<S> {

    private Function<S, Double> heuristic;

    public Astar(Function<S, Double> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public Optional<BasicNode<S>> search(
            S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        // prepare open and closed collections
        Queue<HeuristicNode<S>> open = new PriorityQueue<>(HeuristicNode.COMPARE_BY_TOTAL);
        Set<S> closed = new HashSet<>();
        open.add(new HeuristicNode<>(s0, null, 0.0, heuristic.apply(s0)));

        while (!open.isEmpty()) {
            // check head node
            HeuristicNode<S> n = open.remove();
            if (goal.test(n.getState())) return Optional.of(n);
            closed.add(n.getState());
            setStatesVisited(closed.size());

            // go through all successors
            for (StateCostPair<S> successor : succ.apply(n.getState())) {
                // skip closed ones
                if (closed.contains(successor.getState())) continue;

                // calculate new cost
                double cost = n.getCost() + successor.getCost();

                // check if current state already exist in open queue
                // if it exists, update its cost if needed
                boolean successorIsCheaper = true;
                Iterator<HeuristicNode<S>> it = open.iterator();
                while (it.hasNext()) {
                    HeuristicNode<S> temp = it.next();
                    if (!temp.getState().equals(successor.getState())) continue;
                    if (cost > temp.getCost()) successorIsCheaper = false;
                    else it.remove();
                    break;
                }

                // insert successor into open collection if path to it is cheaper
                // than original one or if successor doesn't exist in open collection
                if (successorIsCheaper) {
                    double total = cost + heuristic.apply(successor.getState());
                    open.add(new HeuristicNode<>(successor.getState(), n, cost, total));
                }
            }
        }
        return Optional.empty();
    }

}
