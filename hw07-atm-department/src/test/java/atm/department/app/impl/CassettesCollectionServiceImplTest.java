package atm.department.app.impl;

import atm.department.app.CassettesService;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.exceptions.OutOfBanknoteException;
import atm.department.model.CassettesCollection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CassettesCollectionServiceImplTest extends BaseTest {


    @Test
    void performWithdrawTheEldestNominalFirst() throws Exception {
        CassettesCollection cassettesCollection = cassettesWithMaxCapacityMock(10);
        int initialSumOfMoney = cassettesCollection.getAmountOfMoney();
        CassettesService service = new CassettesServiceImpl(cassettesCollection);
        service.withdrawMoney(5000);

        assertEquals(9, cassettesCollection.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(initialSumOfMoney - 5000, cassettesCollection.getAmountOfMoney());
    }

    @Test
    void performWithdrawWhenEldestNominalEnded() throws Exception {
        CassettesCollection cassettesCollection = cassettesWithMaxCapacityMock(10);
        cassettesCollection.getCassetteByNominal(Nominal.N5000).setQuantity(0);
        int initialSumOfMoney = cassettesCollection.getAmountOfMoney();
        CassettesService service = new CassettesServiceImpl(cassettesCollection);

        service.withdrawMoney(5250);

        assertEquals(0, cassettesCollection.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(initialSumOfMoney - 5250, cassettesCollection.getAmountOfMoney());
        assertEquals(9, cassettesCollection.getCassetteByNominal(Nominal.N200).getQuantity());
        assertEquals(9, cassettesCollection.getCassetteByNominal(Nominal.N50).getQuantity());
    }

    @Test
    void performWithdrawHappensbyMinimumAvailableBanknotes() throws Exception {
        List<CassetteConfiguration> cassetteConfigurationList = cassettesConfigurationMock(
                new Nominal[]{Nominal.N5000, Nominal.N1000, Nominal.N200},
                new int[]{3, 100, 20}
        );
        CassettesCollection cassettesCollection = new CassettesCollection(cassetteConfigurationList);

        int sumOfMoneyToWithdraw = 30000;

        CassettesService service = new CassettesServiceImpl(cassettesCollection);

        service.withdrawMoney(sumOfMoneyToWithdraw);

        assertEquals(0, cassettesCollection.getCassetteByNominal(Nominal.N5000).getQuantity());
        assertEquals(85, cassettesCollection.getCassetteByNominal(Nominal.N1000).getQuantity());
        assertEquals(20, cassettesCollection.getCassetteByNominal(Nominal.N200).getQuantity());
    }


    @Test
    public void testOutOfBanknoteException() {
        CassettesCollection cassettesCollection = cassettesWithNominalAndMaxCapacityMock(
                new Nominal[]{Nominal.N1000}, new int[]{100});
        CassettesService service = new CassettesServiceImpl(cassettesCollection);

        assertThrows(OutOfBanknoteException.class, () -> service.withdrawMoney(900));
    }


}