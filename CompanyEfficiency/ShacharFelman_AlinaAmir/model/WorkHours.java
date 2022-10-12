package model;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.*;

import exceptions.*;
import model.CompanyModel.*;

public class WorkHours implements Serializable {

	private eWorkTypes theWorkType;
	private LocalTime startHour;

	public WorkHours(eWorkTypes theWorkType, LocalTime startHour) throws WorkHoursTimeException {
		if (theWorkType == eWorkTypes.Regular) {
			setWorkTypeHours(theWorkType, CompanyModel.REGULAR_START_HOUR);
		}
		else if (theWorkType == eWorkTypes.Home) {
			setWorkTypeHours(theWorkType, null);
		}
		else {
			setWorkTypeHours(theWorkType, startHour);
		}
	}

	public eWorkTypes getWorkType() {
		return theWorkType;
	}

	protected void setWorkTypeHours(eWorkTypes theWorkType, LocalTime startHour) throws WorkHoursTimeException {
		if (startHour != null) {
			if (theWorkType == eWorkTypes.Early && startHour.isAfter(CompanyModel.REGULAR_START_HOUR)
					&& startHour.isBefore(LocalTime.MIDNIGHT)) {
				throw new WorkHoursTimeException("Start hour not in range of working day");
			}
			if (theWorkType == eWorkTypes.Later && startHour.isBefore(CompanyModel.REGULAR_START_HOUR)
					&& startHour.isAfter(LocalTime.MIDNIGHT)) {
				throw new WorkHoursTimeException("Start hour not in range of working day");
			}
			this.startHour = startHour;
		}
		else if (theWorkType != eWorkTypes.Home) {
			throw new WorkHoursTimeException("Starting work hours is missing");
		}
		this.theWorkType = theWorkType;
	}

	public LocalTime getStartHour() {
		return startHour;
	}

	public LocalTime getEndHour() {
		LocalTime endHour = startHour.plusHours(1);
		return endHour.plusHours(CompanyModel.WORKING_HOURS_IN_DAY);
	}

	public double calcHoursDiff() {
		if (theWorkType != eWorkTypes.Home) {
			return (Math.abs(ChronoUnit.MINUTES.between(CompanyModel.REGULAR_START_HOUR, startHour)/60));
		}
		return 0;
	}
	
	@Override
	public String toString() {
		if (theWorkType == eWorkTypes.Home) {
			return "Home";
		}
		else if (theWorkType == eWorkTypes.Regular) {
			return CompanyModel.REGULAR_START_HOUR + " - " + CompanyModel.REGULAR_START_HOUR.plusHours(CompanyModel.WORKING_HOURS_IN_DAY + 1);
		}
		return startHour + " - " + startHour.plusHours(CompanyModel.WORKING_HOURS_IN_DAY + 1);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof WorkHours))
			return false;

		WorkHours temp = (WorkHours) other;
		return theWorkType.equals(temp.theWorkType) && this.startHour.equals(temp.getStartHour());
	}

}
