package ui.model;

import java.util.Collection;

/**
 * Propositional logic model.
 */
public class PLModel {

    private final Collection<CNFClause> clauses;
    private CNFClause goalClause;

    public PLModel(Collection<CNFClause> clauses, CNFClause goalClause) {
        this.clauses = clauses;
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
