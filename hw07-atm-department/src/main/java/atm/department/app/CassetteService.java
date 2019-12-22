package atm.department.app;

import atm.department.exceptions.OutOfBanknoteException;

public interface CassetteService {

    void withdrawNotes(int quantity) throws OutOfBanknoteException;

    int calculateActualBankNoteQuantityPerRequestedSumOfMoney(int sumOfMoney);
}
