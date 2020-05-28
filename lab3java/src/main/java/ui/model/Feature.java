package ui.model;

public class Feature implements Comparable<Feature> {

    private final String name;
    private final String value;

    public Feature(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Feature other) {
        return this.name.compareTo(other.name);
    }

}
