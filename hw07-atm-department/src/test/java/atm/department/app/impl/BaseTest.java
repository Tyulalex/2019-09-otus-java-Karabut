package atm.department.app.impl;

import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.model.Atm;
import atm.department.model.Cassette;
import atm.department.model.CassettesCollection;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseTest {


    protected static final int MAX_NOTE_CAPACITY = 100;
    protected static final Nominal DEFAULT_NOMINAL = Nominal.N5000;
    protected static final int MAX_SUM_PER_OPERATION = 200000;

    protected Cassette cassetteWithNominalAndCapacity(Nominal nominal,
                                                      int maxCapacity) {
        var configuration = cassetteConfigurationMockWith(nominal, maxCapacity);

        return new Cassette(configuration);
    }

    protected CassettesCollection cassettesWithNominalAndMaxCapacityMock(Nominal[] nominals, int[] capacities) {
        return new CassettesCollection(cassettesConfigurationMock(nominals, capacities));
    }

    protected CassettesCollection cassettesWithMaxCapacityMock(int maxCapacity) {
        int[] quantities = new int[Nominal.values().length];
        Arrays.fill(quantities, maxCapacity);

        return cassettesWithNominalAndMaxCapacityMock(Nominal.values(), quantities);
    }

    CassetteConfiguration cassetteConfigurationMockWith(Nominal nominal, int maxCapacity) {
        var configuration = Mockito.mock(CassetteConfiguration.class);
        Mockito.when(configuration.getMaxNoteCapacity()).thenReturn(maxCapacity);
        Mockito.when(configuration.getNominal()).thenReturn(nominal);

        return configuration;
    }

    protected List<CassetteConfiguration> cassettesConfigurationMock(Nominal[] nominals, int[] quantities) {
        List<CassetteConfiguration> cassetteConfigurations = new ArrayList<>();
        for (int i = 0; i < nominals.length; i++) {
            cassetteConfigurations.add(cassetteConfigurationMockWith(nominals[i], quantities[i]));
        }

        return cassetteConfigurations;
    }

    protected Cassette cassetteWithDefaultNominalAndCapacity(int maxCapacity) {
        return cassetteWithNominalAndCapacity(DEFAULT_NOMINAL, maxCapacity);
    }

    protected Cassette cassetteWithNominalAndDefaultCapacity(Nominal nominal) {
        return cassetteWithNominalAndCapacity(nominal, MAX_NOTE_CAPACITY);
    }

    protected Atm createAtmWithCassettesMock(CassettesCollection cassettesCollection) {
        CassettesCollection cassettess = cassettesCollection == null ? getCassettesDefaultMock() : cassettesCollection;
        return new Atm(1, MAX_SUM_PER_OPERATION, cassettess, new BuiltInAtm());
    }

    protected CassettesCollection getCassettesDefaultMock() {
        return getCassettesMockWithSumOfMoney(10000);
    }

    protected CassettesCollection getCassettesMockWithSumOfMoney(int sumOfMoney) {
        var cassettes = Mockito.mock(CassettesCollection.class);
        Mockito.when(cassettes.getAmountOfMoney()).thenReturn(sumOfMoney);
        Mockito.when(cassettes.getCassettesListState()).thenReturn(new ArrayList<>());

        return cassettes;
    }
}
