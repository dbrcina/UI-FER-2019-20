package hr.fer.zemris.search;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Breadth first search algorithm.
 */
public class BFS<S> extends SearchAlgorithm<S, S> {

    @Override
    public Optional<BasicNode<S>> search(S s0, Function<S, Set<S>> succ, Predicate<S> goal) {
        System.out.println("Running bfs:");
        // prepare open and closed
        Deque<BasicNode<S>> open = new LinkedList<>();
        Set<S> closed = new HashSet<>();
        open.add(new BasicNode<>(s0, null));

        while (!open.isEmpty()) {

            // check for head
            BasicNode<S> n = open.removeFirst();
            if (goal.test(n.getState())) return Optional.of(n);
            closed.add(n.getState());
            setStatesVisited(closed.size());

            // go through all successors
            for (S successor : succ.apply(n.getState())) {
                if (closed.contains(successor)) continue;
                BasicNode<S> m = new BasicNode<>(successor, n);
                if (goal.test(successor)) return Optional.of(m);
                open.addLast(m);
            }
        }
        return Optional.empty();
    }

}
