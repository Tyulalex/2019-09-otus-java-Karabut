package atm.department.app.impl;

import atm.department.app.AtmLocationType;
import atm.department.app.AtmType;

public class BuiltInAtm implements AtmLocationType {

    @Override
    public AtmType getType() {
        return AtmType.BUILT_IN;
    }

    @Override
    public boolean canDepositMoney() {
        return false;
    }
}
