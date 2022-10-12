package view;

import java.io.*;
import java.time.*;
import java.util.*;

import exceptions.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.util.Duration;
import listeners.*;
import model.*;
import model.CompanyModel.*;
import viewDesignEvents.*;

public class ViewMngPanes {
	MainCompanyView mainView;
	private static final double ENTITY_BUTTONS_SIZE = 60;

	protected ViewMngPanes(MainCompanyView mainView) throws FileNotFoundException {
		this.mainView = mainView;
	}

	protected void setSubMenuButtonDesign(Button btn) {
		btn.setFont(MainCompanyView.FONT_SUB_MENU);
		btn.setBackground(MainCompanyView.BACKGROUND_MNG_BUTTONS);
		btn.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);
		btn.setOnMouseEntered(new EventMouseEnteredMngButton());
		btn.setOnMouseExited(new EventMouseExitedMngButton());
	}

	protected void setEditEntityButtonDesign(Button btn) {
		btn.setMinWidth(ENTITY_BUTTONS_SIZE);
		btn.setBackground(MainCompanyView.BACKGROUND_MNG_BUTTONS);
		btn.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);
		btn.setOnMouseEntered(new EventMouseEnteredMngButton());
		btn.setOnMouseExited(new EventMouseExitedMngButton());
	}

	protected void setRemoveEntityButtonDesign(Button btn) {
		btn.setMinWidth(ENTITY_BUTTONS_SIZE);
		btn.setBackground(MainCompanyView.BACKGROUND_MNG_BUTTONS_REMOVE);
		btn.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);
		btn.setOnMouseEntered(new EventMouseEnteredMngButtonRemove());
		btn.setOnMouseExited(new EventMouseExitedMngButtonRemove());
	}

// <==========================================>
// <= Build Pane: Manage Company Departments =>
// ===========================================>
	public VBox setMngCompanyPane(ViewListenable l) {
		final double COL_SIZE_ID = 50;
		final double COL_SIZE_NAME = 150;
		final double COL_SIZE_TYPE = 120;
		final double COL_SIZE_WORK_HOURS = 120;
		final double COL_SIZE_NUM_OF_ROLES = 120;
		final double SPACE_BETWEEN_DETAILS_TO_BUTTONS = 170;
		final double SPACE_BETWEEN_BUTTONS = 10;

		VBox vbMngCompanyModel = new VBox(5);
		vbMngCompanyModel.setPadding(new Insets(10));

		Label lblHeadLineCompanyName = new Label(l.viewGetCompanyName() + " Departments");
		lblHeadLineCompanyName.setFont(MainCompanyView.FONT_HEAD_LINE);
		HBox hbCompanyTitle = new HBox(10, lblHeadLineCompanyName);
		hbCompanyTitle.setAlignment(Pos.CENTER);

		GridPane gpCompanyData = new GridPane();
		gpCompanyData.setPadding(new Insets(10));
		gpCompanyData.setHgap(50);
		gpCompanyData.setVgap(5);
		Label lblTitleCompanyName = new Label("Name");
		Label lblTitleCompanyDepatrmentsCnt = new Label("No. of Departments");
		Label lblTitleCompanyRolesCnt = new Label("No. of Roles");
		Label lblTitleCompanyEmployeesCnt = new Label("No. of Employees");
		Label lblCompanyName = new Label(l.viewGetCompanyName());
		Label lblCompanyDepatrmentsCnt = new Label(l.viewGetNumOfDepartments() + "");
		Label lblCompanyRolesCnt = new Label(l.viewGetNumOfRoles() + "");
		Label lblCompanyEmployeesCnt = new Label(l.viewGetNumOfEmployees() + "");
		gpCompanyData.addRow(0, lblTitleCompanyName, lblTitleCompanyDepatrmentsCnt, lblTitleCompanyRolesCnt,
				lblTitleCompanyEmployeesCnt);
		gpCompanyData.addRow(1, new Separator(), new Separator(), new Separator(), new Separator());
		gpCompanyData.addRow(2, lblCompanyName, lblCompanyDepatrmentsCnt, lblCompanyRolesCnt, lblCompanyEmployeesCnt);
		gpCompanyData.setAlignment(Pos.CENTER);
		gpCompanyData.setBorder(new Border(
				new BorderStroke(Color.SILVER, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		HBox hbCompanyButtons = new HBox(100);
		hbCompanyButtons.setAlignment(Pos.CENTER);

		TextField tfEditName = new TextField();
		Button btnEditCompany = new Button("Edit Company Name");
		setSubMenuButtonDesign(btnEditCompany);
		Button btnConfirmEditName = new Button("Save name");
		setSubMenuButtonDesign(btnConfirmEditName);
		btnEditCompany.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				tfEditName.setText(l.viewGetCompanyName());
				hbCompanyTitle.getChildren().clear();
				hbCompanyTitle.getChildren().addAll(tfEditName, btnConfirmEditName);
			}
		});
		btnConfirmEditName.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (tfEditName.getText().isEmpty()) {
					mainView.msgPopupError("Edit company name", "Name cannot be empty.");
				} else {
					for (ViewListenable l : mainView.allListeners) {
						l.viewSetCompanyName(tfEditName.getText());
						mainView.setSideMenuPane();
						mainView.setMngCompanyPane();
					}
				}
			}
		});

		Button btnAddDepartment = new Button("+ Add Department");
		setSubMenuButtonDesign(btnAddDepartment);
		btnAddDepartment.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				mainView.addDepartmentFromUser(null);
			}
		});
		hbCompanyButtons.getChildren().addAll(btnEditCompany, btnAddDepartment);

		HBox hbDepHeadline = new HBox();
		Label lblTitleNumID = new Label("ID");
		Label lblTitleDepName = new Label("Name");
		Label lblTitleDepType = new Label("Type");
		Label lblTitleDepWorkHours = new Label("Work Hours");
		Label lblTitleDepRolesCnt = new Label("No. of Roles");
		lblTitleNumID.setPrefWidth(COL_SIZE_ID);
		lblTitleDepName.setPrefWidth(COL_SIZE_NAME);
		lblTitleDepType.setPrefWidth(COL_SIZE_TYPE);
		lblTitleDepWorkHours.setPrefWidth(COL_SIZE_WORK_HOURS);
		lblTitleDepRolesCnt.setPrefWidth(COL_SIZE_NUM_OF_ROLES);
		hbDepHeadline.getChildren().addAll(lblTitleNumID, lblTitleDepName, lblTitleDepType, lblTitleDepWorkHours,
				lblTitleDepRolesCnt);

		ListView<HBox> lstDepartments = new ListView<HBox>();
		VBox vbDepatments = new VBox(lstDepartments);
		Set<Department> allDepartments = l.viewGetDepartments();
		for (Department D : allDepartments) {
			GridPane gpDepDetails = new GridPane();
			GridPane gpDepButtons = new GridPane();
			gpDepButtons.setHgap(SPACE_BETWEEN_BUTTONS);

			Label lblNumID = new Label(D.getNumID() + "");
			Label lblDepName = new Label(D.getName());
			Label lblDepType = new Label(D.getDepartmentType().toString());
			Label lblDepWorkHours = new Label();
			Label lblDepRolesCnt = new Label(D.getNumOfRoles() + "");
			if (D.getDepartmentType() == eDepartmentsTypes.Synchronized) {
				lblDepWorkHours.setText(D.getWorkHoursActual().toString());
			}

			lblNumID.setPrefWidth(COL_SIZE_ID);
			lblDepName.setPrefWidth(COL_SIZE_NAME);
			lblDepType.setPrefWidth(COL_SIZE_TYPE);
			lblDepWorkHours.setPrefWidth(COL_SIZE_WORK_HOURS);
			lblDepRolesCnt.setPrefWidth(COL_SIZE_NUM_OF_ROLES);
			gpDepDetails.addRow(0, lblNumID, lblDepName, lblDepType, lblDepWorkHours, lblDepRolesCnt);

			Button btnDepEdit = new Button("Edit");
			Button btnDepRemove = new Button("Remove");
			setEditEntityButtonDesign(btnDepEdit);
			setRemoveEntityButtonDesign(btnDepRemove);
			gpDepButtons.addRow(0, btnDepEdit, btnDepRemove);

			HBox hbDepartments = new HBox(SPACE_BETWEEN_DETAILS_TO_BUTTONS);
			hbDepartments.getChildren().addAll(gpDepDetails, gpDepButtons);
			lstDepartments.getItems().add(hbDepartments);

			btnDepEdit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {
					mainView.changePaneToPresent(D, null);
				}
			});
			btnDepRemove.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {
					for (ViewListenable l : mainView.allListeners) {
						try {
							l.viewRemoveDepartment(D);
							mainView.setSideMenuPane();
							mainView.setMngCompanyPane();
						} catch (EntityNotFoundException e) {
							mainView.msgPopupError("Remove department", e.getMessage());
						}
					}
				}
			});
		}

		vbMngCompanyModel.getChildren().addAll(hbCompanyTitle, gpCompanyData, hbCompanyButtons, hbDepHeadline,
				new Separator(Orientation.HORIZONTAL), vbDepatments);
		return vbMngCompanyModel;
	}

