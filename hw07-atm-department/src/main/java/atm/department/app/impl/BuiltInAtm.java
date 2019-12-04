package atm.department.app.impl;

import atm.department.app.AtmLocationType;
import atm.department.app.AtmType;

public class BuiltInAtm implements AtmLocationType {

    public static final AtmType ATM_TYPE = AtmType.BUILT_IN;

    @Override
    public boolean canDepositMoney() {
        return false;
    }
}
