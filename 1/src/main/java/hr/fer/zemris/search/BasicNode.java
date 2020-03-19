package hr.fer.zemris.search;

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

    public static <T> String printPathTowardsNode(BasicNode<T> node) {
        return pathFromFirstNodeRecursive(node);
    }

    private static <T> String pathFromFirstNodeRecursive(BasicNode<T> node) {
        if (node.parent == null) return node.toString();
        return pathFromFirstNodeRecursive(node.getParent()) + " =>\n" + node;
    }

}
