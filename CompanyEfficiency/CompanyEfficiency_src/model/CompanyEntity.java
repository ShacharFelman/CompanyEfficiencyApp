package model;

import java.io.Serializable;

public abstract class CompanyEntity implements Serializable {
	
	protected String name;
	protected int numID;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNumID() {
		return numID;
	}

	@Override
	public String toString() {
		return numID + " " + name;
	}
	
	public String toStringClassID() {
		return getClass().getSimpleName() + " " + numID;
	}

}
