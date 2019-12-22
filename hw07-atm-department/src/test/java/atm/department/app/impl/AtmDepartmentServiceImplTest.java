package atm.department.app.impl;

import atm.department.app.Nominal;
import atm.department.exceptions.AtmOperationException;
import atm.department.model.Atm;
import atm.department.model.CassettesCollection;
import atm.department.model.state.AtmState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AtmDepartmentServiceImplTest extends BaseTest {

    @Test
    void getBalance() throws AtmOperationException {
        Atm atm1 = AtmMockWithSumOfMoney(10_000);
        Atm atm2 = AtmMockWithSumOfMoney(50_000);
        var atmDepartment = new AtmDepartmentServiceImpl(List.of(atm1, atm2));
        assertEquals(60_000, atmDepartment.getCurrentBalance());
    }

    @Test
    void getBalanceWhenWithdraw() throws AtmOperationException {
        CassettesCollection cassettesCollection = new CassettesCollection(cassettesConfigurationMock(
                new Nominal[]{Nominal.N50, Nominal.N100}, new int[]{10, 10}
        ));
        Atm atm1 = new Atm(1, 10000, cassettesCollection, new BuiltInAtm());
        Atm atm2 = new Atm(1, 10000, cassettesCollection, new BuiltInAtm());
        var service = new AtmDepartmentServiceImpl(List.of(atm1, atm2));
        assertEquals(atm1.getAmountOfMoney() + atm1.getAmountOfMoney(), service.getCurrentBalance());
        new AtmServiceImpl(atm1).withdrawMoney(50);
        assertEquals(atm1.getAmountOfMoney() + atm1.getAmountOfMoney(), service.getCurrentBalance());
    }

    @Test
    void restoreToInitState() throws AtmOperationException {
        CassettesCollection cassettesCollection1 = new CassettesCollection(cassettesConfigurationMock(
                new Nominal[]{Nominal.N50, Nominal.N100}, new int[]{10, 10}
        ));
        CassettesCollection cassettesCollection2 = new CassettesCollection(cassettesConfigurationMock(
                new Nominal[]{Nominal.N500, Nominal.N1000}, new int[]{20, 30}
        ));
        Atm atm1 = new Atm(1, 10000, cassettesCollection1, new BuiltInAtm());
        Atm atm2 = new Atm(2, 10000, cassettesCollection2, new BuiltInAtm());
        int initialSumOfMoney1 = atm1.getAmountOfMoney();
        int initialSumOfMoney2 = atm2.getAmountOfMoney();

        var service = new AtmDepartmentServiceImpl(List.of(atm1, atm2));
        new AtmServiceImpl(atm1).withdrawMoney(50);
        new AtmServiceImpl(atm2).withdrawMoney(500);
        assertNotEquals(initialSumOfMoney1, atm1.getAmountOfMoney());
        assertNotEquals(initialSumOfMoney2, atm2.getAmountOfMoney());
        service.resetAtmsToInitialState();
        assertEquals(initialSumOfMoney1, atm1.getAmountOfMoney());
        assertEquals(initialSumOfMoney2, atm2.getAmountOfMoney());
    }


    private Atm AtmMockWithSumOfMoney(int sumOfMoney) {
        var atm = Mockito.mock(Atm.class);
        var atmState = Mockito.mock(AtmState.class);
        Mockito.when(atm.getAmountOfMoney()).thenReturn(sumOfMoney);
        Mockito.when(atm.getState()).thenReturn(atmState);
        return atm;
    }
}