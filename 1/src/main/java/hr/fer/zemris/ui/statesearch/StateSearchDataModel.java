package hr.fer.zemris.ui.statesearch;

import hr.fer.zemris.search.StateCostPair;

import java.util.Map;
import java.util.Set;

public class StateSearchDataModel {

    private String initialState;
    private Set<String> goalStates;
    private Map<String, Set<StateCostPair<String>>> transitions;

    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public Set<String> getGoalStates() {
        return goalStates;
    }

    public void setGoalStates(Set<String> goalStates) {
        this.goalStates = goalStates;
    }

    public Set<StateCostPair<String>> getTransition(String state) {
        return transitions.get(state);
    }

    public void setTransitions(Map<String, Set<StateCostPair<String>>> transitions) {
        this.transitions = transitions;
    }

}
