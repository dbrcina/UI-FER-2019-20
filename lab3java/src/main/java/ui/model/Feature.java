package ui.model;

import java.util.*;

public class Feature implements Comparable<Feature> {

    private static final Map<String, Collection<String>> featuresMap = new HashMap<>();

    private final String name;
    private final String value;

    public Feature(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(Feature other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Feature)) return false;
        Feature feature = (Feature) other;
        return name.equals(feature.name) &&
                value.equals(feature.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    public static Collection<String> extendForFeature(String feature, String value) {
        return featuresMap.merge(feature, new TreeSet<>(Arrays.asList(value)), (oldV, newV) -> {
            oldV.addAll(newV);
            return oldV;
        });
    }

    public static Collection<String> allFeatureNames() {
        return featuresMap.keySet();
    }

    public static Collection<String> valuesForFeature(String feature) {
        return featuresMap.getOrDefault(feature, Collections.emptyList());
    }

}
