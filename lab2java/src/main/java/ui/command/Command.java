package ui.command;

import ui.model.CNFClause;
import ui.model.PLModel;

public abstract class Command {

    protected final PLModel model;
    protected final boolean testing;

    protected Command(PLModel model, boolean testing) {
        this.model = model;
        this.testing = testing;
    }

    public abstract void actionPerformed(CNFClause clause);

}
