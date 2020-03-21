package hr.fer.zemris.search.structure;

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

    public static <T> BasicNode<T>[] fromStartToGoal(BasicNode<T> goal) {
        LinkedList<BasicNode<T>> nodes = new LinkedList<>();
        fromGoalToStartRecursive(nodes, goal);
        return nodes.toArray(BasicNode[]::new);
    }

    private static <T> void fromGoalToStartRecursive(LinkedList<BasicNode<T>> nodes, BasicNode<T> node) {
        if (node == null) return;
        nodes.push(node);
        fromGoalToStartRecursive(nodes, node.parent);
    }

    public static <T> String printPathTowardsNode(BasicNode<T> node) {
        BasicNode<T>[] nodes = fromStartToGoal(node);
        StringJoiner sj = new StringJoiner(" =>\n");
        for (BasicNode<T> n : nodes) sj.add(n.toString());
        return sj.toString();
    }

}
