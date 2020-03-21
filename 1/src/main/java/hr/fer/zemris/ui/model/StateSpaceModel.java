package hr.fer.zemris.ui.model;

import hr.fer.zemris.search.structure.StateCostPair;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Simple state space data model which contains everything for search problem:
 * <ul>
 *     <li>initial state</li>
 *     <li>transitions between states</li>
 *     <li>goal states</li>
 * </ul>
 */
public class StateSpaceModel {

    private String initialState;
    private Set<String> goalStates = new LinkedHashSet<>();
    private Map<String, Set<StateCostPair<String>>> transitions = new LinkedHashMap<>();

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

    public Set<String> states() {
        return transitions.keySet();
    }

}
