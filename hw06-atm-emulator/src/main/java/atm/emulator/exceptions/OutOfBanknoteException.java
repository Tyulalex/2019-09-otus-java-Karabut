package atm.emulator.exceptions;

import atm.emulator.app.Nominal;

final public class OutOfBanknoteException extends AtmOperationException {

    private static final String MESSAGE_TEMPLATE = "Not enough banknote of nominal %d";

    public OutOfBanknoteException(Nominal nominal) {
        super(String.format(MESSAGE_TEMPLATE, nominal.getValue()));
    }

    public OutOfBanknoteException() {
        super("Not enough banknotes to proceed with operation");
    }
}