// <========================================>
// <= Build Pane: Manage Company Employees =>
// =========================================>
	public VBox setMngEmployeesPane(ViewListenable l) {
		final double COL_SIZE_ID = 50;
		final double COL_SIZE_NAME = 150;
		final double COL_SIZE_TYPE = 120;
		final double COL_SIZE_PREF = 120;
		final double COL_SIZE_ROLE = 160;
		final double COL_SIZE_SALARY = 100;
		final double SPACE_BETWEEN_DETAILS_TO_BUTTONS = 10;
		final double SPACE_BETWEEN_BUTTONS = 10;

		VBox vbMngCompanyEmployees = new VBox(5);
		vbMngCompanyEmployees.setPadding(new Insets(10));

		Label lblHeadLineCompanyName = new Label(l.viewGetCompanyName() + " Employees");
		lblHeadLineCompanyName.setFont(MainCompanyView.FONT_HEAD_LINE);
		HBox hbCompanyTitle = new HBox(10, lblHeadLineCompanyName);
		hbCompanyTitle.setAlignment(Pos.CENTER);

		HBox hbEmployeesButtons = new HBox(100);
		hbEmployeesButtons.setAlignment(Pos.CENTER);
		Button btnAddEmployee = new Button("+ Add Employee");
		setSubMenuButtonDesign(btnAddEmployee);
		btnAddEmployee.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				mainView.addEmplpoyeeFromUser();
			}
		});
		hbEmployeesButtons.getChildren().addAll(btnAddEmployee);

		HBox hbDepHeadline = new HBox();
		Label lblTitleNumID = new Label("ID");
		Label lblTitleEmpName = new Label("Name");
		Label lblTitleEmpType = new Label("Type");
		Label lblTitleEmpPref = new Label("Work Preference");
		Label lblTitleEmpRole = new Label("Role");
		Label lblTitleEmpSalary = new Label("Salary per Hour");
		lblTitleNumID.setPrefWidth(COL_SIZE_ID);
		lblTitleEmpName.setPrefWidth(COL_SIZE_NAME);
		lblTitleEmpType.setPrefWidth(COL_SIZE_TYPE);
		lblTitleEmpPref.setPrefWidth(COL_SIZE_PREF);
		lblTitleEmpRole.setPrefWidth(COL_SIZE_ROLE);
		lblTitleEmpSalary.setPrefWidth(COL_SIZE_SALARY);
		hbDepHeadline.getChildren().addAll(lblTitleNumID, lblTitleEmpName, lblTitleEmpType, lblTitleEmpPref,
				lblTitleEmpRole, lblTitleEmpSalary);

		ListView<HBox> lstEmployees = new ListView<HBox>();
		VBox vbEmployees = new VBox(lstEmployees);
		Set<Employee> allEmployees = l.viewGetEmployees();
		for (Employee E : allEmployees) {
			GridPane gpEmpDetails = new GridPane();
			GridPane gpEmpButtons = new GridPane();
			gpEmpButtons.setHgap(SPACE_BETWEEN_BUTTONS);

			Label lblNumID = new Label(E.getNumID() + "");
			Label lblEmpName = new Label(E.getName());
			Label lblEmpType = new Label(E.getEmployeeType().toString());
			Label lblEmpPref = new Label(E.getWorkHoursPreferred().toString());
			Label lblEmpRole = new Label(E.getRole() != null ? E.getRole().toString() : "No Role");
			Label lblEmpSalary = new Label(E.getSalaryPerHour() + "");
			lblNumID.setPrefWidth(COL_SIZE_ID);
			lblEmpName.setPrefWidth(COL_SIZE_NAME);
			lblEmpType.setPrefWidth(COL_SIZE_TYPE);
			lblEmpPref.setPrefWidth(COL_SIZE_PREF);
			lblEmpRole.setPrefWidth(COL_SIZE_ROLE);
			lblEmpSalary.setPrefWidth(COL_SIZE_SALARY);
			gpEmpDetails.addRow(0, lblNumID, lblEmpName, lblEmpType, lblEmpPref, lblEmpRole, lblEmpSalary);

			Button btnEmpEdit = new Button("Edit");
			Button btnEmpRemove = new Button("Remove");
			setEditEntityButtonDesign(btnEmpEdit);
			setRemoveEntityButtonDesign(btnEmpRemove);
			gpEmpButtons.addRow(0, btnEmpEdit, btnEmpRemove);

			HBox hbEmployee = new HBox(SPACE_BETWEEN_DETAILS_TO_BUTTONS);
			hbEmployee.getChildren().addAll(gpEmpDetails, gpEmpButtons);
			lstEmployees.getItems().add(hbEmployee);

			btnEmpEdit.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {
					mainView.changePaneToPresent(E, null);
				}
			});
			btnEmpRemove.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {
					for (ViewListenable l : mainView.allListeners) {
						try {
							l.viewRemoveEmployee(E);
							mainView.setSideMenuPane();
							mainView.setMngEmployeesPane();
						} catch (EntityNotFoundException e) {
							mainView.msgPopupError("Remove Employee", e.getMessage());
						}
					}
				}
			});
		}

		vbMngCompanyEmployees.getChildren().addAll(hbCompanyTitle, hbEmployeesButtons, hbDepHeadline,
				new Separator(Orientation.HORIZONTAL), vbEmployees);

		return vbMngCompanyEmployees;
	}

