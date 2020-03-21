package hr.fer.zemris.ui.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple heuristic data model. It contains heuristic values for all states.
 */
public class HeuristicModel {

    private Map<String, Double> heuristicValues = new LinkedHashMap<>();

    public double heuristicValue(String state) {
        return heuristicValues.get(state);
    }

    public void setHeuristicValues(Map<String, Double> heuristicValues) {
        this.heuristicValues = heuristicValues;
    }

}
