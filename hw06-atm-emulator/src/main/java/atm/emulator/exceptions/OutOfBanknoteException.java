package atm.emulator.exceptions;

import atm.emulator.app.Nominal;

public class OutOfBanknoteException extends AtmOperationException {

    private static String MESSAGE_TEMPLATE = "Not enough banknote of nominal %d";

    public OutOfBanknoteException(Nominal nominal) {
        super(String.format(MESSAGE_TEMPLATE, nominal.getValue()));
    }

    public OutOfBanknoteException() {
        super("Not enough banknotes to proceed with operation");
    }
}
