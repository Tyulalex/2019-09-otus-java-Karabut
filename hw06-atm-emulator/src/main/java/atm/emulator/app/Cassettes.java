package atm.emulator.app;

public interface Cassettes<T extends Cassette> {

    int getTotalSumOfMoney();

    void performWithdraw(int requestedSumOfMoney);

    Cassette getCassetteByNominal(Nominal nominal);
}
