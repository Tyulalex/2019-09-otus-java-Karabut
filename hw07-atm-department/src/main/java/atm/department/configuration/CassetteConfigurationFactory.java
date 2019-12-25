package atm.department.configuration;

import atm.department.app.Nominal;

public class CassetteConfigurationFactory {

    public static CassetteConfiguration getCassetteConfiguration(Nominal nominal, int quantity) {

        return new CassetteConfiguration() {
            @Override
            public int getMaxNoteCapacity() {
                return quantity;
            }

            @Override
            public Nominal getNominal() {
                return nominal;
            }
        };
    }
}
