package atm.department.exceptions;

final public class OutOfMoneyException extends AtmOperationException {

    private static final String MESSAGE_TEMPLATE = "Not enough money to proceed with operation";

    public OutOfMoneyException() {
        super(MESSAGE_TEMPLATE);
    }
}
