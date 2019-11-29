package atm.emulator.app;

import atm.emulator.exceptions.ExceedMaxSumPerOperationException;
import atm.emulator.exceptions.OutOfBanknoteException;
import atm.emulator.exceptions.OutOfMoneyException;
import atm.emulator.exceptions.UnsupportedAmountRequestedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmTest {

    private static final int MAX_SUM_PER_OPERATION = 200000;
    private static final int MAX_CASSETTE_CAPACITY = 10;
    private Atm atm;
    private Cassettes cassettes;

    @BeforeEach
    public void setUp() {
        this.cassettes = new Cassettes(() -> MAX_CASSETTE_CAPACITY);
        this.atm = new Atm(() -> MAX_SUM_PER_OPERATION, this.cassettes);
    }

    @Test
    public void testWithdrawSumOfOneOldestNominal() {
        int initialSumOfMoney = this.atm.getTotalSumOfMoney();
        this.atm.withdrawMoney(5000);
        assertEquals(initialSumOfMoney - 5000, this.atm.getTotalSumOfMoney());
        assertEquals(9, this.cassettes.getCassetteByNominal(Nominal.N5000).getQuantity());
    }

    @Test
    public void testWithdrawSumOfSeveralNominals() {
        int initialSumOfMoney = this.atm.getTotalSumOfMoney();
        this.atm.withdrawMoney(7950);
        assertEquals(initialSumOfMoney - 7950, this.atm.getTotalSumOfMoney());
        Map<Nominal, Integer> map = Map.of(
                Nominal.N5000, 9,
                Nominal.N2000, 9,
                Nominal.N1000, 10,
                Nominal.N500, 9,
                Nominal.N200, 8,
                Nominal.N100, 10,
                Nominal.N50, 9
        );
        thenCheckQuantitiesInCassetteEqualsTo(map);
    }

    @Test
    public void testWithdrawWhenOldestNominalEnded() {
        this.cassettes.getCassetteByNominal(Nominal.N5000).withdraw(9);
        int initialSumOfMoney = this.atm.getTotalSumOfMoney();
        this.atm.withdrawMoney(11000);
        assertEquals(initialSumOfMoney - 11000, this.atm.getTotalSumOfMoney());
        Map<Nominal, Integer> map = Map.of(
                Nominal.N5000, 0,
                Nominal.N2000, 7,
                Nominal.N1000, 10,
                Nominal.N500, 10,
                Nominal.N200, 10,
                Nominal.N100, 10,
                Nominal.N50, 10
        );
        thenCheckQuantitiesInCassetteEqualsTo(map);
    }

    @Test
    public void testWithdrawWhenNotEnoughMoney() {
        int initialSumOfMoney = this.atm.getTotalSumOfMoney();
        assertThrows(ExceedMaxSumPerOperationException.class,
                () -> this.atm.withdrawMoney(MAX_SUM_PER_OPERATION + 100));
        assertEquals(initialSumOfMoney, this.atm.getTotalSumOfMoney());
    }

    @Test
    public void testWithdrawWhenIncorrectSum() {
        int initialSumOfMoney = this.atm.getTotalSumOfMoney();
        assertThrows(UnsupportedAmountRequestedException.class,
                () -> this.atm.withdrawMoney(2345));
        assertEquals(initialSumOfMoney, this.atm.getTotalSumOfMoney());
    }

    @Test
    public void testOutOfMoneyException() {
        int initialSumOfMoney = this.atm.getTotalSumOfMoney();
        this.atm.withdrawMoney(initialSumOfMoney);
        assertThrows(OutOfMoneyException.class, () -> this.atm.withdrawMoney(100));
    }

    @Test
    public void testOutOfBanknoteException() {
        this.cassettes.getCassetteByNominal(Nominal.N5000).withdraw(MAX_CASSETTE_CAPACITY);
        this.cassettes.getCassetteByNominal(Nominal.N2000).withdraw(MAX_CASSETTE_CAPACITY);
        this.cassettes.getCassetteByNominal(Nominal.N1000).withdraw(MAX_CASSETTE_CAPACITY - 1);
        this.cassettes.getCassetteByNominal(Nominal.N500).withdraw(MAX_CASSETTE_CAPACITY);
        this.cassettes.getCassetteByNominal(Nominal.N200).withdraw(MAX_CASSETTE_CAPACITY);
        this.cassettes.getCassetteByNominal(Nominal.N100).withdraw(MAX_CASSETTE_CAPACITY);
        this.cassettes.getCassetteByNominal(Nominal.N50).withdraw(MAX_CASSETTE_CAPACITY);

        assertThrows(OutOfBanknoteException.class, () -> this.atm.withdrawMoney(100));
        assertEquals(1000, this.atm.getTotalSumOfMoney());
    }

    private void thenCheckQuantitiesInCassetteEqualsTo(Map<Nominal, Integer> expectedQuantities) {
        for (Map.Entry<Nominal, Integer> entry : expectedQuantities.entrySet()) {
            Integer actualAmount = this.cassettes.getCassetteByNominal(entry.getKey()).getQuantity();
            assertEquals(entry.getValue(),
                    actualAmount,
                    String.format(
                            "EXPECTED %d of Nominal %S, ACTUAL: %d", entry.getValue(), entry.getKey(), actualAmount));
        }
    }


}