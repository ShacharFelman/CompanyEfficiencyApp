package model;

import model.CompanyModel.eEmployeesTypes;

public class EmployeeBonusSalary extends EmployeeBaseSalary {

	private int monthlySalesPerc;

	public EmployeeBonusSalary(String name, WorkHours workHoursPreferred, int salaryPerHour) {
		super(name, eEmployeesTypes.BaseBonusSalary, workHoursPreferred, salaryPerHour);
	}

	public int getSalesPerc() {
		return monthlySalesPerc;
	}

	protected void setSalesPerc(int salesPerc) {
		this.monthlySalesPerc = salesPerc;
	}

}
