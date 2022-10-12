package model;

import model.CompanyModel.eEmployeesTypes;

public class EmployeeHourSalary extends Employee {

	public EmployeeHourSalary(String name, WorkHours workHoursPreferred, int salaryPerHour) {
		super(name, eEmployeesTypes.SalaryPerHour, workHoursPreferred, salaryPerHour);
	}

	public int getWorkHoursCount() {
		return this.currMonthWorkHoursCnt;
	}
	
	public void setWorkHoursCount(int workHoursCount) {
		this.currMonthWorkHoursCnt = workHoursCount;
	}
	
	public void addWorkHoursCount(int workHoursToAdd) {
		this.currMonthWorkHoursCnt += workHoursToAdd;
	}
	
	public void resetWorkHoursCount() {
		this.currMonthWorkHoursCnt = 0;
	}
	
}