package atm.department.model;

import atm.department.app.Nominal;
import atm.department.app.impl.BaseTest;
import atm.department.configuration.CassetteConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CassettesCollectionTest extends BaseTest {

    @Test
    void getTotalSumOfMoney() {

        List<CassetteConfiguration> cassetteConfigurationList = cassettesConfigurationMock(
                new Nominal[]{Nominal.N50, Nominal.N100},
                new int[]{5, 10}
        );
        CassettesCollection cassettesCollection = new CassettesCollection(cassetteConfigurationList);

        assertEquals(1250, cassettesCollection.getAmountOfMoney());
    }

    @Test
    void getCassetteByNominal() {
        CassettesCollection cassettesCollection = cassettesWithMaxCapacityMock(10);
        Cassette cassette = cassetteWithNominalAndCapacity(Nominal.N100, 10);

        assertTrue(cassette.equals(cassettesCollection.getCassetteByNominal(Nominal.N100)));
    }
}