// <=================================>
// <= Build Pane: Manage Department =>
// ==================================>
	public VBox setMngDepartmentPane(ViewListenable l, Department activeDepartment) {
		final double COL_SIZE_ID = 50;
		final double COL_SIZE_NAME = 150;
		final double COL_SIZE_TYPE = 120;
		final double COL_SIZE_EMPLOYEE = 100;
		final double COL_SIZE_WORK_TYPE = 90;
		final double COL_SIZE_WORK_HOURS = 80;
		final double SPACE_BETWEEN_DETAILS_TO_BUTTONS = 130;
		final double SPACE_BETWEEN_BUTTONS = 10;

		VBox vbMngDepartment = new VBox(10);
		vbMngDepartment.setPadding(new Insets(10));

		Label lblHeadLineDepName = new Label(activeDepartment.getName() + " Department");
		lblHeadLineDepName.setFont(MainCompanyView.FONT_HEAD_LINE);
		HBox hbDepTitle = new HBox(lblHeadLineDepName);
		hbDepTitle.setAlignment(Pos.CENTER);

		GridPane gpDepData = new GridPane();
		gpDepData.setPadding(new Insets(10));
		gpDepData.setHgap(100);
		gpDepData.setVgap(5);
		Label lblTitleDepID = new Label("ID");
		Label lblTitleDepName = new Label("Name");
		Label lblTitleDepType = new Label("Type");
		Label lblTitleDepWorkHours = new Label("Work Hours");
		Label lblDepID = new Label(activeDepartment.getNumID() + "");
		Label lblDepName = new Label(activeDepartment.getName());
		Label lblDepType = new Label(activeDepartment.getDepartmentType().toString());
		Label lblDepWorkHours = new Label("Work Hours");
		if (activeDepartment.isHoursSynchronizable()) {
			gpDepData.addRow(0, lblTitleDepID, lblTitleDepName, lblTitleDepType, lblTitleDepWorkHours);
			gpDepData.addRow(1, new Separator(), new Separator(), new Separator(), new Separator());
			gpDepData.addRow(2, lblDepID, lblDepName, lblDepType, lblDepWorkHours);
			lblDepWorkHours.setText(activeDepartment.getWorkHoursActual().toString());
		} else {
			gpDepData.addRow(0, lblTitleDepID, lblTitleDepName, lblTitleDepType);
			gpDepData.addRow(1, new Separator(), new Separator(), new Separator());
			gpDepData.addRow(2, lblDepID, lblDepName, lblDepType);
		}
		gpDepData.setAlignment(Pos.CENTER);
		gpDepData.setBorder(new Border(
				new BorderStroke(Color.SILVER, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		HBox hbDepButtons = new HBox(100);
		hbDepButtons.setAlignment(Pos.CENTER);
		if (activeDepartment.getDepartmentType() == eDepartmentsTypes.Synchronized) {
			Button btnEditDepartment = new Button("Edit Department's Hours");
			setSubMenuButtonDesign(btnEditDepartment);
			btnEditDepartment.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {
					mainView.addDepartmentFromUser(activeDepartment);
				}
			});
			hbDepButtons.getChildren().add(btnEditDepartment);
		}
		Button btnAddRole = new Button("+ Add Role");
		setSubMenuButtonDesign(btnAddRole);
		btnAddRole.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				mainView.addRoleFromUser(activeDepartment);
			}
		});
		hbDepButtons.getChildren().add(btnAddRole);

		HBox hbRoleHeadline = new HBox();
		Label lblRolesTitleNumID = new Label("ID");
		Label lblRolesTitleRoleName = new Label("Name");
		Label lblRolesTitleDepType = new Label("Type");
		Label lblRolesTitleRoleEmployee = new Label("Employee");
		Label lblRolesTitleWorkType = new Label("Work Type");
		Label lblRolesTitleWorkHours = new Label("Work Hours");
		lblRolesTitleNumID.setPrefWidth(COL_SIZE_ID);
		lblRolesTitleRoleName.setPrefWidth(COL_SIZE_NAME);
		lblRolesTitleDepType.setPrefWidth(COL_SIZE_TYPE);
		lblRolesTitleRoleEmployee.setPrefWidth(COL_SIZE_EMPLOYEE);
		lblRolesTitleWorkType.setPrefWidth(COL_SIZE_WORK_TYPE);
		lblRolesTitleWorkHours.setPrefWidth(COL_SIZE_WORK_HOURS);
		hbRoleHeadline.getChildren().addAll(lblRolesTitleNumID, lblRolesTitleRoleName, lblRolesTitleDepType,
				lblRolesTitleRoleEmployee, lblRolesTitleWorkType, lblRolesTitleWorkHours);

		ListView<HBox> lstRoles = new ListView<HBox>();
		VBox vbDepatments = new VBox(lstRoles);
		Set<Role> allRoles = activeDepartment.getRoles();
		if (!allRoles.isEmpty()) {
			for (Role R : allRoles) {
				GridPane gpRoleDetails = new GridPane();
				GridPane gpRoleButtons = new GridPane();
				gpRoleButtons.setHgap(SPACE_BETWEEN_BUTTONS);

				Label lblRoleNumID = new Label(R.getNumID() + "");
				Label lblRoleName = new Label(R.getName());
				Label lblRoleType = new Label(R.getRoleType().toString());
				Label lblRoleEmployee = new Label(R.isOccupied() ? R.getEmployee().getName() : "No Employee");
				Label lblRoleWorkType = new Label(R.getWorkHoursActual().getWorkType().toString());
				Label lblRoleWorkHours = new Label(R.getWorkHoursActual().toString());

				lblRoleNumID.setPrefWidth(COL_SIZE_ID);
				lblRoleName.setPrefWidth(COL_SIZE_NAME);
				lblRoleType.setPrefWidth(COL_SIZE_TYPE);
				lblRoleEmployee.setPrefWidth(COL_SIZE_EMPLOYEE);
				lblRoleWorkType.setPrefWidth(COL_SIZE_WORK_TYPE);
				lblRoleWorkHours.setPrefWidth(COL_SIZE_WORK_HOURS);

				gpRoleDetails.addRow(0, lblRoleNumID, lblRoleName, lblRoleType, lblRoleEmployee, lblRoleWorkType,
						lblRoleWorkHours);

				Tooltip tooltipEmployee = new Tooltip(
						R.isOccupied() ? R.getEmployee().toString() : "No employee at this role");
				tooltipEmployee.setShowDelay(new Duration(MainCompanyView.TOOLTIP_SHOW_DEALAY_MILISEC));
				lblRoleEmployee.setTooltip(tooltipEmployee);
				Button btnRoleEdit = new Button("Edit");
				Button btnRoleRemove = new Button("Remove");
				setEditEntityButtonDesign(btnRoleEdit);
				setRemoveEntityButtonDesign(btnRoleRemove);
				gpRoleButtons.addRow(0, btnRoleEdit, btnRoleRemove);

				HBox hbRoles = new HBox(SPACE_BETWEEN_DETAILS_TO_BUTTONS);
				hbRoles.getChildren().addAll(gpRoleDetails, gpRoleButtons);
				lstRoles.getItems().add(hbRoles);

				btnRoleEdit.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent ae) {
						mainView.changePaneToPresent(R, activeDepartment);
					}
				});

				btnRoleRemove.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent ae) {
						for (ViewListenable l : mainView.allListeners) {
							try {
								l.viewRemoveRole(activeDepartment, R);
								mainView.setSideMenuPane();
								mainView.changePaneToPresent(activeDepartment, null);
							} catch (EntityNotFoundException e) {
								mainView.msgPopupError("Remove role", e.getMessage());
							}
						}
					}
				});
			}
		}

		vbMngDepartment.getChildren().addAll(hbDepTitle, gpDepData, hbDepButtons, hbRoleHeadline,
				new Separator(Orientation.HORIZONTAL), vbDepatments);

		return vbMngDepartment;
	}

