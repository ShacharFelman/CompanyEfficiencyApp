package model;

import java.time.*;
import java.util.*;

import exceptions.*;
import listeners.*;

public class CompanyModel extends CompanyEntity {

	public enum eWorkTypes {
		Regular, Early, Later, Home
	}

	public enum eDepartmentsTypes {
		Permanent, Dynamic, Synchronized
	}

	public enum eRolesTypes {
		Permanent, Dynamic
	}

	public enum eEmployeesTypes {
		BaseSalary, SalaryPerHour, BaseBonusSalary
	}

	public final static LocalTime REGULAR_START_HOUR = LocalTime.of(8, 0);
	public final static int WORKING_HOURS_IN_DAY = 8;
	public final static Double EFFICIENCY = 0.2;
	public final static Double HOME_EFFICIENCY = 0.1;

	private transient Vector<ModelListenable> allListeners;

	private String name;
	private Set<Department> allDepartments;
	private Set<Employee> allEmployees;
	private boolean isHardCodedInProcess;

	public int lastDepSerialNum;
	public int lastRoleSerialNum;
	public int lastEmpSerialNum;

	public CompanyModel(String name) {
		this.name = name;
		this.allDepartments = new LinkedHashSet<Department>();
		this.allEmployees = new LinkedHashSet<Employee>();
		isHardCodedInProcess = false;
	}

	public void registerListener(ModelListenable l) {
		if (allListeners == null) {
			allListeners = new Vector<ModelListenable>();
		}
		allListeners.add(l);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		for (ModelListenable l : allListeners) {
			l.modelUpdateCompanyName();
		}
	}

	public void setHardCodedLoadingStatus(boolean isHardCodedInProcess) {
		this.isHardCodedInProcess = isHardCodedInProcess;
	}
	
	public void addEmployee(Employee E) throws EntityAlreadyExistsException {
		boolean addStatus = this.allEmployees.add(E);
		if (!addStatus) {
			throw new EntityAlreadyExistsException(E);
		}
		if (!isHardCodedInProcess) {
			for (ModelListenable l : allListeners) {
				l.modelNewEmployeeAdded(E);
			}
		}
	}

	public Set<Employee> getEmployees() {
		return this.allEmployees;
	}

	public Set<Employee> getWorkingEmployees() {
		Set<Employee> workingEmployees = new LinkedHashSet<Employee>();
		for (Employee E : allEmployees) {
			if (E.getRole() != null) {
				workingEmployees.add(E);
			}
		}
		return workingEmployees;
	}

	public int getNumOfEmployees() {
		return allEmployees.size();
	}

	public void removeEmployee(Employee E) throws EntityNotFoundException {
		if (E.getRole() != null) {
			E.getRole().setEmployee(null);
		}
		boolean removeStatus = this.allEmployees.remove(E);
		if (!removeStatus) {
			throw new EntityNotFoundException(E);
		}
	}

	public void setEmployee(Employee E, String name, eEmployeesTypes empType, int salaryPerHour, WorkHours workHours,
			int currMonthWorkHoursCnt, int salesPerc) {
		E.setName(name);
		E.setEmpType(empType);
		E.setSalaryPerHour(salaryPerHour);
		E.setWorkHoursPreferred(workHours);
		E.setCurrMonthWorkHoursCnt(currMonthWorkHoursCnt);
		if (E.getEmployeeType() == eEmployeesTypes.BaseBonusSalary) {
			((EmployeeBonusSalary) E).setSalesPerc(salesPerc);
		}
	}

	public void addDepartment(Department D) throws EntityAlreadyExistsException {
		boolean addStatus = this.allDepartments.add(D);
		if (!addStatus) {
			throw new EntityAlreadyExistsException(D);
		}
		if (!isHardCodedInProcess) {
			for (ModelListenable l : allListeners) {
				l.modelNewDepartmentAdded(D);
			}
		}
	}

	public void setDepartment(Department D, String name, eDepartmentsTypes depType, WorkHours theWH)
			throws EntityAlreadyExistsException, WorkHoursTimeException, RoleDepartmentTypesMismatchException {
		D.setName(name);
		D.setDepartmentType(depType, theWH);
	}

	public void setRole(Role R, String name, Department D, eRolesTypes roleType, WorkHours theWH, Employee E)
			throws EntityAlreadyExistsException, WorkHoursTimeException, RoleDepartmentTypesMismatchException {
		R.setName(name);
		R.setEmployee(E);
		if (D.getDepartmentType() == eDepartmentsTypes.Permanent && roleType != eRolesTypes.Permanent) {
			throw new RoleDepartmentTypesMismatchException();
		}
		R.setRoleType(roleType);
		if (D.getDepartmentType() == eDepartmentsTypes.Synchronized) {
			R.setWorkHoursActual(D.getWorkHoursActual());
		} else if (theWH == null) {
			throw new WorkHoursTimeException("Actual work hours is missing");
		}
		R.setWorkHoursActual(theWH);
	}

	public Set<Department> getDepatments() {
		return allDepartments;
	}

	public void removeDepartment(Department D) throws EntityNotFoundException {
		boolean removeStatus = this.allDepartments.remove(D);
		if (!removeStatus) {
			throw new EntityNotFoundException(D);
		}
	}

	public void removeRole(Department D, Role R) throws EntityNotFoundException {
		D.removeRole(R);
	}

	public int getNumOfDepartments() {
		return allDepartments.size();
	}

	public Department getDepartmentByID(int numID) {
		for (Department D : allDepartments) {
			if (D.getNumID() == numID) {
				return D;
			}
		}
		return null;
	}

	public void setDepartmentName(Department dep, String name) {
		dep.setName(name);
	}

	public int getNumOfRoles() {
		int rolesCount = 0;
		for (Department D : allDepartments) {
			rolesCount += D.getNumOfRoles();
		}
		return rolesCount;
	}

	public double calcEfficiency() {
		double sum = 0;
		for (Department theDep : allDepartments) {
			sum += theDep.calcEfficiency();
		}
		return sum;
	}
	
	public double calcEfficiencyProfit() {
		double sum = 0;
		for (Department theDep : allDepartments) {
			sum += theDep.calcEfficiencyProfit();
		}
		return sum;
	}

	public double calcDepartmentEfficiency(Department D) {
		return D.calcEfficiency();
	}

	public double calcEmployeeEfficiency(Employee E) {
		if (E.getRole() != null) {
			return E.getRole().calcEfficiency();
		}
		return 0;
	}
	
	public double calcDepartmentEfficiencyProfit(Department D) {
		return D.calcEfficiencyProfit();
	}

	public double calcEmployeeEfficiencyProfit(Employee E) {
		if (E.getRole() != null) {
			return E.getRole().calcEfficiencyProfit();
		}
		return 0;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof CompanyModel))
			return false;

		CompanyModel temp = (CompanyModel) other;
		return this.name.equals(temp.name);
	}
}