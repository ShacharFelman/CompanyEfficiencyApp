package listeners;

import java.io.*;

import model.*;

public interface ModelListenable {

	public void modelUpdateCompanyName();
	public void modelNewDepartmentAdded (Department D);
	public void modelNewRoleAdded (Role R);
	public void modelNewEmployeeAdded (Employee E);
	void saveToFile() throws FileNotFoundException, IOException;
	void loadFromFile() throws FileNotFoundException, ClassNotFoundException, IOException;
}