// <==================================>
// <= Build Pane: Add New department =>
// ===================================>
	public GridPane addSetDepartmentFromUser(ViewListenable l, Department dep) {
		GridPane gpAddDepartment = new GridPane();
		gpAddDepartment.setPadding(new Insets(10));
		gpAddDepartment.setHgap(10);
		gpAddDepartment.setVgap(10);

		gpAddDepartment.add(new Label("Department Name: "), 0, 0);
		TextField tfName = new TextField();
		gpAddDepartment.add(tfName, 1, 0);

		gpAddDepartment.add(new Label("Department Type: "), 0, 1);
		ComboBox<eDepartmentsTypes> cmbDepartmentTypes = new ComboBox<eDepartmentsTypes>();
		cmbDepartmentTypes.getItems().addAll(eDepartmentsTypes.values());
		gpAddDepartment.add(cmbDepartmentTypes, 1, 1);

		Label lblWorkType = new Label("Department Work Type: ");
		gpAddDepartment.add(lblWorkType, 0, 2);
		ComboBox<eWorkTypes> cmbWorkTypes = new ComboBox<eWorkTypes>();
		cmbWorkTypes.getItems().addAll(eWorkTypes.values());
		cmbWorkTypes.setValue(eWorkTypes.Regular);
		gpAddDepartment.add(cmbWorkTypes, 1, 2);

		Label lblStartHour = new Label("Department Start Hour: ");
		gpAddDepartment.add(lblStartHour, 0, 3);
		ComboBox<LocalTime> cmbHours = new ComboBox<LocalTime>();
		gpAddDepartment.add(cmbHours, 1, 3);

		lblWorkType.setVisible(false);
		cmbWorkTypes.setVisible(false);
		lblStartHour.setVisible(false);
		cmbHours.setVisible(false);

		VBox vbAllEmployees = new VBox();
		vbAllEmployees.setVisible(false);

		if (dep != null) {
			tfName.setDisable(true);
			cmbDepartmentTypes.setDisable(true);
			tfName.setText(dep.getName());
			cmbDepartmentTypes.setValue(dep.getDepartmentType());
			if (dep.getDepartmentType() == eDepartmentsTypes.Synchronized) {
				cmbWorkTypes.setValue(dep.getWorkHoursActual().getWorkType());
				lblWorkType.setVisible(true);
				cmbWorkTypes.setVisible(true);
				lblStartHour.setVisible(false);
				cmbHours.setVisible(false);
				cmbHours.getItems().clear();
				switch (cmbWorkTypes.getValue()) {
				case Early:
					for (int i = LocalTime.MIDNIGHT.getHour(); i < CompanyModel.REGULAR_START_HOUR.getHour(); i++) {
						cmbHours.getItems().add(LocalTime.of(i, 0));
					}
					cmbHours.setValue(dep.getWorkHoursActual().getStartHour());
					;
					lblStartHour.setVisible(true);
					cmbHours.setVisible(true);
					break;
				case Later:
					for (int i = CompanyModel.REGULAR_START_HOUR.getHour() + 1; i < LocalTime.MIDNIGHT
							.minusHours(CompanyModel.WORKING_HOURS_IN_DAY).getHour(); i++) {
						cmbHours.getItems().add(LocalTime.of(i, 0));
					}
					cmbHours.setValue(dep.getWorkHoursActual().getStartHour());
					lblStartHour.setVisible(true);
					cmbHours.setVisible(true);
				default:
					break;
				}

				final double COL_SIZE_ID = 50;
				final double COL_SIZE_NAME = 100;
				final double COL_SIZE_WORK_HOURS = 80;

				ListView<HBox> lstRoles = new ListView<HBox>();
				VBox vbEmployees = new VBox(lstRoles);
				HBox hbEmpHeadline = new HBox();
				Label lblEmpTitleNumID = new Label("ID");
				Label lblEmpTitleRoleName = new Label("Name");
				Label lblEmpTitleWorkHours = new Label("Work Hours");
				lblEmpTitleNumID.setPrefWidth(COL_SIZE_ID);
				lblEmpTitleRoleName.setPrefWidth(COL_SIZE_NAME);
				lblEmpTitleWorkHours.setPrefWidth(COL_SIZE_WORK_HOURS);
				hbEmpHeadline.getChildren().addAll(lblEmpTitleNumID, lblEmpTitleRoleName, lblEmpTitleWorkHours);

				Set<Role> allRoles = dep.getRoles();
				if (!allRoles.isEmpty()) {
					for (Role R : allRoles) {
						if (R.getEmployee() != null) {
							GridPane gpEmpDetails = new GridPane();

							Label lblEmpNumID = new Label(R.getEmployee().getNumID() + "");
							Label lblEmpName = new Label(R.getEmployee().getName());
							Label lblEmpWorkHours = new Label(R.getEmployee().getWorkHoursPreferred().toString());

							lblEmpNumID.setPrefWidth(COL_SIZE_ID);
							lblEmpName.setPrefWidth(COL_SIZE_NAME);
							lblEmpWorkHours.setPrefWidth(COL_SIZE_WORK_HOURS);

							gpEmpDetails.addRow(0, lblEmpNumID, lblEmpName, lblEmpWorkHours);
							HBox hbEmployees = new HBox();
							hbEmployees.getChildren().add(gpEmpDetails);
							lstRoles.getItems().add(hbEmployees);
						}
					}
					vbAllEmployees.getChildren().addAll(hbEmpHeadline, new Separator(Orientation.HORIZONTAL),
							vbEmployees);
					vbAllEmployees.setVisible(true);
				}
			}
		}
		cmbDepartmentTypes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				lblWorkType.setVisible(false);
				cmbWorkTypes.setVisible(false);
				lblStartHour.setVisible(false);
				cmbHours.setVisible(false);
				if (cmbDepartmentTypes.getValue() == eDepartmentsTypes.Synchronized) {
					lblWorkType.setVisible(true);
					cmbWorkTypes.setVisible(true);
				} else {
					cmbWorkTypes.setValue(null);
					cmbHours.setValue(null);
				}
			}
		});
		cmbWorkTypes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				lblStartHour.setVisible(false);
				cmbHours.setVisible(false);
				cmbHours.getItems().clear();
				if (cmbWorkTypes.getValue() != null) {
					switch (cmbWorkTypes.getValue()) {
					case Early:
						for (int i = LocalTime.MIDNIGHT.getHour(); i < CompanyModel.REGULAR_START_HOUR.getHour(); i++) {
							cmbHours.getItems().add(LocalTime.of(i, 0));
						}
						lblStartHour.setVisible(true);
						cmbHours.setVisible(true);
						break;
					case Later:
						for (int i = CompanyModel.REGULAR_START_HOUR.getHour() + 1; i < LocalTime.MIDNIGHT
								.minusHours(CompanyModel.WORKING_HOURS_IN_DAY).getHour(); i++) {
							cmbHours.getItems().add(LocalTime.of(i, 0));
						}
						lblStartHour.setVisible(true);
						cmbHours.setVisible(true);
					default:
						break;
					}
				}
			}
		});

		Button btnConfirm = new Button((dep == null) ? "Add Department" : "Save Department");
		setSubMenuButtonDesign(btnConfirm);
		gpAddDepartment.add(btnConfirm, 0, 4);
		gpAddDepartment.add(vbAllEmployees, 0, 5);

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (tfName.getText().isEmpty() || cmbDepartmentTypes.getValue() == null
						|| cmbDepartmentTypes.getValue() == eDepartmentsTypes.Synchronized
								&& cmbWorkTypes.getValue() == null
						|| ((cmbWorkTypes.getValue() == eWorkTypes.Later || cmbWorkTypes.getValue() == eWorkTypes.Early)
								&& cmbHours.getValue() == null)) {
					mainView.msgPopupError("Add/Save Department", "All fields must be filled/selected.");
				} else {
					for (ViewListenable l : mainView.allListeners) {
						try {
							WorkHours theWH = null;
							if (cmbDepartmentTypes.getValue() == eDepartmentsTypes.Synchronized) {
								if ((cmbWorkTypes.getValue() == eWorkTypes.Later
										|| cmbWorkTypes.getValue() == eWorkTypes.Early)) {
									theWH = new WorkHours(cmbWorkTypes.getValue(), cmbHours.getValue());
								} else {
									theWH = new WorkHours(cmbWorkTypes.getValue(), null);
								}
							}
							if (dep != null) {
								l.viewSetDepartment(dep, tfName.getText(), cmbDepartmentTypes.getValue(), theWH);
							} else {
								l.viewAddDepartment(tfName.getText(), cmbDepartmentTypes.getValue(), theWH);
							}
							mainView.setSideMenuPane();
							mainView.setMngCompanyPane();
						} catch (EntityAlreadyExistsException | WorkHoursTimeException
								| RoleDepartmentTypesMismatchException e) {
							mainView.msgPopupError("Add/Save department", e.getMessage());
						}
					}
				}
			}
		});
		return gpAddDepartment;
	}

