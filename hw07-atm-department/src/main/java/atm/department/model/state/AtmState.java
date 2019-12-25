package atm.department.model.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AtmState {

    private final int maxSumPerOperation;
    private final List<CassetteState> cassetteStatesList;
    private final int identifier;
}
