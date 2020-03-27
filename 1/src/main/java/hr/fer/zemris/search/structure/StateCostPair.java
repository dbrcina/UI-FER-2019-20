package hr.fer.zemris.search.structure;

public final class StateCostPair<S> implements Comparable<StateCostPair<String>> {

    private S state;
    private double cost;

    public StateCostPair(S state, double cost) {
        this.state = state;
        this.cost = cost;
    }

    public S getState() {
        return state;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public int compareTo(StateCostPair<String> other) {
        return Double.compare(this.cost, other.cost);
    }

}
