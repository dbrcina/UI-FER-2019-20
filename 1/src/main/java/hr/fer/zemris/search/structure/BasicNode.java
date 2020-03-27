package hr.fer.zemris.search.structure;

import java.util.Deque;
import java.util.LinkedList;
import java.util.StringJoiner;

public class BasicNode<S> {

    private S state;
    private BasicNode<S> parent;

    public BasicNode(S state, BasicNode<S> parent) {
        this.state = state;
        this.parent = parent;
    }

    public S getState() {
        return state;
    }

    public BasicNode<S> getParent() {
        return parent;
    }

    public int depth() {
        if (parent == null) return 0;
        return 1 + parent.depth();
    }

    @Override
    public String toString() {
        return state.toString();
    }

    /**
     * Creates an array of nodes from start to goal node.
     *
     * @param goal goal node.
     * @param <T>  state type.
     * @return an array of {@link BasicNode} objects.
     */
    public static <T> BasicNode<T>[] fromStartToGoal(BasicNode<T> goal) {
        Deque<BasicNode<T>> nodes = new LinkedList<>();
        fromGoalToStartRecursive(nodes, goal);
        return nodes.toArray(BasicNode[]::new);
    }

    private static <T> void fromGoalToStartRecursive(Deque<BasicNode<T>> nodes, BasicNode<T> node) {
        if (node != null) {
            nodes.addFirst(node);
            fromGoalToStartRecursive(nodes, node.parent);
        }
    }

    /**
     * Constructs a path from start node to provided <i>node</i>.
     *
     * @param node node.
     * @param <T>  state type.
     * @return string representation of path.
     */
    public static <T> String printPathTowardsNode(BasicNode<T> node) {
        BasicNode<T>[] nodes = fromStartToGoal(node);
        StringJoiner sj = new StringJoiner(" =>\n");
        for (BasicNode<T> n : nodes) sj.add(n.toString());
        return sj.toString();
    }

}
