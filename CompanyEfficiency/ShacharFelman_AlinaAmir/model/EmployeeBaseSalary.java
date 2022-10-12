package model;

import model.CompanyModel.eEmployeesTypes;

public class EmployeeBaseSalary extends Employee {

	public final static int BASE_WORKING_HOURS_IN_MONTH = 160;
	
	public EmployeeBaseSalary(String name, WorkHours workHoursPreferred, int salaryPerHour) {
		super(name, eEmployeesTypes.BaseSalary, workHoursPreferred, salaryPerHour);
		this.currMonthWorkHoursCnt = BASE_WORKING_HOURS_IN_MONTH;
	}
	
	protected EmployeeBaseSalary(String name, eEmployeesTypes empType, WorkHours workHoursPreferred, int salaryPerHour) {
		super(name, empType == null ? eEmployeesTypes.BaseSalary : empType, workHoursPreferred, salaryPerHour);
		this.currMonthWorkHoursCnt = BASE_WORKING_HOURS_IN_MONTH;
	}

}
