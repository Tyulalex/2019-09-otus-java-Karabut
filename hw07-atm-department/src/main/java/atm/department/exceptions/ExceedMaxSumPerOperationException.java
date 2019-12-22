package atm.department.exceptions;

final public class ExceedMaxSumPerOperationException extends AtmOperationException {

    private static final String MESSAGE_TEMPLATE = "Requested amount shall not exceed limit in %d per one operation";

    public ExceedMaxSumPerOperationException(int maxSumPerOperation) {
        super(String.format(MESSAGE_TEMPLATE, maxSumPerOperation));
    }
}
