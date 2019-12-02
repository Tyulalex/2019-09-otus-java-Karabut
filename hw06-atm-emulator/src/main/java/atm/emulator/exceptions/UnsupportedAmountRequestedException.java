package atm.emulator.exceptions;

final public class UnsupportedAmountRequestedException extends AtmOperationException {

    private static final String MESSAGE_TEMPLATE = "Entered amount is incorrect, it should be multiple to %d";

    public UnsupportedAmountRequestedException(int multiplicity) {
        this(String.format(MESSAGE_TEMPLATE, multiplicity));
    }

    public UnsupportedAmountRequestedException(String message) {
        super(message);
    }
}
