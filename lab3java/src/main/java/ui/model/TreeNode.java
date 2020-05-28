package ui.model;

import java.util.Collection;
import java.util.StringJoiner;

public class TreeNode<T> {

    private final T branch;
    private final T value;
    private final Collection<TreeNode<T>> children;
    private final int depth;

    public TreeNode(T branch, T value, Collection<TreeNode<T>> children, int depth) {
        this.branch = branch;
        this.value = value;
        this.children = children;
        this.depth = depth;
    }

    public T getBranch() {
        return branch;
    }

    public T getValue() {
        return value;
    }

    public Collection<TreeNode<T>> getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {
        return String.format("%d:%s", depth, value);
    }

    public static <X> String constructPathByNodes(TreeNode<X> root) {
        if (root == null) return "";
        StringJoiner sj = new StringJoiner(", ");
        constructPathRecursive(root, sj);
        return sj.toString();
    }

    private static <X> void constructPathRecursive(TreeNode<X> node, StringJoiner sj) {
        if (node.children != null) {
            sj.add(node.toString());
            node.children.forEach(child -> constructPathRecursive(child, sj));
        }
    }

}
