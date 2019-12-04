package atm.department.app;

import atm.department.state.AtmState;
import atm.department.state.Memento;

public interface Atm extends Calculable, Memento<AtmState> {

    void withdrawMoney(int sumOfMoney);
}