// <============================>
// <= Build Pane: Add New Role =>
// =============================>
	public GridPane addSetRoleFromUser(ViewListenable l, Role role, Department theDep) {
		GridPane gpAddRole = new GridPane();
		gpAddRole.setPadding(new Insets(10));
		gpAddRole.setHgap(10);
		gpAddRole.setVgap(10);

		Label lblStartHour = new Label("Role Start Hour: ");
		TextField tfName = new TextField();

		ComboBox<eRolesTypes> cmbRoleTypes = new ComboBox<eRolesTypes>();
		cmbRoleTypes.getItems().addAll(eRolesTypes.values());

		ComboBox<Employee> cmbEmployees = new ComboBox<Employee>();
		cmbEmployees.getItems().addAll(l.viewGetEmployees());

		Label lblEmpPrefTitle = new Label("Employee Preference: ");
		Label lblEmpPref = new Label();
		lblEmpPref.setTextFill(Color.DARKRED);
		lblEmpPrefTitle.setTextFill(Color.DARKRED);
		lblEmpPrefTitle.setVisible(false);
		lblEmpPref.setVisible(false);

		ComboBox<eWorkTypes> cmbWorkTypes = new ComboBox<eWorkTypes>();
		cmbWorkTypes.getItems().addAll(eWorkTypes.values());
		cmbWorkTypes.setValue(eWorkTypes.Regular);

		ComboBox<LocalTime> cmbHours = new ComboBox<LocalTime>();
		lblStartHour.setVisible(false);
		cmbHours.setVisible(false);

		if (theDep.isHoursSynchronizable()) {
			cmbWorkTypes.setDisable(true);
			cmbHours.setDisable(true);
			cmbWorkTypes.setValue(theDep.getWorkHoursActual().getWorkType());
			cmbHours.setValue(theDep.getWorkHoursActual().getStartHour());
		}
		cmbEmployees.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				lblEmpPrefTitle.setVisible(false);
				lblEmpPref.setVisible(false);
				if (cmbEmployees.getValue() != null) {
					lblEmpPref.setText(cmbEmployees.getValue().getWorkHoursPreferred().toString());
					lblEmpPrefTitle.setVisible(true);
					lblEmpPref.setVisible(true);
				}
			}
		});

		if (role != null) {
			tfName.setText(role.getName());
			cmbRoleTypes.setValue(role.getRoleType());
			tfName.setDisable(true);
			cmbRoleTypes.setDisable(true);
			if (role.getEmployee() != null) {
				cmbEmployees.setValue(role.getEmployee());
			}
			if (cmbEmployees.getValue() != null) {
				lblEmpPref.setText(cmbEmployees.getValue().getWorkHoursPreferred().toString());
				lblEmpPrefTitle.setVisible(true);
				lblEmpPref.setVisible(true);
			}
			if (theDep.isHoursSynchronizable() || role.getRoleType() == eRolesTypes.Permanent) {
				cmbWorkTypes.setDisable(true);
				cmbHours.setDisable(true);
			}
			cmbWorkTypes.setValue(role.getWorkHoursActual().getWorkType());
			lblStartHour.setVisible(false);
			cmbHours.setVisible(false);
			cmbHours.getItems().clear();
			switch (cmbWorkTypes.getValue()) {
			case Early:
				for (int i = LocalTime.MIDNIGHT.getHour(); i < CompanyModel.REGULAR_START_HOUR.getHour(); i++) {
					cmbHours.getItems().add(LocalTime.of(i, 0));
				}
				cmbHours.setValue(role.getWorkHoursActual().getStartHour());
				lblStartHour.setVisible(true);
				cmbHours.setVisible(true);
				break;
			case Later:
				for (int i = CompanyModel.REGULAR_START_HOUR.getHour() + 1; i < LocalTime.MIDNIGHT
						.minusHours(CompanyModel.WORKING_HOURS_IN_DAY).getHour(); i++) {
					cmbHours.getItems().add(LocalTime.of(i, 0));
				}
				cmbHours.setValue(role.getWorkHoursActual().getStartHour());
				lblStartHour.setVisible(true);
				cmbHours.setVisible(true);
			default:
				break;
			}
		}
		cmbWorkTypes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				lblStartHour.setVisible(false);
				cmbHours.setVisible(false);
				cmbHours.getItems().clear();
				if (cmbWorkTypes.getValue() != null) {
					switch (cmbWorkTypes.getValue()) {
					case Early:
						for (int i = LocalTime.MIDNIGHT.getHour(); i < CompanyModel.REGULAR_START_HOUR.getHour(); i++) {
							cmbHours.getItems().add(LocalTime.of(i, 0));
						}
						lblStartHour.setVisible(true);
						cmbHours.setVisible(true);
						break;
					case Later:
						for (int i = CompanyModel.REGULAR_START_HOUR.getHour() + 1; i < LocalTime.MIDNIGHT
								.minusHours(CompanyModel.WORKING_HOURS_IN_DAY).getHour(); i++) {
							cmbHours.getItems().add(LocalTime.of(i, 0));
						}
						lblStartHour.setVisible(true);
						cmbHours.setVisible(true);
					default:
						break;
					}
				}
			}
		});

		Button btnConfirm = new Button((role == null) ? "Add Role" : "Save Role");
		setSubMenuButtonDesign(btnConfirm);

		gpAddRole.addColumn(0, new Label("Role Name: "), new Label("Role Type: "), new Label("Employee: "),
				new Label("Role Work Type: "), lblStartHour, btnConfirm);
		gpAddRole.addColumn(1, tfName, cmbRoleTypes, cmbEmployees, cmbWorkTypes, cmbHours);
		gpAddRole.add(lblEmpPrefTitle, 2, 2);
		gpAddRole.add(lblEmpPref, 3, 2);

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (tfName.getText().isEmpty() || cmbRoleTypes.getValue() == null || cmbWorkTypes.getValue() == null
						|| ((cmbWorkTypes.getValue() == eWorkTypes.Early && cmbHours.getValue() == null)
								|| (cmbWorkTypes.getValue() == eWorkTypes.Later && cmbHours.getValue() == null))) {
					mainView.msgPopupError("Add/Save Role", "All fields must be filled/selected.");
				} else {
					for (ViewListenable l : mainView.allListeners) {
						try {
							WorkHours theWH = null;
							theWH = new WorkHours(cmbWorkTypes.getValue(), cmbHours.getValue());
							if (role != null) {
								l.viewSetRole(role, tfName.getText(), theDep, cmbRoleTypes.getValue(), theWH,
										cmbEmployees.getValue());
							} else {
								l.viewAddRole(tfName.getText(), theDep, cmbRoleTypes.getValue(), theWH,
										cmbEmployees.getValue());
							}
							mainView.setSideMenuPane();
							mainView.setMngDepartmentPane(theDep);
						} catch (WorkHoursTimeException | EntityAlreadyExistsException
								| RoleDepartmentTypesMismatchException e) {
							mainView.msgPopupError("Add/Save Role", e.getMessage());
						}
					}
				}
			}
		});

		return gpAddRole;
	}

