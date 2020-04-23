package ui.model;

import java.util.Collection;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class CNFClause {

    private final Collection<Literal> literals;
    private final int index;

    public CNFClause(Collection<Literal> literals, int index) {
        this.literals = literals.stream().map(Literal::copy).collect(Collectors.toSet());
        this.index = index;
    }

    public Collection<Literal> getLiterals() {
        return literals;
    }

    public int getIndex() {
        return index;
    }

    public CNFClause negate() {
        literals.forEach(Literal::negate);
        return this;
    }

    public CNFClause nNegate() {
        return copy().negate();
    }

    public CNFClause copy() {
        return new CNFClause(literals, index);
    }

    public boolean isSubsumed(CNFClause other) {
        return other.literals.containsAll(literals);
    }

    public boolean isTautology() {
        for (Literal literal : literals) {
            if (literals.contains(literal.nNegate())) return true;
        }
        return false;
    }

    public boolean containsLiteral(Literal l) {
        return literals.contains(l);
    }

    public boolean addLiteral(Literal l) {
        return literals.add(l);
    }

    public boolean removeLiteral(Literal l) {
        return literals.remove(l);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" v ");
        literals.forEach(l -> sj.add(l.toString()));
        return sj.toString();
    }

}
