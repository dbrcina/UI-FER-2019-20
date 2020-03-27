package hr.fer.zemris.ui.model;

import hr.fer.zemris.search.structure.StateCostPair;

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

    public Set<String> states() {
        return transitions.keySet();
    }

    public int stateSpaceSize() {
        return states().size();
    }

    public int totalTransitions() {
        return transitions.values()
                .stream()
                .mapToInt(Set::size)
                .sum();
    }

    public void toBidirectionalGraph() {
        for (Map.Entry<String, Set<StateCostPair<String>>> entry : transitions.entrySet()) {
            for (StateCostPair<String> value : entry.getValue()) {
                transitions.get(value.getState()).add(value);
            }
        }
    }

}