// <================================>
// <= Build Pane: Add New Employee =>
// =================================>
	public GridPane addSetEmplpoyeeFromUser(ViewListenable l, Employee emp) {
		GridPane gpAddEmployee = new GridPane();
		gpAddEmployee.setPadding(new Insets(10));
		gpAddEmployee.setHgap(10);
		gpAddEmployee.setVgap(10);

		Label lblStartHour = new Label("Preferred Start Hour: ");
		Label lblRole = new Label("Role: ");
		Label lblRolePrefTitle = new Label("Role Hours: ");
		Label lblRolePref = new Label();
		Label lblWorkHourCnt = new Label("Update hours count: ");
		Label lblMonthSalePerc = new Label("Update Monthly Sales (%): ");
		lblRole.setVisible(false);
		lblRolePref.setTextFill(Color.DARKRED);
		lblRolePrefTitle.setTextFill(Color.DARKRED);
		lblRolePrefTitle.setVisible(false);
		lblRolePref.setVisible(false);
		lblWorkHourCnt.setVisible(false);
		lblMonthSalePerc.setVisible(false);
		Label lblEmpRole = new Label();
		TextField tfName = new TextField();

		ComboBox<eEmployeesTypes> cmbEmpTypes = new ComboBox<eEmployeesTypes>();
		cmbEmpTypes.getItems().addAll(eEmployeesTypes.values());

		ComboBox<Integer> cmbSalaryPerHour = new ComboBox<Integer>();
		for (int i = 30; i <= 300; i += 5) {
			cmbSalaryPerHour.getItems().add(i);
		}

		ComboBox<eWorkTypes> cmbWorkTypes = new ComboBox<eWorkTypes>();
		cmbWorkTypes.getItems().addAll(eWorkTypes.values());
		cmbWorkTypes.setValue(eWorkTypes.Regular);

		ComboBox<LocalTime> cmbHours = new ComboBox<LocalTime>();
		lblStartHour.setVisible(false);
		cmbHours.setVisible(false);

		ComboBox<Integer> cmbMonthWorkHoursCnt = new ComboBox<Integer>();
		for (int i = 0; i <= CompanyModel.WORKING_HOURS_IN_DAY * 31; i += CompanyModel.WORKING_HOURS_IN_DAY) {
			cmbMonthWorkHoursCnt.getItems().add(i);
		}
		cmbMonthWorkHoursCnt.setVisible(false);
		cmbMonthWorkHoursCnt.setValue(0);

		ComboBox<Integer> cmbMonthSalesPerc = new ComboBox<Integer>();
		for (int i = 0; i <= 100; i += 5) {
			cmbMonthSalesPerc.getItems().add(i);
		}
		cmbMonthSalesPerc.setVisible(false);
		cmbMonthSalesPerc.setValue(0);

		if (emp != null) {
			tfName.setText(emp.getName());
			cmbEmpTypes.setValue(emp.getEmployeeType());
			cmbSalaryPerHour.setValue(emp.getSalaryPerHour());
			cmbWorkTypes.setValue(emp.getWorkHoursPreferred().getWorkType());
			lblEmpRole.setText(emp.getRole() != null ? emp.getRole().toString() : "No Role");
			if (emp.getRole() != null) {
				lblRolePref.setText(emp.getRole().getWorkHoursActual().toString());
				lblRolePrefTitle.setVisible(true);
				lblRolePref.setVisible(true);
			}
			cmbMonthWorkHoursCnt.setValue(emp.getMonthWorkHoursCnt());
			cmbEmpTypes.setDisable(true);
			lblStartHour.setVisible(false);
			cmbHours.setVisible(false);
			cmbHours.getItems().clear();
			lblRole.setVisible(true);
			lblWorkHourCnt.setVisible(true);
			cmbMonthWorkHoursCnt.setVisible(true);
			if (emp.getEmployeeType() == eEmployeesTypes.BaseSalary) {
				cmbMonthWorkHoursCnt.setDisable(true);
			}
			if (emp.getEmployeeType() == eEmployeesTypes.BaseBonusSalary) {
				cmbMonthSalesPerc.setValue(((EmployeeBonusSalary) emp).getSalesPerc());
				cmbMonthSalesPerc.setVisible(true);
				lblMonthSalePerc.setVisible(true);
				cmbMonthWorkHoursCnt.setDisable(true);
			}
			switch (cmbWorkTypes.getValue()) {
			case Early:
				for (int i = LocalTime.MIDNIGHT.getHour(); i < CompanyModel.REGULAR_START_HOUR.getHour(); i++) {
					cmbHours.getItems().add(LocalTime.of(i, 0));
				}
				cmbHours.setValue(emp.getWorkHoursPreferred().getStartHour());
				lblStartHour.setVisible(true);
				cmbHours.setVisible(true);
				break;
			case Later:
				for (int i = CompanyModel.REGULAR_START_HOUR.getHour() + 1; i < LocalTime.MIDNIGHT
						.minusHours(CompanyModel.WORKING_HOURS_IN_DAY).getHour(); i++) {
					cmbHours.getItems().add(LocalTime.of(i, 0));
				}
				cmbHours.setValue(emp.getWorkHoursPreferred().getStartHour());
				lblStartHour.setVisible(true);
				cmbHours.setVisible(true);
			default:
				break;
			}
		}
		cmbWorkTypes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				lblStartHour.setVisible(false);
				cmbHours.setVisible(false);
				cmbHours.getItems().clear();
				if (cmbWorkTypes.getValue() != null) {
					switch (cmbWorkTypes.getValue()) {
					case Early:
						for (int i = LocalTime.MIDNIGHT.getHour(); i < CompanyModel.REGULAR_START_HOUR.getHour(); i++) {
							cmbHours.getItems().add(LocalTime.of(i, 0));
						}
						lblStartHour.setVisible(true);
						cmbHours.setVisible(true);
						break;
					case Later:
						for (int i = CompanyModel.REGULAR_START_HOUR.getHour() + 1; i < LocalTime.MIDNIGHT
								.minusHours(CompanyModel.WORKING_HOURS_IN_DAY).getHour(); i++) {
							cmbHours.getItems().add(LocalTime.of(i, 0));
						}
						lblStartHour.setVisible(true);
						cmbHours.setVisible(true);
					default:
						break;
					}
				}
			}
		});
		Button btnConfirm = new Button((emp == null) ? "Add Employee" : "Save Employee");
		setSubMenuButtonDesign(btnConfirm);

		gpAddEmployee.addColumn(0, new Label("Employee Name: "), new Label("Employee Type: "),
				new Label("Salary Per Hour: "), new Label("Preferred Work Type: "), lblStartHour, lblRole,
				lblWorkHourCnt, lblMonthSalePerc, btnConfirm);
		gpAddEmployee.addColumn(1, tfName, cmbEmpTypes, cmbSalaryPerHour, cmbWorkTypes, cmbHours, lblEmpRole,
				cmbMonthWorkHoursCnt, cmbMonthSalesPerc);
		gpAddEmployee.add(lblRolePrefTitle, 3, 5);
		gpAddEmployee.add(lblRolePref, 4, 5);

		btnConfirm.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (tfName.getText().isEmpty() || cmbEmpTypes.getValue() == null || cmbSalaryPerHour.getValue() == null
						|| cmbWorkTypes.getValue() == null
						|| ((cmbWorkTypes.getValue() == eWorkTypes.Early && cmbHours.getValue() == null)
								|| (cmbWorkTypes.getValue() == eWorkTypes.Later && cmbHours.getValue() == null))) {
					mainView.msgPopupError("Add/Save Employee", "All fields must be filled/selected.");
				} else {
					for (ViewListenable listener : mainView.allListeners) {
						try {
							WorkHours theWH = null;
							theWH = new WorkHours(cmbWorkTypes.getValue(), cmbHours.getValue());
							if (emp != null) {
								listener.setEmployee(emp, tfName.getText(), cmbEmpTypes.getValue(),
										cmbSalaryPerHour.getValue(), theWH, cmbMonthWorkHoursCnt.getValue(),
										cmbMonthSalesPerc.getValue());
							} else {
								listener.viewAddEmployee(tfName.getText(), cmbEmpTypes.getValue(),
										cmbSalaryPerHour.getValue(), theWH);
							}
							mainView.setSideMenuPane();
							mainView.setMngEmployeesPane();
						} catch (EntityAlreadyExistsException | WorkHoursTimeException e) {
							mainView.msgPopupError("Add/Save Employee", e.getMessage());
						}
					}
				}
			}
		});
		return gpAddEmployee;
	}
}
