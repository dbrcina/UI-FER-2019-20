package ui.command;

import ui.model.CNFClause;
import ui.model.PLModel;

public abstract class Command {

    protected final PLModel model;

    protected Command(PLModel model) {
        this.model = model;
    }

    public abstract void actionPerformed(CNFClause clause);

}
