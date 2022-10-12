package controller;

import java.io.*;
import java.time.*;
import java.util.*;

import javax.swing.JOptionPane;

import exceptions.*;
import listeners.*;
import model.*;
import model.CompanyModel.*;
import view.*;

public class Controller implements ModelListenable, ViewListenable {

	static final String fileName = "CompanyData.dat";

	private CompanyModel companyModel;
	private MainCompanyView companyView;

	public Controller(CompanyModel theModel, MainCompanyView theView) {
		this.companyModel = theModel;
		this.companyView = theView;
		companyModel.registerListener(this);
		companyView.registerListener(this);
		try {
			loadFromFile();
		} catch (FileNotFoundException e) {
			companyModel.setName(JOptionPane.showInputDialog(null, "Please enter you company name:"));
			try {
				createHardCodedObjects(this.companyModel);
			} catch (EntityAlreadyExistsException | WorkHoursTimeException | RoleDepartmentTypesMismatchException e1) {
			}
		} catch (ClassNotFoundException | IOException e) {
			modelErrorMessage("Load data from file", "" + e.getMessage() + "\nStarting new company.");
			try {
				createHardCodedObjects(this.companyModel);
			} catch (EntityAlreadyExistsException | WorkHoursTimeException | RoleDepartmentTypesMismatchException e1) {
			}
		}
	}

	public CompanyModel viewGetCompany() {
		return this.companyModel;
	}

	public String viewGetCompanyName() {
		return companyModel.getName();
	}

	public void viewSetCompanyName(String name) {
		this.companyModel.setName(name);
	}

	public void modelUpdateCompanyName() {
		companyView.updateCompanyName();
	}

	public Set<Employee> viewGetEmployees() {
		return companyModel.getEmployees();
	}
	
	public Set<Employee> viewGetWorkingEmployees() {
		return companyModel.getWorkingEmployees();
	}

	public int viewGetNumOfEmployees() {
		return companyModel.getNumOfEmployees();
	}

	public void viewAddDepartment(String name, eDepartmentsTypes depType, WorkHours theWH)
			throws EntityAlreadyExistsException, WorkHoursTimeException {
		companyModel.addDepartment(new Department(name, depType, theWH));
	}

	public void viewSetDepartment(Department D, String name, eDepartmentsTypes depType, WorkHours theWH)
			throws EntityAlreadyExistsException, WorkHoursTimeException, RoleDepartmentTypesMismatchException {
		companyModel.setDepartment(D, name, depType, theWH);
	}

	public void viewRemoveDepartment(Department D) throws EntityNotFoundException {
		companyModel.removeDepartment(D);
	}

	public void viewAddRole(String name, Department D, eRolesTypes roleType, WorkHours workHours, Employee E) {
		Role R = new Role(name, roleType, workHours);
		try {
			D.addRole(R, E);
		} catch (EntityAlreadyExistsException | RoleDepartmentTypesMismatchException e) {
			modelErrorMessage("Add Role", e.getMessage());
		}
	}

	public void viewSetRole(Role R, String name, Department D, eRolesTypes roleType, WorkHours workHours, Employee E)
			throws EntityAlreadyExistsException, WorkHoursTimeException, RoleDepartmentTypesMismatchException {
		companyModel.setRole(R, name, D, roleType, workHours, E);
	}

	public void viewRemoveRole(Department D, Role R) throws EntityNotFoundException {
		companyModel.removeRole(D, R);
	}

	public void viewAddEmployee(String name, eEmployeesTypes empType, int salaryPerHour, WorkHours workHours)
			throws EntityAlreadyExistsException {
		Employee E = null;
		if (empType == eEmployeesTypes.BaseSalary) {
			E = new EmployeeBaseSalary(name, workHours, salaryPerHour);
		} else if (empType == eEmployeesTypes.SalaryPerHour) {
			E = new EmployeeHourSalary(name, workHours, salaryPerHour);
		} else if (empType == eEmployeesTypes.BaseBonusSalary) {
			E = new EmployeeBonusSalary(name, workHours, salaryPerHour);
		}
		companyModel.addEmployee(E);
	}

	public void setEmployee(Employee E, String name, eEmployeesTypes empType, int salaryPerHour, WorkHours workHours,
			int currMonthWorkHoursCnt, int salesPerc) {
		companyModel.setEmployee(E, name, empType, salaryPerHour, workHours, currMonthWorkHoursCnt, salesPerc);
	}
	
