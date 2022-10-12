package exceptions;

import model.CompanyEntity;

public class EntityAlreadyExistsException extends Exception {

	public EntityAlreadyExistsException(CompanyEntity entity) {
		super(entity.toStringClassID() + " already exists");
	}
}
