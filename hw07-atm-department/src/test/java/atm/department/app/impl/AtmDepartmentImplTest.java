package atm.department.app.impl;

import atm.department.app.Atm;
import atm.department.state.AtmDepartmentState;
import atm.department.state.AtmState;
import atm.department.state.CassettesState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AtmDepartmentImplTest {

    @Test
    void getTotalSumOfMoney() {
        var atmDepartment = new AtmDepartmentImpl(
                List.of(AtmMockWithSumOfMoney(10_000), AtmMockWithSumOfMoney(50_000)));
        assertEquals(60_000, atmDepartment.getTotalSumOfMoney());
    }

    @Test
    void getState() {
        List<Atm> atms = List.of(AtmMockWithSumOfMoney(10_000), AtmMockWithSumOfMoney(50_000));
        var atmDepartment = new AtmDepartmentImpl(atms);
        List<AtmState> atmStates = List.of(atms.get(0).getState(), atms.get(1).getState());
        assertEquals(atmStates, atmDepartment.getState().getAtmsState());
    }

    @Test
    void restoreState() {
        var atm1 = AtmMockWithSumOfMoney(10_000);
        var atm2 = AtmMockWithSumOfMoney(50_000);
        var atm1State = atm1.getState();
        var atm2State = atm2.getState();
        var atmDepartment = new AtmDepartmentImpl(List.of(atm1, atm2));
        AtmDepartmentState state = new AtmDepartmentState() {
            @Override
            public List<AtmState> getAtmsState() {
                return List.of(atm1State, atm2State);
            }
        };

        atmDepartment.restoreState(state);

        Mockito.verify(atm1, Mockito.times(1)).restoreState(atm1State);
        Mockito.verify(atm2, Mockito.times(1)).restoreState(atm2State);
    }

    @Test
    void restoreToInitState() {
        var atm1 = AtmMockWithSumOfMoney(10_000);
        var atm2 = AtmMockWithSumOfMoney(50_000);
        var atm1State = atm1.getState();
        var atm2State = atm2.getState();
        var atmDepartment = new AtmDepartmentImpl(List.of(atm1, atm2));
        AtmDepartmentState state = new AtmDepartmentState() {
            @Override
            public List<AtmState> getAtmsState() {
                return List.of(atm1State, atm2State);
            }
        };

        atmDepartment.restoreToInitState();

        Mockito.verify(atm1, Mockito.times(1)).restoreState(atm1State);
        Mockito.verify(atm2, Mockito.times(1)).restoreState(atm2State);
    }

    private Atm AtmMockWithSumOfMoney(int sumOfMoney) {
        var atm = Mockito.mock(AtmImpl.class);
        var cassetesState = Mockito.mock(CassettesState.class);
        var atmState = Mockito.mock(AtmState.class);
        Mockito.when(atmState.getCassettesState()).thenReturn(cassetesState);
        Mockito.when(atm.getTotalSumOfMoney()).thenReturn(sumOfMoney);
        Mockito.when(atm.getState()).thenReturn(atmState);
        return atm;
    }
}