package hr.fer.zemris.search.uninformed;

import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.structure.StateCostPair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Breadth first search algorithm.
 */
public class BFS<S> extends SearchAlgorithm<S> {

    @Override
    public Optional<BasicNode<S>> search(
            S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        // prepare open and closed collections
        Deque<BasicNode<S>> open = new LinkedList<>();
        Set<S> closed = new HashSet<>();
        open.add(new BasicNode<>(s0, null));

        while (!open.isEmpty()) {
            // check head node
            BasicNode<S> n = open.removeFirst();
            if (goal.test(n.getState())) return Optional.of(n);
            closed.add(n.getState());
            setStatesVisited(closed.size());

            // go through all successors
            Set<S> successors = succ.apply(n.getState()).stream()
                    .map(StateCostPair::getState)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            for (S successor : successors) {
                // skip closed ones
                if (closed.contains(successor)) continue;
                BasicNode<S> m = new BasicNode<>(successor, n);
                if (goal.test(successor)) return Optional.of(m);
                open.addLast(m);
            }
        }
        return Optional.empty();
    }

}
