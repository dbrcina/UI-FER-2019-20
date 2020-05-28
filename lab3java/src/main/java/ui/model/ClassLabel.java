package ui.model;

import java.util.Collection;
import java.util.Objects;
import java.util.TreeSet;

public class ClassLabel {

    private static final Collection<String> classLabelValues = new TreeSet<>();

    private final String name;
    private final String value;

    public ClassLabel(String name, String value) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassLabel)) return false;
        ClassLabel that = (ClassLabel) o;
        return name.equals(that.name) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    public static void extendValues(String value) {
        classLabelValues.add(value);
    }

    public static Collection<String> values() {
        return classLabelValues;
    }

}
