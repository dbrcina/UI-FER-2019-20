package ui.command;

import ui.model.CNFClause;
import ui.model.PLModel;

public class RemoveCommand extends Command {

    public RemoveCommand(PLModel model) {
        super(model);
    }

    @Override
    public void actionPerformed(CNFClause clause) {
        model.removeClause(clause);
        System.out.println("removed " + clause);
    }

}
