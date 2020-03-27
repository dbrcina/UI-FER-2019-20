package hr.fer.zemris.search.uninformed;

import hr.fer.zemris.search.SearchAlgorithm;
import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.StateCostPair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Breadth first search algorithm.<br> This implementation is enchanced one:
 * <ul>
 *     <li>has collection that keeps track about closed nodes to avoid cycles,</li>
 *     <li>tests the child nodes before putting them in collection of open nodes to lower the time/space
 *     complexity from 2^(d+1) to 2^d, where d is distance(depth) of optimal result.
 *     </li>
 * </ul>
 */
public class BFS<S> extends SearchAlgorithm<S> {

    @Override
    public Optional<BasicNode<S>> search(
            S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal) {
        Deque<BasicNode<S>> open = new LinkedList<>();
        Set<S> closed = new HashSet<>();

        BasicNode<S> head = new BasicNode<>(s0, null);
        if (goal.test(head.getState())) return Optional.of(head);

        open.add(head);

        while (!open.isEmpty()) {
            BasicNode<S> n = open.removeFirst();
            closed.add(n.getState());
            setStatesVisited(closed.size());

            Set<S> successors = succ.apply(n.getState()).stream()
                    .map(StateCostPair::getState)
                    .collect(Collectors.toSet());
            for (S successor : successors) {
                if (closed.contains(successor)) continue;
                BasicNode<S> m = new BasicNode<>(successor, n);
                if (goal.test(successor)) return Optional.of(m);
                open.addLast(m);
            }
        }
        return Optional.empty();
    }

}
