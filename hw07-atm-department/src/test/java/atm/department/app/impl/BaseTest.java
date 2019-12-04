package atm.department.app.impl;

import atm.department.app.Cassette;
import atm.department.app.Cassettes;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseTest {

    Cassette cassetteWithNominalAndCapacity(Nominal nominal,
                                            int maxCapacity) {
        var configuration = cassetteConfigurationMockWith(nominal, maxCapacity);

        return new CassetteImpl(configuration);
    }

    Cassettes cassettesWithNominalAndMaxCapacityMock(Nominal[] nominals, int[] capacities) {
        return new CassettesImpl(cassettesConfigurationMock(nominals, capacities));
    }

    Cassettes cassettesWithMaxCapacityMock(int maxCapacity) {
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

    List<CassetteConfiguration> cassettesConfigurationMock(Nominal[] nominals, int[] quantities) {
        List<CassetteConfiguration> cassetteConfigurations = new ArrayList<>();
        for (int i = 0; i < nominals.length; i++) {
            cassetteConfigurations.add(cassetteConfigurationMockWith(nominals[i], quantities[i]));
        }

        return cassetteConfigurations;
    }
}
