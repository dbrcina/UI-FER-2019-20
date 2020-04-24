package ui;

import ui.model.CNFClause;
import ui.model.Literal;
import ui.model.PLModel;

import java.util.*;
import java.util.stream.Collectors;

public class ResolutionUtils {

    public static boolean plResolution(PLModel model, StringBuilder sb, boolean verbose) {
        Collection<CNFClause> allClauses = model.getClauses().stream()
                .map(CNFClause::copy)
                .collect(Collectors.toList());
        CNFClause negatedGoalClause = model.getGoalClause().nNegate();
        if (verbose) {
            allClauses.forEach(c -> sb.append(c.getIndex()).append(". ").append(c).append("\n"));
            sb.append("===========").append("\n");
            sb.append(negatedGoalClause.getIndex()).append(". ").append(negatedGoalClause).append("\n");
            sb.append("===========").append("\n");
        }
        Collection<CNFClause> sOs = new LinkedList<>();
        sOs.add(negatedGoalClause);
        int index = negatedGoalClause.getIndex() + 1;
        while (true) {
            Collection<Map.Entry<CNFClause, CNFClause>> clausePairs = selectClauses(allClauses, sOs);
            if (clausePairs.isEmpty()) {
                if (verbose) sb.append("===========").append("\n");
                return false;
            }
            for (Map.Entry<CNFClause, CNFClause> clausePair : clausePairs) {
                CNFClause c1 = clausePair.getKey();
                CNFClause c2 = clausePair.getValue();
                Collection<CNFClause> resolvents = plResolve(c1, c2, index);
                resolvents.removeIf(CNFClause::isTautology);
                for (CNFClause resolvent : resolvents) {
                    allClauses.removeIf(c -> c.isSubsumedBy(resolvent));
                    sOs.removeIf(c -> c.isSubsumedBy(resolvent));
                }
                index += resolvents.size();
                if (verbose) {
                    boolean isFinal = false;
                    if (resolvents.isEmpty()) {
                        isFinal = true;
                        resolvents.add(new CNFClause(Arrays.asList(new Literal("NIL")), index));
                    }
                    resolvents.forEach(r -> sb.append(r.getIndex()).append(". ").append(r)
                            .append(" (").append(c1.getIndex()).append(", ")
                            .append(c2.getIndex()).append(")\n"));
                    if (isFinal) {
                        sb.append("===========").append("\n");
                        resolvents.clear();
                    }
                }
                if (resolvents.isEmpty()) {
                    return true;
                }
                sOs.addAll(resolvents);
                allClauses.addAll(resolvents);
            }
        }
    }

    private static Collection<Map.Entry<CNFClause, CNFClause>> selectClauses(
            Collection<CNFClause> clauses, Collection<CNFClause> sOs) {
        Collection<Map.Entry<CNFClause, CNFClause>> clausePairs = new LinkedList<>();
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
        Collection<CNFClause> resolvents = new LinkedList<>();
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
