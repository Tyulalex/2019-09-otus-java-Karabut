package atm.department.configuration;

import atm.department.app.AtmType;

public interface AtmConfiguration {

    int getMaxSumPerOperation();

    AtmType getAtmType();
}
