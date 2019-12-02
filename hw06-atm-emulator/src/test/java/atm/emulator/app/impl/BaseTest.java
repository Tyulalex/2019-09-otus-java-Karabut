package atm.emulator.app.impl;

import atm.emulator.configuration.CassetteConfiguration;
import org.mockito.Mockito;

public class BaseTest {

    protected CassetteConfiguration cassetteConfigurationMockWithMaxCapacity(int maxCapacity) {
        var configuration = Mockito.mock(CassetteConfiguration.class);
        Mockito.when(configuration.getMaxNoteCapacity()).thenReturn(maxCapacity);

        return configuration;
    }
}
