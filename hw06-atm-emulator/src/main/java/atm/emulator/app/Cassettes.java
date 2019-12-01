package atm.emulator.app;

public interface Cassettes {

    int getTotalSumOfMoney();

    void performWithdraw(int requestedSumOfMoney);

    Cassette getCassetteByNominal(Nominal nominal);
}
