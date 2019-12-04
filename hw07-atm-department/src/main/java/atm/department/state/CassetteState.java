package atm.department.state;

import atm.department.app.Nominal;

public interface CassetteState extends State {

    int getQuantity();

    Nominal getNominal();

}
