package hr.fer.zemris.search;

import hr.fer.zemris.search.structure.BasicNode;
import hr.fer.zemris.search.structure.StateCostPair;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An abstract class of search algorithms that declares {@link #search(Object, Function, Predicate)}
 * method. It also keeps track about number of states visited which can be accessed through certain
 * getter.
 *
 * @param <S> state type.
 */
public abstract class SearchAlgorithm<S> {

    private int statesVisited;

    public int getStatesVisited() {
        return statesVisited;
    }

    protected void setStatesVisited(int statesVisited) {
        this.statesVisited = statesVisited;
    }

    /**
     * Executes state space search algorithm.
     *
     * @param s0   initial state.
     * @param succ successors function.
     * @param goal goal predicate.
     * @return an optional value of {@link BasicNode}.
     */
    public abstract Optional<BasicNode<S>> search(
            S s0, Function<S, Set<StateCostPair<S>>> succ, Predicate<S> goal);

}
