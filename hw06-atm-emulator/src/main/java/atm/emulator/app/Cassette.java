package atm.emulator.app;

public interface Cassette extends Comparable<Cassette> {

    void withdraw(int quantity);

    int calculateActualBankNoteQuantityPerRequestedSumOfMoney(int sumOfMoney);

    Nominal getNominal();

    int getQuantity();

    void setQuantity(int quantity);

    int getAmount();

}
