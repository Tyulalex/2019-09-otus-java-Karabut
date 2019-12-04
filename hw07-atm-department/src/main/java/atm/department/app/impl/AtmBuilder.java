package atm.department.app.impl;

import atm.department.app.Atm;
import atm.department.app.AtmLocationType;
import atm.department.app.Cassettes;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class AtmBuilder {

    private final int maxSumPerOperation;
    private Cassettes cassettes;
    private AtmLocationType atmType;

    public AtmBuilder(int maxSumPerOperation) {
        this.maxSumPerOperation = maxSumPerOperation;
    }

    public Cassettes getCassettes() {
        return cassettes;
    }

    public int getMaxSumPerOperation() {
        return maxSumPerOperation;
    }

    public AtmLocationType getAtmType() {
        return atmType;
    }

    public AtmBuilder withCassetesOfNominalAndQuantity(EnumMap.Entry<Nominal, Integer>... cassetesOfNominalAndQuantity) {
        List<CassetteConfiguration> cassetteConfigurationList = new ArrayList<>();


        for (EnumMap.Entry<Nominal, Integer> casseteSet : cassetesOfNominalAndQuantity) {
            cassetteConfigurationList.add(new CassetteConfiguration() {
                @Override
                public int getMaxNoteCapacity() {
                    return casseteSet.getValue();
                }

                @Override
                public Nominal getNominal() {
                    return casseteSet.getKey();
                }
            });
        }
        this.cassettes = new CassettesImpl(cassetteConfigurationList);

        return this;
    }

    public AtmBuilder withAtmType(AtmLocationType atmType) {
        this.atmType = atmType;

        return this;
    }

    public Atm build() {
        return new AtmImpl(this);
    }
}
