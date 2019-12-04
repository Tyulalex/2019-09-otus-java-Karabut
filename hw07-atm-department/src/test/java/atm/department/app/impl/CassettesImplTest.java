package atm.department.app.impl;

import atm.department.app.Cassette;
import atm.department.app.Cassettes;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.state.CassettesState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CassettesImplTest extends BaseTest {
    @Test
    void getTotalSumOfMoney() {

        List<CassetteConfiguration> cassetteConfigurationList = cassettesConfigurationMock(
                new Nominal[]{Nominal.N50, Nominal.N100},
                new int[]{5, 10}
        );
        Cassettes cassettes = new CassettesImpl(cassetteConfigurationList);

        assertEquals(1250, cassettes.getTotalSumOfMoney());
    }

    @Test
    void performWithdrawTheEldestNominalFirst() {
        Cassettes cassettes = cassettesWithMaxCapacityMock(10);
        int initialSumOfMoney = cassettes.getTotalSumOfMoney();

        cassettes.withdrawMoney(5000);

        assertEquals(9, cassettes.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(initialSumOfMoney - 5000, cassettes.getTotalSumOfMoney());
    }

    @Test
    void performWithdrawWhenEldestNominalEnded() {
        Cassettes cassettes = cassettesWithMaxCapacityMock(10);
        cassettes.getCassetteByNominal(Nominal.N5000).setQuantity(0);
        int initialSumOfMoney = cassettes.getTotalSumOfMoney();

        cassettes.withdrawMoney(5250);

        assertEquals(0, cassettes.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(initialSumOfMoney - 5250, cassettes.getTotalSumOfMoney());
        assertEquals(9, cassettes.getCassetteByNominal(Nominal.N200).getQuantity());
        assertEquals(9, cassettes.getCassetteByNominal(Nominal.N50).getQuantity());
    }

    @Test
    void performWithdrawHappensbyMinimumAvailableBanknotes() {
        List<CassetteConfiguration> cassetteConfigurationList = cassettesConfigurationMock(
                new Nominal[]{Nominal.N5000, Nominal.N1000, Nominal.N200},
                new int[]{3, 100, 20}
        );
        Cassettes cassettes = new CassettesImpl(cassetteConfigurationList);

        int sumOfMoneyToWithdraw = 30000;

        cassettes.withdrawMoney(sumOfMoneyToWithdraw);

        assertEquals(0, cassettes.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(85, cassettes.getCassetteByNominal(Nominal.N1000).getQuantity());
        assertEquals(20, cassettes.getCassetteByNominal(Nominal.N200).getQuantity());
    }

    @Test
    void getCassetteByNominal() {
        Cassettes cassettes = cassettesWithMaxCapacityMock(10);
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N100, 10);

        assertTrue(cassette.equals(cassettes.getCassetteByNominal(Nominal.N100)));
    }

    @Test
    public void testOutOfBanknoteException() {
        Cassettes cassettes = cassettesWithNominalAndMaxCapacityMock(
                new Nominal[]{Nominal.N1000}, new int[]{100});

        assertThrows(OutOfBanknoteException.class, () -> cassettes.withdrawMoney(900));
    }

    @Test
    public void cassettesGetState() {
        Cassettes cassettes = cassettesWithNominalAndMaxCapacityMock(
                new Nominal[]{Nominal.N1000, Nominal.N50}, new int[]{100, 50});
        CassettesState cassettesState = cassettes.getState();

        assertEquals(100, cassettesState.getCassetteStateByNominal(Nominal.N1000).getQuantity());
        assertEquals(50, cassettesState.getCassetteStateByNominal(Nominal.N50).getQuantity());
        assertEquals(2, cassettesState.getCassettesState().size());
    }

    @Test
    public void restoreState() {
        Cassettes cassettes = cassettesWithNominalAndMaxCapacityMock(
                new Nominal[]{Nominal.N1000, Nominal.N50}, new int[]{100, 50});
        CassettesState cassettesState = cassettes.getState();

        cassettes.getCassetteByNominal(Nominal.N1000).withdrawNotes(10);
        cassettes.getCassetteByNominal(Nominal.N50).withdrawNotes(50);

        cassettes.restoreState(cassettesState);
        assertEquals(100, cassettesState.getCassetteStateByNominal(Nominal.N1000).getQuantity());
        assertEquals(50, cassettesState.getCassetteStateByNominal(Nominal.N50).getQuantity());
        assertEquals(2, cassettesState.getCassettesState().size());
    }
}