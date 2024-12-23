package seedu.pill.command;

import seedu.pill.exceptions.PillException;
import seedu.pill.util.ItemMap;
import seedu.pill.util.Storage;
import seedu.pill.util.TransactionManager;

public class TransactionsCommand extends Command{
    private TransactionManager transactionManager;

    public TransactionsCommand(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void execute(ItemMap itemMap, Storage storage) throws PillException {
        transactionManager.listTransactions();
    }
}
