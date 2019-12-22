package atm.department.configuration;

import atm.department.app.Nominal;

public interface CassetteConfiguration {

    int getMaxNoteCapacity();

    Nominal getNominal();
}