	public void viewRemoveEmployee(Employee E) throws EntityNotFoundException {
		companyModel.removeEmployee(E);
	}

	public Set<Department> viewGetDepartments() {
		return companyModel.getDepatments();
	}

	public int viewGetNumOfDepartments() {
		return companyModel.getNumOfDepartments();
	}

	public int viewGetNumOfRoles() {
		return companyModel.getNumOfRoles();
	}

	public void modelErrorMessage(String operation, String errorMsg) {
		companyView.msgPopupError(operation, errorMsg);
	}

	public void modelSuccessMessage(String operation, String errorMsg) {
		companyView.msgPopupError(operation, errorMsg);
	}

	@Override
	public void modelNewDepartmentAdded(Department D) {
		companyView.msgPopupSuccess(D.getName() + " added Successfully");
	}

	@Override
	public void modelNewRoleAdded(Role R) {
		companyView.msgPopupSuccess(R.getName() + " added Successfully");
	}

	@Override
	public void modelNewEmployeeAdded(Employee E) {
		companyView.msgPopupSuccess(E.getName() + " added Successfully");
	}

	public static void createHardCodedObjects(CompanyModel company)
			throws EntityAlreadyExistsException, WorkHoursTimeException, RoleDepartmentTypesMismatchException {
		company.setHardCodedLoadingStatus(true);

		EmployeeHourSalary E1 = new EmployeeHourSalary("Avi", new WorkHours(eWorkTypes.Later, LocalTime.of(10, 0)), 30);
		EmployeeHourSalary E2 = new EmployeeHourSalary("Moshe", new WorkHours(eWorkTypes.Later, LocalTime.of(9, 0)), 30);
		EmployeeHourSalary E3 = new EmployeeHourSalary("Israel", new WorkHours(eWorkTypes.Early, LocalTime.of(7, 0)), 30);
		EmployeeHourSalary E4 = new EmployeeHourSalary("Dina", new WorkHours(eWorkTypes.Later, LocalTime.of(9, 0)), 40);
		EmployeeHourSalary E5 = new EmployeeHourSalary("Eli", new WorkHours(eWorkTypes.Home, null), 40);
		
		EmployeeBaseSalary E6 = new EmployeeBaseSalary("Michal", new WorkHours(eWorkTypes.Early, LocalTime.of(7, 0)), 110);
		EmployeeBaseSalary E7 = new EmployeeBaseSalary("Noy", new WorkHours(eWorkTypes.Early, LocalTime.of(7, 0)), 55);
		EmployeeBaseSalary E8 = new EmployeeBaseSalary("Shahar", new WorkHours(eWorkTypes.Later, LocalTime.of(12, 0)), 75);
		EmployeeBaseSalary E9 = new EmployeeBaseSalary("Kobi", new WorkHours(eWorkTypes.Regular, null), 40);
		EmployeeBaseSalary E10 = new EmployeeBaseSalary("Tal", new WorkHours(eWorkTypes.Regular, null), 40);
		EmployeeBaseSalary E11 = new EmployeeBaseSalary("Amit", new WorkHours(eWorkTypes.Regular, null), 40);
		EmployeeBaseSalary E12 = new EmployeeBaseSalary("Sapir", new WorkHours(eWorkTypes.Home, null), 35);
		
		EmployeeBaseSalary E13 = new EmployeeBaseSalary("Yossi", new WorkHours(eWorkTypes.Home, null), 200);
		EmployeeBaseSalary E14 = new EmployeeBaseSalary("David", new WorkHours(eWorkTypes.Regular, null), 180);
		EmployeeBaseSalary E15 = new EmployeeBaseSalary("Shani", new WorkHours(eWorkTypes.Later, LocalTime.of(9, 0)), 135);
		EmployeeHourSalary E16 = new EmployeeHourSalary("Tomer", new WorkHours(eWorkTypes.Home, null), 135);
		EmployeeHourSalary E17 = new EmployeeHourSalary("Gil", new WorkHours(eWorkTypes.Home, null), 150);
		
		EmployeeBaseSalary E18 = new EmployeeBaseSalary("Or", new WorkHours(eWorkTypes.Later, LocalTime.of(9, 0)), 120);
		EmployeeBaseSalary E19 = new EmployeeBaseSalary("Yoni", new WorkHours(eWorkTypes.Early, LocalTime.of(5, 0)), 90);
		EmployeeBaseSalary E20 = new EmployeeBaseSalary("Keren", new WorkHours(eWorkTypes.Later, LocalTime.of(10, 0)), 50);
		EmployeeBaseSalary E21 = new EmployeeBaseSalary("Ortal", new WorkHours(eWorkTypes.Later, LocalTime.of(11, 0)), 45);
		EmployeeBaseSalary E22 = new EmployeeBaseSalary("Ram", new WorkHours(eWorkTypes.Home, null), 40);
		EmployeeBonusSalary E23 = new EmployeeBonusSalary("Tamar", new WorkHours(eWorkTypes.Later, LocalTime.of(9, 0)), 80);
		EmployeeBonusSalary E24 = new EmployeeBonusSalary("Alon", new WorkHours(eWorkTypes.Early, LocalTime.of(5, 0)), 50);
		EmployeeBonusSalary E25 = new EmployeeBonusSalary("Lior", new WorkHours(eWorkTypes.Later, LocalTime.of(10, 0)),50);
		EmployeeBonusSalary E26 = new EmployeeBonusSalary("Boaz", new WorkHours(eWorkTypes.Later, LocalTime.of(11, 0)), 50);
		EmployeeBonusSalary E27 = new EmployeeBonusSalary("Dor", new WorkHours(eWorkTypes.Home, null), 50);

		company.addEmployee(E1);
		company.addEmployee(E2);
		company.addEmployee(E3);
		company.addEmployee(E4);
		company.addEmployee(E5);
		company.addEmployee(E6);
		company.addEmployee(E7);
		company.addEmployee(E8);
		company.addEmployee(E9);
		company.addEmployee(E10);
		company.addEmployee(E11);
		company.addEmployee(E12);
		company.addEmployee(E13);
		company.addEmployee(E14);
		company.addEmployee(E15);
		company.addEmployee(E16);
		company.addEmployee(E17);
		company.addEmployee(E18);
		company.addEmployee(E19);
		company.addEmployee(E20);
		company.addEmployee(E21);
		company.addEmployee(E22);
		company.addEmployee(E23);
		company.addEmployee(E24);
		company.addEmployee(E25);
		company.addEmployee(E26);
		company.addEmployee(E27);
		
		Department dep1 = new Department("Customer Service", eDepartmentsTypes.Synchronized, new WorkHours(eWorkTypes.Regular, null));
		Role role11 = new Role("Phone Guy", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		Role role12 = new Role("Phone Guy", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		Role role13 = new Role("Phone Guy", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		Role role14 = new Role("Technician", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		Role role15 = new Role("Technician", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		dep1.addRole(role11, E1);
		dep1.addRole(role12, E2);
		dep1.addRole(role13, E3);
		dep1.addRole(role14, E4);
		dep1.addRole(role15, E5);

		Department dep2 = new Department("Manufacturing", eDepartmentsTypes.Permanent, null);
		Role role21 = new Role("Manager", eRolesTypes.Permanent, new WorkHours(eWorkTypes.Regular, null));
		Role role22 = new Role("Product Operator", eRolesTypes.Permanent, new WorkHours(eWorkTypes.Early, LocalTime.of(6, 0)));
		Role role23 = new Role("Product Operator", eRolesTypes.Permanent, new WorkHours(eWorkTypes.Regular, null));
		Role role24 = new Role("Product Operator", eRolesTypes.Permanent, new WorkHours(eWorkTypes.Later, LocalTime.of(14, 0)));
		Role role25 = new Role("Packing Operator", eRolesTypes.Permanent, new WorkHours(eWorkTypes.Later, LocalTime.of(9, 0)));
		Role role26 = new Role("Packing Operator", eRolesTypes.Permanent, new WorkHours(eWorkTypes.Regular, null));
		Role role27 = new Role("Packing Operator", eRolesTypes.Permanent, new WorkHours(eWorkTypes.Later, LocalTime.of(12, 0)));
		dep2.addRole(role21, E6);
		dep2.addRole(role22, E7);
		dep2.addRole(role23, E8);
		dep2.addRole(role24, E9);
		dep2.addRole(role25, E10);
		dep2.addRole(role26, E11);
		dep2.addRole(role27, E12);

		Department dep3 = new Department("Engineering", eDepartmentsTypes.Dynamic, null);
		Role role31 = new Role("Master Engineer", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		Role role32 = new Role("Electric Engineer", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Early, LocalTime.of(6, 0)));
		Role role33 = new Role("Product Engineer", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Later, LocalTime.of(9, 0)));
		Role role34 = new Role("Efficiency Engineer", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Home, null));
		Role role35 = new Role("Program Engineer", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Home, null));
		dep3.addRole(role31, E13);
		dep3.addRole(role32, E14);
		dep3.addRole(role33, E15);
		dep3.addRole(role34, E16);
		dep3.addRole(role35, E17);

		Department dep4 = new Department("Research", eDepartmentsTypes.Dynamic, null);
		Role role41 = new Role("Data Scientist", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Home, null));
		Role role42 = new Role("Chemistry Scientist", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Later, LocalTime.of(10, 0)));
		Role role43 = new Role("assistant", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Early, LocalTime.of(6, 0)));
		Role role44 = new Role("assistant", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, LocalTime.of(10, 0)));
		Role role45 = new Role("assistant", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Later, LocalTime.of(10, 0)));
		dep4.addRole(role41, E18);
		dep4.addRole(role42, E19);
		dep4.addRole(role43, E20);
		dep4.addRole(role44, E21);
		dep4.addRole(role45, E22);

		Department dep5 = new Department("Marketing", eDepartmentsTypes.Dynamic, null);
		Role role51 = new Role("Manager", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		Role role52 = new Role("Product Seller", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Early, LocalTime.of(6, 0)));
		Role role53 = new Role("Product Seller", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Early, LocalTime.of(6, 0)));
		Role role54 = new Role("Strategy Developer", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Home, null));
		Role role55 = new Role("Lids Manager", eRolesTypes.Dynamic, new WorkHours(eWorkTypes.Regular, null));
		dep5.addRole(role51, E23);
		dep5.addRole(role52, E24);
		dep5.addRole(role53, E25);
		dep5.addRole(role54, E26);
		dep5.addRole(role55, E27);
		
		company.addDepartment(dep1);
		company.addDepartment(dep2);
		company.addDepartment(dep3);
		company.addDepartment(dep4);
		company.addDepartment(dep5);
		company.setHardCodedLoadingStatus(false);

	}

	@Override
	public double viewCalcEfficiencyCompany() {
		return companyModel.calcEfficiency();
	}

	@Override
	public double viewCalcEfficiencyDepartment(Department D) {
		return companyModel.calcDepartmentEfficiency(D);
	}

	@Override
	public double viewCalcEfficiencyEmployee(Employee E) {
		return companyModel.calcEmployeeEfficiency(E);
	}
	
	@Override
	public double viewCalcEfficiencyProfitCompany() {
		return companyModel.calcEfficiencyProfit();
	}

	@Override
	public double viewCalcEfficiencyProfitDepartment(Department D) {
		return companyModel.calcDepartmentEfficiencyProfit(D);
	}

	@Override
	public double viewCalcEfficiencyProfitEmployee(Employee E) {
		return companyModel.calcEmployeeEfficiencyProfit(E);
	}


	@Override
	public void loadFromFile() throws FileNotFoundException, ClassNotFoundException, IOException {
		ObjectInputStream inFile;
		inFile = new ObjectInputStream(new FileInputStream((fileName)));
		this.companyModel = (CompanyModel) inFile.readObject();
		inFile.close();
		companyModel.registerListener(this);
		Department.depSerialNum = companyModel.lastDepSerialNum;
		Role.roleSerialNum = companyModel.lastRoleSerialNum;
		Employee.empSerialNum = companyModel.lastEmpSerialNum;
		companyModel.setHardCodedLoadingStatus(false);
	}

	@Override
	public void saveToFile() throws FileNotFoundException, IOException {
		companyModel.lastDepSerialNum = Department.depSerialNum;
		companyModel.lastRoleSerialNum = Role.roleSerialNum;
		companyModel.lastEmpSerialNum = Employee.empSerialNum;
		ObjectOutputStream outFile;
		outFile = new ObjectOutputStream(new FileOutputStream((fileName)));
		outFile.writeObject(companyModel);
		outFile.close();
	}
}