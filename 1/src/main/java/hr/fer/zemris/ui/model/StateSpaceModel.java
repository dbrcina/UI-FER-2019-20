package hr.fer.zemris.ui.model;

import hr.fer.zemris.search.structure.StateCostPair;

import java.util.*;

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

    public StateSpaceModel reverseGraph() {
        Map<String, Set<StateCostPair<String>>> reversed = new HashMap<>();
        transitions.keySet().forEach(k -> reversed.putIfAbsent(k, new HashSet<>()));
        for (Map.Entry<String, Set<StateCostPair<String>>> entry : transitions.entrySet()) {
            for (StateCostPair<String> pair : entry.getValue()) {
                reversed.get(pair.getState()).add(new StateCostPair<>(entry.getKey(), pair.getCost()));
            }
        }
        StateSpaceModel model = new StateSpaceModel();
        model.initialState = initialState;
        model.goalStates = new HashSet<>(goalStates);
        model.transitions = reversed;
        return model;
    }

}
