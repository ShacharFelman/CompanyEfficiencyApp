package model;

import java.util.*;

import exceptions.*;
import model.CompanyModel.*;

public class Department extends CompanyEntity implements HoursChangeable, HoursSynchronizable {

	protected Set<Role> allRoles;
	protected eDepartmentsTypes depType;
	protected WorkHours workHoursActual;
	public static int depSerialNum = 0;

	public Department(String name, eDepartmentsTypes depType, WorkHours theWH) throws WorkHoursTimeException {
		if (depType == eDepartmentsTypes.Synchronized) {
			if (theWH == null) {
				throw new WorkHoursTimeException("Work hours for synchronized department is missing");
			}
			this.workHoursActual = theWH;
		} else {
			this.workHoursActual = null;
		}
		this.name = name;
		this.depType = depType;
		this.allRoles = new LinkedHashSet<Role>();
		this.numID = ++depSerialNum;
	}

	public eDepartmentsTypes getDepartmentType() {
		return this.depType;
	}

	public void setDepartmentType(eDepartmentsTypes depType, WorkHours theWH)
			throws WorkHoursTimeException, RoleDepartmentTypesMismatchException {
		if (depType == eDepartmentsTypes.Permanent) {
			for (Role R : allRoles) {
				if (R.getRoleType() != eRolesTypes.Permanent) {
					throw new RoleDepartmentTypesMismatchException();
				}
			}
		} else {
			for (Role R : allRoles) {
				if (R.getRoleType() != eRolesTypes.Dynamic) {
					throw new RoleDepartmentTypesMismatchException();
				}
			}
		}
		if (depType == eDepartmentsTypes.Synchronized) {
			if (theWH == null) {
				throw new WorkHoursTimeException("Work hours for synchronized department is missing");
			}
			this.workHoursActual = theWH;
			for (Role R : allRoles) {
				R.setWorkHoursActual(theWH);
			}

		} else {
			this.workHoursActual = null;
		}
		this.depType = depType;
	}

	@Override
	public boolean isHoursSynchronizable() {
		return (this.depType == eDepartmentsTypes.Synchronized);
	}

	@Override
	public boolean isHoursChangeable() {
		return (this.depType == eDepartmentsTypes.Dynamic);
	}

	public WorkHours getWorkHoursActual() {
		return this.workHoursActual;
	}

	public int getNumOfRoles() {
		return allRoles.size();
	}

	public Set<Role> getRoles() {
		return allRoles;
	}

	public void addRole(Role R, Employee E) throws EntityAlreadyExistsException, RoleDepartmentTypesMismatchException {
		if (((isHoursChangeable() || isHoursSynchronizable()) ^ R.isHoursChangeable())) {
			throw new RoleDepartmentTypesMismatchException();
		}
		
		boolean addStatus = this.allRoles.add(R);
		if (!addStatus) {
			throw new EntityAlreadyExistsException(R);
		}

		R.setEmployee(E);
	}

	public void removeRole(Role R) throws EntityNotFoundException {
		boolean removeStatus = this.allRoles.remove(R);
		if (!removeStatus) {
			throw new EntityNotFoundException(R);
		}
		if (R.getEmployee() != null) {
			R.getEmployee().setRole(null);
		}
	}

	public Role getRoleByID(int numID) {
		for (Role R : allRoles) {
			if (R.getNumID() == numID) {
				return R;
			}
		}
		return null;
	}
	
	public double calcEfficiency() {
		double sum = 0 ; 
		for (Role theRole : allRoles) {
			sum += theRole.calcEfficiency(); 
		}
		 return sum;  
	}
	
	public double calcEfficiencyProfit() {
		double sum = 0 ; 
		for (Role theRole : allRoles) {
			sum += theRole.calcEfficiencyProfit(); 
		}
		 return sum;  
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Department))
			return false;

		Department temp = (Department) other;
		return this.name.equals(temp.name) && this.numID == temp.numID;
	}

}
