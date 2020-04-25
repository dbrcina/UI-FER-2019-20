package ui.command;

import ui.model.CNFClause;
import ui.model.PLModel;

public class AddCommand extends Command {

    public AddCommand(PLModel model, boolean testing) {
        super(model, testing);
    }

    @Override
    public void actionPerformed(CNFClause clause) {
        model.addClause(clause);
        if (!testing) {
            System.out.println("added " + clause);
        }
    }

}
