package atm.department.model.state;

import atm.department.app.Nominal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CassetteState {

    int quantity;
    Nominal nominal;
}
