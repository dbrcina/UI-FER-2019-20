package ui.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Propositional logic model.
 */
public class PLModel {

    private final Collection<CNFClause> clauses;
    private CNFClause goalClause;

    public PLModel(Collection<CNFClause> clauses, CNFClause goalClause) {
        //this.clauses = new HashSet<>(clauses);
        this.clauses = new LinkedList<>(clauses);
        this.goalClause = goalClause;
    }

    public Collection<CNFClause> getClauses() {
        return clauses;
    }

    public CNFClause getGoalClause() {
        return goalClause;
    }

    public void setGoalClause(CNFClause goalClause) {
        this.goalClause = goalClause;
    }

    public void addClause(CNFClause clause) {
        clauses.add(clause);
    }

    public void removeClause(CNFClause clause) {
        clauses.remove(clause);
    }

}
