package atm.department.app;

import atm.department.state.CassetteState;
import atm.department.state.Memento;

public interface Cassette extends Comparable<Cassette>, Memento<CassetteState> {

    void withdrawNotes(int quantity);

    int calculateActualBankNoteQuantityPerRequestedSumOfMoney(int sumOfMoney);

    Nominal getNominal();

    int getQuantity();

    void setQuantity(int quantity);

    int getAmount();
}
