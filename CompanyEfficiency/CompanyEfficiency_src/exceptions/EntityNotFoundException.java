package exceptions;

import model.CompanyEntity;

public class EntityNotFoundException extends Exception {

	public EntityNotFoundException(CompanyEntity entity) {
		super(entity.toStringClassID() + " not found");
	}
}

