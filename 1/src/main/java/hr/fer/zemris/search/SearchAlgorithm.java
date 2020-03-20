package hr.fer.zemris.search;

import hr.fer.zemris.search.structure.BasicNode;

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
 * @param <M> data model of successors.
 */
public abstract class SearchAlgorithm<S, M> {

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
     * @param goal goal predicate.
     * @return an optional value of object that is or extends {@link BasicNode}.
     */
    public abstract Optional<? extends BasicNode<S>> search(
            S s0, Function<S, Set<M>> succ, Predicate<S> goal);

}
