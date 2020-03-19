package hr.fer.zemris.search;

public class StateCostPair<S> {

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
    public String toString() {
        return String.format("<%s, %.2f>", state, cost);
    }

}
