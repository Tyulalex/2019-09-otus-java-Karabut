package atm.department.app.impl;

import atm.department.app.AtmLocationType;
import atm.department.app.AtmType;

public class FreeStandingAtm implements AtmLocationType {

    public static final AtmType ATM_TYPE = AtmType.FREESTANDING;

    @Override
    public boolean canDepositMoney() {
        return true;
    }
}
