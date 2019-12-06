package atm.department.exceptions;

import atm.department.app.Nominal;

final public class OutOfBanknoteException extends AtmOperationException {

    private static final String MESSAGE_TEMPLATE = "Not enough banknote of nominal %d";

    private static final String GENERAL_MESSAGE_TEMPLATE = "Not enough banknotes to proceed with operation";

    public OutOfBanknoteException(Nominal nominal) {
        super(String.format(MESSAGE_TEMPLATE, nominal.getValue()));
    }

    public OutOfBanknoteException() {
        super(GENERAL_MESSAGE_TEMPLATE);
    }
}
