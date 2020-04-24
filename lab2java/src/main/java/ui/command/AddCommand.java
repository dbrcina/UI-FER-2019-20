package ui.command;

import ui.model.CNFClause;
import ui.model.PLModel;

public class AddCommand extends Command {

    public AddCommand(PLModel model) {
        super(model);
    }

    @Override
    public void actionPerformed(CNFClause clause) {
        model.addClause(clause);
        System.out.println("added " + clause);
    }

}
