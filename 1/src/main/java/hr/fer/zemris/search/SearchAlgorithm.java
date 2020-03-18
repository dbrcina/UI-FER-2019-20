package hr.fer.zemris.search;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An abstract class of search algorithms that declares {@link #search(Object, Function, Predicate)}
 * method. It also keeps track about number of states visited which can be accessed through certain
 * getter.
 *
 * @param <S> type used in search algorithms, for example strings, numbers etc.
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
     * Performs state space search algorithm.
     *
     * @param s0   initial state.
     * @param succ successor function.
     * @param goal goal state(s).
     * @return an optional value of {@link BasicNode}.
     */
    public abstract Optional<BasicNode<S>> search(S s0, Function<S, Set<S>> succ, Predicate<S> goal);

}
