package atm.department.model.builder;

import atm.department.app.AtmLocationType;
import atm.department.app.Nominal;
import atm.department.configuration.CassetteConfiguration;
import atm.department.configuration.CassetteConfigurationFactory;
import atm.department.model.Atm;
import atm.department.model.CassettesCollection;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AtmBuilder extends AbstractAtmBuilder {

    public AtmBuilder(int identifier) {
        this.identifier = identifier;
    }

    @SafeVarargs
    @Override
    public final AtmBuilder withCassettesCollection(Map.Entry<Nominal, Integer>... cassettesConfig) {
        List<CassetteConfiguration> cassetteConfigurationList = new ArrayList<>();
        for (EnumMap.Entry<Nominal, Integer> casseteSet : cassettesConfig) {
            cassetteConfigurationList.add(
                    CassetteConfigurationFactory.getCassetteConfiguration(casseteSet.getKey(), casseteSet.getValue()));
        }
        this.cassettesCollection = new CassettesCollection(cassetteConfigurationList);

        return this;
    }

    @Override
    public AtmBuilder withMaxSumPerOperation(int maxSumPerOperation) {
        this.maxSumPerOperation = maxSumPerOperation;

        return this;
    }

    @Override
    public AtmBuilder withAtmType(AtmLocationType atmType) {
        this.atmLocationType = atmType;

        return this;
    }

    public Atm build() {
        return new Atm(this.identifier, this.maxSumPerOperation, this.cassettesCollection, this.atmLocationType);
    }
}
