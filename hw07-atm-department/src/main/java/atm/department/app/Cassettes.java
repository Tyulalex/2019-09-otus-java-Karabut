package atm.department.app;

import atm.department.state.CassettesState;
import atm.department.state.Memento;

public interface Cassettes extends Calculable, Memento<CassettesState> {

    void withdrawMoney(int requestedSumOfMoney);

    Cassette getCassetteByNominal(Nominal nominal);
}
