package ui.model;

import java.util.*;

public class CNFClause {

    private final Collection<Literal> literals;
    private final int index;

    public CNFClause(Collection<Literal> literals, int index) {
        this.literals = new HashSet<>(literals);
        this.index = index;
    }

    public Collection<Literal> getLiterals() {
        return literals;
    }

    public int getIndex() {
        return index;
    }

    public Collection<CNFClause> negate() {
        Collection<CNFClause> clauses = new LinkedList<>();
        int index = this.index;
        for (Literal literal : literals) {
            clauses.add(new CNFClause(Arrays.asList(literal.nNegate()), index++));
        }
        return clauses;
    }

    public boolean isSubsumedBy(CNFClause other) {
        return literals.containsAll(other.literals);
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

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(" v ");
        literals.forEach(l -> sj.add(l.toString()));
        return sj.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof CNFClause)) return false;
        CNFClause clause = (CNFClause) other;
        return literals.equals(clause.literals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literals);
    }

}
