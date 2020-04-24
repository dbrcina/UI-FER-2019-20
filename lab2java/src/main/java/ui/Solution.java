package ui;

import ui.command.AddCommand;
import ui.command.Command;
import ui.command.QueryCommand;
import ui.command.RemoveCommand;
import ui.model.CNFClause;
import ui.model.Literal;
import ui.model.PLModel;
import ui.util.FileParser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String[] args) throws IOException {
        if (args.length < 2 || args.length > 4) {
            return;
        }
        String task = args[0].toLowerCase();
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

        switch (task) {
            case "resolution":
                resolution(model, verbose);
                break;
            case "cooking_test":
                cookingTest(model, commandsFile, verbose);
                break;
            case "cooking_interactive":
                cookingInteractive(model, verbose);
                break;
        }
    }

    private static void resolution(PLModel model, boolean verbose) {
        StringBuilder sb = new StringBuilder();
        boolean result = ResolutionUtils.plResolution(model, sb, verbose);
        if (!result) sb.setLength(0);
        sb.append(model.getGoalClause()).append(" is ").append(result ? "true" : "unknown");
        System.out.println(sb.toString());
    }

    private static void cookingTest(
            PLModel model, Path commandsFile, boolean verbose) throws IOException {
        Collection<Entry<CNFClause, Command>> cnfCommands = FileParser.parseCommandsFile(
                commandsFile, model, verbose);
        cnfCommands.forEach(pair -> {
            CNFClause clause = pair.getKey();
            Command command = pair.getValue();
            command.actionPerformed(clause);
        });
    }

    private static void cookingInteractive(PLModel model, boolean verbose) throws IOException {
        CNFClause c = model.getGoalClause();
        model.addClause(model.getGoalClause());
        int index = c.getIndex() + 1;
        System.out.println("Testing cooking assistant with standard resolution");
        System.out.println("Constructed with knowledge:");
        model.getClauses().forEach(cl -> System.out.println("> " + cl));
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n>>> Please enter your query");
                System.out.print(">>> ");
                String line = sc.nextLine().trim().toLowerCase();
                if (line.isEmpty()) continue;
                if (line.equals("exit")) break;
                int lastIndexOfSpace = line.lastIndexOf(' ');
                String comm = line.substring(lastIndexOfSpace + 1);
                CNFClause clause = new CNFClause(
                        Arrays.stream(line.substring(0, lastIndexOfSpace).split(" v "))
                                .map(Literal::new)
                                .collect(Collectors.toList()), index++);
                Command command = null;
                if (comm.equals("+")) command = new AddCommand(model);
                if (comm.equals("-")) command = new RemoveCommand(model);
                if (comm.equals("?")) command = new QueryCommand(model, verbose);
                command.actionPerformed(clause);
            }
        }
    }

}
