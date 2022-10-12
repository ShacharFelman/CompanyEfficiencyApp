package model;

import model.CompanyModel.*;

public abstract class Employee extends CompanyEntity  {

	protected eEmployeesTypes empType;
	protected WorkHours workHoursPreferred;
	protected int salaryPerHour;
	protected int currMonthWorkHoursCnt;
	protected Role empRole;
	public static int empSerialNum = 0;

	
	public Employee(String name, eEmployeesTypes empType, WorkHours workHoursPreferred, int salaryPerHour) {
		this.name = name;
		this.empType = empType;
		this.workHoursPreferred = workHoursPreferred;
		this.salaryPerHour = salaryPerHour;
		this.currMonthWorkHoursCnt = 0;
		this.empRole = null;
		this.numID = ++empSerialNum;
	}

	public eEmployeesTypes getEmployeeType() {
		return this.empType;
	}

	public void setEmpType(eEmployeesTypes empType) {
		this.empType = empType;
	}
	
	public WorkHours getWorkHoursPreferred() {
		return workHoursPreferred;
	}

	protected void setWorkHoursPreferred(WorkHours workHoursPreferred) {
		this.workHoursPreferred = workHoursPreferred;
	}

	public int getSalaryPerHour() {
		return salaryPerHour;
	}

	protected void setSalaryPerHour(int salaryPerHour) {
		this.salaryPerHour = salaryPerHour;
	}

	public int getMonthWorkHoursCnt () {
		return this.currMonthWorkHoursCnt;
	}
	
	public void setCurrMonthWorkHoursCnt(int currMonthWorkHoursCnt) {
		this.currMonthWorkHoursCnt = currMonthWorkHoursCnt;
	}

	public Role getRole() {
		return this.empRole;
	}
	
	protected void setRole(Role R) {
		this.empRole = R;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Employee)) {
			return false;
		}

		Employee temp = (Employee) other;
		return this.name.equals(temp.name) && this.numID == temp.numID;
	}
	
}
