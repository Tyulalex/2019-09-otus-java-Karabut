package atm.department.model.builder;

import atm.department.app.AtmLocationType;
import atm.department.app.Nominal;
import atm.department.model.CassettesCollection;
import lombok.Getter;

import java.util.EnumMap;

@Getter
public abstract class AbstractAtmBuilder {

    protected CassettesCollection cassettesCollection;
    protected AtmLocationType atmLocationType;
    protected int maxSumPerOperation;
    protected int identifier;

    public abstract AbstractAtmBuilder withCassettesCollection(EnumMap.Entry<Nominal, Integer>... cassettesConfig);

    public abstract AbstractAtmBuilder withMaxSumPerOperation(int maxSumPerOperation);

    public abstract AbstractAtmBuilder withAtmType(AtmLocationType atmType);
}
