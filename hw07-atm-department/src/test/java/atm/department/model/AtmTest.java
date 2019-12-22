package atm.department.model;

import atm.department.app.AtmService;
import atm.department.app.Nominal;
import atm.department.app.impl.AtmServiceImpl;
import atm.department.app.impl.BaseTest;
import atm.department.exceptions.AtmOperationException;
import atm.department.model.state.AtmState;
import atm.department.model.state.Memento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AtmTest extends BaseTest {

    @Test
    public void testGetTotalSumOfMoneyReturnsSameAsCassettesGetTotal() {
        int sumOfMoney = 80000;
        var cassettes = getCassettesMockWithSumOfMoney(sumOfMoney);
        var atm = createAtmWithCassettesMock(cassettes);
        Assertions.assertEquals(sumOfMoney, atm.getAmountOfMoney());
        Mockito.verify(cassettes, Mockito.times(1)).getAmountOfMoney();
    }

    @Test
    public void saveState() {
        var cassettes = cassettesWithNominalAndMaxCapacityMock(
                new Nominal[]{Nominal.N50, Nominal.N100}, new int[]{5, 10});

        Atm atm = createAtmWithCassettesMock(cassettes);
        Memento memento = atm.saveState();
        AtmState atmState = (AtmState) memento.getState();
        assertEquals(10, atmState.getCassetteStatesList().get(0).getQuantity());
        assertEquals(5, atmState.getCassetteStatesList().get(1).getQuantity());
        assertEquals(Nominal.N100, atmState.getCassetteStatesList().get(0).getNominal());
        assertEquals(Nominal.N50, atmState.getCassetteStatesList().get(1).getNominal());
        assertEquals(atm.getIdentifier(), atmState.getIdentifier());
        assertEquals(atm.getMaxSumPerOperation(), atmState.getMaxSumPerOperation());
    }

    @Test
    public void restoreState() throws AtmOperationException {
        var cassettes = cassettesWithNominalAndMaxCapacityMock(
                new Nominal[]{Nominal.N50, Nominal.N100},
                new int[]{5, 10});
        Atm atm = createAtmWithCassettesMock(cassettes);
        Memento memento = atm.saveState();
        int sumOfMoney = atm.getAmountOfMoney();
        AtmService atmService = new AtmServiceImpl(atm);
        atmService.withdrawMoney(1050);

        assertNotEquals(sumOfMoney, atm.getAmountOfMoney());
        assertEquals(0, cassettes.getCassetteByNominal(Nominal.N100).getQuantity());
        assertEquals(4, cassettes.getCassetteByNominal(Nominal.N50).getQuantity());

        atm.restoreState(memento);

        assertEquals(sumOfMoney, atm.getAmountOfMoney());
        assertEquals(10, cassettes.getCassetteByNominal(Nominal.N100).getQuantity());
        assertEquals(5, cassettes.getCassetteByNominal(Nominal.N50).getQuantity());
    }
}