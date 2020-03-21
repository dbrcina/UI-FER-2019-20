package hr.fer.zemris.search.structure;

import java.util.Comparator;

public class HeuristicNode<S> extends CostNode<S> {

    private double totalEstimatedCost;

    public HeuristicNode(S state, CostNode<S> parent, double cost, double totalEstimatedCost) {
        super(state, parent, cost);
        this.totalEstimatedCost = totalEstimatedCost;
    }

    public double getTotalEstimatedCost() {
        return totalEstimatedCost;
    }

    @Override
    public HeuristicNode<S> getParent() {
        return (HeuristicNode<S>) super.getParent();
    }

    public static final Comparator<HeuristicNode<?>> COMPARE_BY_TOTAL =
            Comparator.comparingDouble(h -> h.totalEstimatedCost);

}
