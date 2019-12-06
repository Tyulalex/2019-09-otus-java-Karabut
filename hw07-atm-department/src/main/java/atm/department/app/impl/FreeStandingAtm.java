package atm.department.app.impl;

import atm.department.app.AtmLocationType;
import atm.department.app.AtmType;

public class FreeStandingAtm implements AtmLocationType {

    @Override
    public AtmType getType() {
        return AtmType.FREESTANDING;
    }

    @Override
    public boolean canDepositMoney() {
        return true;
    }
}
