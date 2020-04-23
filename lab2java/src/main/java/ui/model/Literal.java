package ui.model;

import java.util.Objects;

/**
 * Models literal in propositional logic.
 */
public class Literal {

    private final String name;
    private boolean isNegated;

    private Literal(String name, boolean isNegated) {
        this.name = name;
        this.isNegated = isNegated;
    }

    public Literal(String name) {
        int lastIndexOfNegation = name.lastIndexOf('~');
        this.name = name.substring(lastIndexOfNegation + 1);
        if (lastIndexOfNegation == -1) {
            isNegated = false;
        } else {
            int numberOfNegations = name.substring(0, lastIndexOfNegation + 1).length();
            isNegated = numberOfNegations % 2 != 0;
        }
    }

    public Literal negate() {
        isNegated = !isNegated;
        return this;
    }

    public Literal nNegate() {
        return copy().negate();
    }

    public Literal copy() {
        return new Literal(name, isNegated);
    }

    @Override
    public String toString() {
        return (isNegated ? "~" : "") + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Literal)) return false;
        Literal literal = (Literal) o;
        return isNegated == literal.isNegated &&
                name.equals(literal.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isNegated);
    }

}
