package atm.emulator.app;

public interface Cassette {

    void withdraw(int quantity);

    int calculateActualBankNoteQuantityPerRequestedSumOfMoney(int sumOfMoney);

    Nominal getNominal();

    int getQuantity();

    void setQuantity(int quantity);

    int getAmount();

    int compareTo(Cassette cassette);

}
