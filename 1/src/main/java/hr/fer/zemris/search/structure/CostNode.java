package hr.fer.zemris.search.structure;

public class CostNode<S> extends BasicNode<S> implements Comparable<CostNode<S>> {

    private double cost;

    public CostNode(S state, CostNode<S> parent, double cost) {
        super(state, parent);
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public CostNode<S> getParent() {
        return (CostNode<S>) super.getParent();
    }

    @Override
    public int compareTo(CostNode<S> other) {
        return Double.compare(this.cost, other.cost);
    }

}
