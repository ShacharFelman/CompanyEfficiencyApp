package model;

import model.CompanyModel.eRolesTypes;
import model.CompanyModel.eWorkTypes;

public class Role extends CompanyEntity implements HoursChangeable {

	private Employee theEmployee;
	private WorkHours workHoursActual;
	private eRolesTypes roleType;

	public static int roleSerialNum = 0;

	public Role(String name, eRolesTypes roleType, WorkHours workHoursActual) {
		this.name = name;
		this.roleType = roleType;
		this.workHoursActual = workHoursActual;
		this.numID = ++roleSerialNum;
	}

	public eRolesTypes getRoleType() {
		return roleType;
	}

	protected void setRoleType(eRolesTypes roleType) {
		this.roleType = roleType;
	}

	@Override
	public boolean isHoursChangeable() {
		return (this.roleType == eRolesTypes.Dynamic);
	}

	protected void setWorkHoursActual(WorkHours workHoursActual) {
		this.workHoursActual = workHoursActual;
	}

	public Employee getEmployee() {
		return theEmployee;
	}

	public boolean isOccupied() {
		if (theEmployee == null) {
			return false;
		}
		return true;
	}

	protected void setEmployee(Employee E) {
		if (this.theEmployee != null) {
			this.theEmployee.setRole(null);
		}

		if (E != null) {
			if (E.getRole() != null) {
				E.getRole().setEmployee(null);
			}
			E.setRole(this);
		}
		
		this.theEmployee = E;
	}

	public WorkHours getWorkHoursActual() {
		return workHoursActual;
	}

	public int diffHour() { // if the employee is less efficient --> then the function return negative value
		if (this.theEmployee == null)
			return 0;

		int diffBetweenStartHourToPref = 0;
		int diffBetweenCurrentHourToPref = 0;
		int efficiencyHours = 0;
		eWorkTypes employeePreference = theEmployee.getWorkHoursPreferred().getWorkType();
		eWorkTypes roleDemand = workHoursActual.getWorkType();

		if (employeePreference == eWorkTypes.Home && roleDemand == eWorkTypes.Home) // If the preference of the
																						// employee and the role is home
			return CompanyModel.WORKING_HOURS_IN_DAY;
		else if (employeePreference == eWorkTypes.Home ^ roleDemand == eWorkTypes.Home) // dont want home but the
																								// roles demand is home
			return -(CompanyModel.WORKING_HOURS_IN_DAY);
		else if (roleDemand == eWorkTypes.Early && employeePreference == eWorkTypes.Early) {
			diffBetweenCurrentHourToPref = Math.abs(
					workHoursActual.getStartHour().getHour() - theEmployee.getWorkHoursPreferred().getStartHour().getHour());
			diffBetweenCurrentHourToPref = -1 * diffBetweenCurrentHourToPref;
			if (workHoursActual.getStartHour().getHour() < theEmployee.getWorkHoursPreferred().getStartHour().getHour()) // 8-who
																													// is
																													// bigger
				diffBetweenStartHourToPref = Math.abs(
						CompanyModel.REGULAR_START_HOUR.getHour() - theEmployee.getWorkHoursPreferred().getStartHour().getHour());
			else
				diffBetweenStartHourToPref = Math
						.abs(CompanyModel.REGULAR_START_HOUR.getHour() - workHoursActual.getStartHour().getHour());
			efficiencyHours = diffBetweenStartHourToPref + diffBetweenCurrentHourToPref;
			return efficiencyHours;
		} else if (roleDemand == eWorkTypes.Later && employeePreference == eWorkTypes.Later) {
			diffBetweenCurrentHourToPref = Math.abs(
					theEmployee.getWorkHoursPreferred().getStartHour().getHour() - workHoursActual.getStartHour().getHour());
			diffBetweenCurrentHourToPref = -1 * diffBetweenCurrentHourToPref;
			if (workHoursActual.getStartHour().getHour() > theEmployee.getWorkHoursPreferred().getStartHour().getHour()) // 8-who
																													// is
																													// bigger
				diffBetweenStartHourToPref = Math.abs(
						CompanyModel.REGULAR_START_HOUR.getHour() - theEmployee.getWorkHoursPreferred().getStartHour().getHour());
			else
				diffBetweenStartHourToPref = Math
						.abs(CompanyModel.REGULAR_START_HOUR.getHour() - workHoursActual.getStartHour().getHour());
			efficiencyHours = diffBetweenStartHourToPref + diffBetweenCurrentHourToPref;
			return efficiencyHours;
		} else if (employeePreference == eWorkTypes.Regular && roleDemand == eWorkTypes.Regular)
			return 0;
		else {
			diffBetweenStartHourToPref = Math.abs(this.workHoursActual.getStartHour().getHour()
					- theEmployee.getWorkHoursPreferred().getStartHour().getHour());
			diffBetweenStartHourToPref = 0 - diffBetweenStartHourToPref;
		}
		return diffBetweenStartHourToPref;
	}

	public double calcEfficiency() {
		int diffHour = diffHour();
		if (diffHour == CompanyModel.WORKING_HOURS_IN_DAY)
			return CompanyModel.WORKING_HOURS_IN_DAY *  CompanyModel.HOME_EFFICIENCY;
		else if (diffHour == -CompanyModel.WORKING_HOURS_IN_DAY)
			return -CompanyModel.WORKING_HOURS_IN_DAY *  CompanyModel.HOME_EFFICIENCY;
		return diffHour *  CompanyModel.EFFICIENCY;
	}
	
	public double calcEfficiencyProfit() {
		if (theEmployee != null) {
			return calcEfficiency() * theEmployee.getSalaryPerHour();
		}
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Role))
			return false;

		Role temp = (Role) other;
		return this.name.equals(temp.name) && this.numID == temp.numID;
	}
}