package ui;

import ui.model.CNFClause;
import ui.model.Literal;
import ui.model.PLModel;
import ui.util.FileParser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String[] args) throws IOException {
        if (args.length < 2 || args.length > 4) {
            return;
        }
        String task = args[0].toLowerCase();
        if (!task.equals("resolution")) return;
        Path clausesFile = Paths.get(args[1]);
        Path commandsFile = null;
        boolean verbose = false;
        if (args.length == 4) {
            commandsFile = Paths.get(args[2]);
            verbose = true;
        } else if (args.length == 3) {
            if (args[2].equals("verbose")) verbose = true;
            else commandsFile = Paths.get(args[2]);
        }
        PLModel model = FileParser.parseClausesFile(clausesFile);

        StringBuilder sb = new StringBuilder();
        boolean result = plResolution(model, sb, verbose);
        sb.append(model.getGoalClause()).append(" is ").append(result ? "true" : "unknown");
        System.out.println(sb.toString());
    }

    private static boolean plResolution(PLModel model, StringBuilder sb, boolean verbose) {
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
        while (!allClauses.isEmpty()) {
            Collection<CNFClause> newKnowledge = new LinkedList<>();
            for (Entry<CNFClause, CNFClause> clausePair : selectClauses(allClauses, sOs)) {
                CNFClause c1 = clausePair.getKey();
                CNFClause c2 = clausePair.getValue();
                Collection<CNFClause> resolvents = plResolve(c1, c2, index);
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
                newKnowledge.addAll(resolvents);
            }
            sOs.addAll(newKnowledge);
        }
        if (verbose) sb.append("===========").append("\n");
        return false;
    }

    private static Collection<Entry<CNFClause, CNFClause>> selectClauses(
            Collection<CNFClause> clauses, Collection<CNFClause> sOs) {
        Collection<Entry<CNFClause, CNFClause>> clausePairs = new LinkedList<>();
        Iterator<CNFClause> itClauses = clauses.iterator();
        while (itClauses.hasNext()) {
            CNFClause clause = itClauses.next();
            if (clause.isTautology()) { // remove if tautology
                itClauses.remove();
                continue;
            }
            boolean found = false;
            Iterator<CNFClause> itSoS = sOs.iterator();
            while (itSoS.hasNext()) {
                CNFClause sOsClause = itSoS.next();
                if (clause.isSubsumed(sOsClause)) { // remove if subsumed
                    itClauses.remove();
                    break;
                }
                if (sOsClause.isSubsumed(clause)) { // remove if subsumed
                    itClauses.remove();
                    itSoS.remove();
                    break;
                }
                for (Literal l : sOsClause.getLiterals()) {
                    if (clause.containsLiteral(l.nNegate())) {
                        clausePairs.add(new AbstractMap.SimpleEntry<>(clause, sOsClause));
                        found = true;
                        itClauses.remove();
                        itSoS.remove();
                        break;
                    }
                }
                if (found) break;
            }
        }
        return clausePairs;
    }

    private static Collection<CNFClause> plResolve(CNFClause c1, CNFClause c2, int index) {
        Collection<CNFClause> resolvents = new LinkedList<>();
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
        if (literalsToDelete.isEmpty()) {
            literals1.addAll(literals2);
            resolvents.add(new CNFClause(literals1, index));
        } else {
            for (Literal l : literalsToDelete) {
                literals1.remove(l);
                literals1.addAll(literals2.stream().filter(li -> !li.equals(l.nNegate())).collect(Collectors.toList()));
                if (!literals1.isEmpty()) resolvents.add(new CNFClause(literals1, index++));
            }
        }
        return resolvents;
    }

}
