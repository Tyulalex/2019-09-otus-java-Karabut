package atm.department.app;

import atm.department.exceptions.AtmOperationException;

public interface CassettesService {

    void withdrawMoney(int requestedSumOfMoney) throws AtmOperationException;
}
