package ui;

import ui.model.CNFClause;
import ui.model.Literal;
import ui.model.PLModel;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ResolutionUtils {

    public static boolean plResolution(PLModel model, StringBuilder sb) {
        Collection<CNFClause> allClauses = new HashSet<>(model.getClauses());
        allClauses.forEach(c -> sb.append(c.getIndex()).append(". ").append(c).append("\n"));
        CNFClause goalClause = model.getGoalClause();
        Collection<CNFClause> negatedGoalClause = goalClause.negate();
        Collection<CNFClause> sOs = new HashSet<>(model.getGoalClause().negate());
        sb.append("=============").append("\n");
        sOs.forEach(c -> sb.append(c.getIndex()).append(". ").append(c).append("\n"));
        sb.append("=============").append("\n");
        int index = goalClause.getIndex() + negatedGoalClause.size();
        while (true) {
            allClauses.removeIf(CNFClause::isTautology);
            sOs.removeIf(CNFClause::isTautology);
            Collection<Entry<CNFClause, CNFClause>> clausePairs = selectClauses(allClauses, sOs);
            if (clausePairs.isEmpty()) {
                return false;
            }
            for (Entry<CNFClause, CNFClause> clausePair : clausePairs) {
                CNFClause c1 = clausePair.getKey();
                CNFClause c2 = clausePair.getValue();
                Collection<CNFClause> resolvents = plResolve(c1, c2, index);
                index += resolvents.size();
                if (resolvents.isEmpty()) {
                    sb.append(index).append(". NIL (");
                    sb.append(c1.getIndex()).append(", ").append(c2.getIndex()).append(")");
                    sb.append("\n");
                    sb.append("=============").append("\n");
                    return true;
                }
                resolvents.forEach(r -> {
                    sb.append(r.getIndex()).append(". ").append(r).append(" (");
                    sb.append(c1.getIndex()).append(", ").append(c2.getIndex()).append(")");
                    sb.append("\n");
                });
                resolvents.removeIf(CNFClause::isTautology);
                if (resolvents.isEmpty()) {
                    return false;
                }

                for (CNFClause resolvent : resolvents) {
                    allClauses.removeIf(c -> c.isSubsumedBy(resolvent));
                    sOs.removeIf(c -> c.isSubsumedBy(resolvent));
                }
                sOs.addAll(resolvents);
                allClauses.addAll(resolvents);
            }
        }
    }

    private static Collection<Entry<CNFClause, CNFClause>> selectClauses(
            Collection<CNFClause> clauses, Collection<CNFClause> sOs) {
        Collection<Entry<CNFClause, CNFClause>> clausePairs = new HashSet<>();
        for (CNFClause clause : clauses) {
            for (CNFClause sOsClause : sOs) {
                for (Literal l : sOsClause.getLiterals()) {
                    if (clause.containsLiteral(l.nNegate())) {
                        clausePairs.add(new AbstractMap.SimpleEntry<>(clause, sOsClause));
                    }
                }
            }
        }
        return clausePairs;
    }

    private static Collection<CNFClause> plResolve(CNFClause c1, CNFClause c2, int index) {
        Collection<CNFClause> resolvents = new HashSet<>();
        Collection<Literal> literals = new LinkedList<>();
        Collection<Literal> literals1 = c1.getLiterals();
        Collection<Literal> literals2 = c2.getLiterals();
        Collection<Literal> literalsToDelete = new HashSet<>();
        for (Literal l1 : literals1) {
            for (Literal l2 : literals2) {
                if (l1.equals(l2.nNegate())) {
                    literalsToDelete.add(l1);
                }
            }
        }
        for (Literal l : literalsToDelete) {
            literals.addAll(literals1.stream()
                    .filter(li -> !li.equals(l))
                    .collect(Collectors.toSet()));
            literals.addAll(literals2.stream()
                    .filter(li -> !li.equals(l.nNegate()))
                    .collect(Collectors.toSet()));
            if (!literals.isEmpty()) {
                resolvents.add(new CNFClause(literals, index++));
                literals.clear();
            }
        }
        return resolvents;
    }

}
