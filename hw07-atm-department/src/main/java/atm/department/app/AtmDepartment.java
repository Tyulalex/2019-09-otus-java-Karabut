package atm.department.app;

import atm.department.state.AtmDepartmentState;
import atm.department.state.Memento;

public interface AtmDepartment extends Calculable, Memento<AtmDepartmentState> {
}
