package hr.fer.zemris.ui.uninformed;

import hr.fer.zemris.search.structure.StateCostPair;

import java.util.Map;
import java.util.Set;

public class UninformedSearchDataModel {

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

    public int stateSpaceSize() {
        return transitions.keySet().size();
    }

    public int totalTransitions() {
        return transitions.values()
                .stream()
                .mapToInt(Set::size)
                .sum();
    }

}
