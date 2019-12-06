package atm.department.app;

import atm.department.exceptions.AtmOperationException;

public interface AtmService {

    void withdrawMoney(int sumOfMoney) throws AtmOperationException;
}
