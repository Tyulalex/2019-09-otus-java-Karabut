package atm.department.state;

import atm.department.app.Nominal;

import java.util.List;

public interface CassettesState extends State {

    List<CassetteState> getCassettesState();

    default CassetteState getCassetteStateByNominal(Nominal nominal) {
        return this.getCassettesState().stream()
                .filter(cassetteState -> cassetteState.getNominal().equals(nominal))
                .findFirst()
                .orElse(null);
    }
}
