package exceptions;

public class RoleDepartmentTypesMismatchException extends Exception {

	public RoleDepartmentTypesMismatchException() {
		super("Role type not match to department type");
	}
	
}
