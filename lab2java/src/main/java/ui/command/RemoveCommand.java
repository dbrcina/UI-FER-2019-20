package ui.command;

import ui.model.CNFClause;
import ui.model.PLModel;

public class RemoveCommand extends Command {

    public RemoveCommand(PLModel model, boolean testing) {
        super(model, testing);
    }

    @Override
    public void actionPerformed(CNFClause clause) {
        model.removeClause(clause);
        if (!testing) {
            System.out.println("removed " + clause);
        }
    }

}
