package atm.department.app;

import atm.department.exceptions.AtmOperationException;

public interface CassettesCollectionService {

    void withdrawMoney(int requestedSumOfMoney) throws AtmOperationException;
}
