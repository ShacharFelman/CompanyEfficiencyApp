package listeners;

import java.util.*;

import exceptions.*;
import model.*;
import model.CompanyModel.*;

public interface ViewListenable {

	public CompanyModel viewGetCompany();
	public String viewGetCompanyName();
	public void viewSetCompanyName(String name);
	public Set<Employee> viewGetEmployees();
	public Set<Employee> viewGetWorkingEmployees();
	public Set<Department> viewGetDepartments();
	public int viewGetNumOfDepartments();
	public int viewGetNumOfRoles();
	public int viewGetNumOfEmployees();
	public void viewAddDepartment(String name, eDepartmentsTypes depType, WorkHours theWH) throws EntityAlreadyExistsException, WorkHoursTimeException;
	public void viewSetDepartment(Department D, String name, eDepartmentsTypes depType, WorkHours theWH) throws EntityAlreadyExistsException, WorkHoursTimeException, RoleDepartmentTypesMismatchException;
	public void viewRemoveDepartment(Department D) throws EntityNotFoundException;
	public void viewAddRole(String name, Department D, eRolesTypes roleType, WorkHours workHours, Employee E);
	public void viewSetRole(Role R, String name, Department D, eRolesTypes roleType, WorkHours workHours, Employee E) throws EntityAlreadyExistsException, WorkHoursTimeException, RoleDepartmentTypesMismatchException;
	public void viewRemoveRole (Department D, Role R) throws EntityNotFoundException;
	public void viewAddEmployee(String name, eEmployeesTypes empType, int salaryPerHour, WorkHours workHours) throws EntityAlreadyExistsException;
	public void setEmployee(Employee E, String name, eEmployeesTypes empType, int salaryPerHour, WorkHours workHours, int currMonthWorkHoursCnt, int salesPerc);
	public void viewRemoveEmployee(Employee E) throws EntityNotFoundException;
	public double viewCalcEfficiencyCompany();
	public double viewCalcEfficiencyDepartment(Department D);
	public double viewCalcEfficiencyEmployee(Employee E);
	double viewCalcEfficiencyProfitCompany();
	double viewCalcEfficiencyProfitDepartment(Department D);
	double viewCalcEfficiencyProfitEmployee(Employee E);

}
