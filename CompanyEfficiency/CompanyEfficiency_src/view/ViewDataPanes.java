package view;

import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import listeners.*;
import model.*;

public class ViewDataPanes {
// <============================================>
// <= Build Pane: Company All Departments Data =>
// =============================================>
	public VBox setDataCompanyModelPane(ViewListenable l) {
		double maxDepRolesCnt = 0;
		for (Department theDep : l.viewGetDepartments()) {
			if (theDep.getNumOfRoles() > maxDepRolesCnt) {
				maxDepRolesCnt = theDep.getNumOfRoles();
			}
		}
		
		final double barLimitCompany = l.viewGetWorkingEmployees().size() * CompanyModel.EFFICIENCY
				* CompanyModel.WORKING_HOURS_IN_DAY;
		final NumberAxis xAxisCompany = new NumberAxis(-(barLimitCompany), barLimitCompany, 1);
		final CategoryAxis yAxisCompany = new CategoryAxis();
		final BarChart<Number, String> bcCompanyEfficiency = new BarChart<Number, String>(xAxisCompany, yAxisCompany);

		final double barLimitDepartments = maxDepRolesCnt * CompanyModel.EFFICIENCY * CompanyModel.WORKING_HOURS_IN_DAY;
		final CategoryAxis xAxisDepartments = new CategoryAxis();
		final NumberAxis yAxisDepartments = new NumberAxis(-(barLimitDepartments), barLimitDepartments, 1);
		final BarChart<String, Number> bcDepartmentsEfficiency = new BarChart<String, Number>(xAxisDepartments,
				yAxisDepartments);

		VBox vbDataCompanyModel = new VBox(5);
		vbDataCompanyModel.setPadding(new Insets(10));

		Label lblHeadLineCompanyName = new Label(l.viewGetCompanyName() + " Departments");
		lblHeadLineCompanyName.setFont(MainCompanyView.FONT_HEAD_LINE);
		HBox hbCompanyTitle = new HBox(10, lblHeadLineCompanyName);
		hbCompanyTitle.setAlignment(Pos.CENTER);

		bcCompanyEfficiency.setTitle("Efficiency");
		xAxisCompany.setLabel("Extra Efficiency");

		XYChart.Series<Number, String> dsCompanyData = new Series<Number, String>();
		dsCompanyData.getData().add(new XYChart.Data<Number, String>(l.viewCalcEfficiencyCompany(), ""));

		bcCompanyEfficiency.getData().addAll(dsCompanyData);
		bcCompanyEfficiency.setMaxHeight(10);

		if ((double) dsCompanyData.getData().get(0).getXValue() > 0) {
			dsCompanyData.getData().get(0).getNode().setStyle("-fx-bar-fill: green;");
		} else {
			dsCompanyData.getData().get(0).getNode().setStyle("-fx-bar-fill: red;");
		}

		bcDepartmentsEfficiency.setTitle("Department Efficiency");
		xAxisDepartments.setLabel("Department name");
		yAxisDepartments.setLabel("Efficient value");

		XYChart.Series<String, Number> dsDepartmentsData = new Series<String, Number>();

		for (Department theDep : l.viewGetDepartments()) {
			dsDepartmentsData.getData().add(new Data<String, Number>(theDep.getName(), theDep.calcEfficiency()));
		}

		bcDepartmentsEfficiency.getData().addAll(dsDepartmentsData);

		double efficiency = l.viewCalcEfficiencyCompany();
		double efficiencyProfit = l.viewCalcEfficiencyProfitCompany();

		Label lblEfficiency = new Label(String.format("%,.1f", efficiency) + " [hour]");
		Label lblEfficiencyProfit = new Label(String.format("%,.1f", efficiencyProfit) + " [MoneyUnit]");
		HBox hbEfficiencyResult = new HBox(10);
		hbEfficiencyResult.setAlignment(Pos.CENTER);
		
		if (efficiency < 0) {
			lblEfficiency.setTextFill(Color.RED);
			lblEfficiencyProfit.setTextFill(Color.RED);

		} else {
			lblEfficiency.setTextFill(Color.GREEN);
			lblEfficiencyProfit.setTextFill(Color.RED);
		}
		
		hbEfficiencyResult.getChildren().addAll(new Label("Extra Efficiency: "), lblEfficiency, new Label(","),  lblEfficiencyProfit);

		vbDataCompanyModel.getChildren().addAll(hbCompanyTitle, bcCompanyEfficiency, hbEfficiencyResult,
				bcDepartmentsEfficiency);
		return vbDataCompanyModel;
	}

// <==========================================>
// <= Build Pane: Company All Employees Data =>
// ===========================================>
	public VBox setDataCompanyEmployeesPane(ViewListenable l) {
		final double barLimitCompany = l.viewGetWorkingEmployees().size() * CompanyModel.EFFICIENCY
				* CompanyModel.WORKING_HOURS_IN_DAY;
		final double barLimitEmployees = CompanyModel.EFFICIENCY * CompanyModel.WORKING_HOURS_IN_DAY;
		final NumberAxis xAxisCompany = new NumberAxis(-(barLimitCompany), barLimitCompany, 1);
		final CategoryAxis yAxisCompany = new CategoryAxis();
		final BarChart<Number, String> bcCompanyEfficiency = new BarChart<Number, String>(xAxisCompany, yAxisCompany);
		final CategoryAxis xAxisEmployees = new CategoryAxis();
		final NumberAxis yAxisEmployees = new NumberAxis(-(barLimitEmployees), barLimitEmployees,
				CompanyModel.EFFICIENCY);
		final BarChart<String, Number> bcEmployeesEfficiency = new BarChart<String, Number>(xAxisEmployees,
				yAxisEmployees);

		VBox vbDataCompanyEmployees = new VBox(5);
		vbDataCompanyEmployees.setPadding(new Insets(10));

		Label lblHeadLineCompanyName = new Label(l.viewGetCompanyName() + " Employees");
		lblHeadLineCompanyName.setFont(MainCompanyView.FONT_HEAD_LINE);
		HBox hbCompanyTitle = new HBox(10, lblHeadLineCompanyName);
		hbCompanyTitle.setAlignment(Pos.CENTER);

		bcCompanyEfficiency.setTitle("Efficiency");
		xAxisCompany.setLabel("Extra Efficiency");

		XYChart.Series<Number, String> dsCompanyData = new Series<Number, String>();
		dsCompanyData.getData().add(new XYChart.Data<Number, String>(l.viewCalcEfficiencyCompany(), ""));

		bcCompanyEfficiency.getData().addAll(dsCompanyData);
		bcCompanyEfficiency.setMaxHeight(10);

		if ((double) dsCompanyData.getData().get(0).getXValue() > 0) {
			dsCompanyData.getData().get(0).getNode().setStyle("-fx-bar-fill: green;");
		} else {
			dsCompanyData.getData().get(0).getNode().setStyle("-fx-bar-fill: red;");
		}

		bcEmployeesEfficiency.setTitle("Employees Efficiency");
		xAxisEmployees.setLabel("Employee name");
		yAxisEmployees.setLabel("Efficient value");

		XYChart.Series<String, Number> dsEmployeesData = new Series<String, Number>();

		for (Employee theEmp : l.viewGetWorkingEmployees()) {
			if (theEmp.getRole() != null) {
				dsEmployeesData.getData()
						.add(new Data<String, Number>(theEmp.getName(), theEmp.getRole().calcEfficiency()));
			}
		}

		bcEmployeesEfficiency.getData().addAll(dsEmployeesData);

		double efficiency = l.viewCalcEfficiencyCompany();
		double efficiencyProfit = l.viewCalcEfficiencyProfitCompany();

		Label lblEfficiency = new Label(String.format("%,.1f", efficiency) + " [hour]");
		Label lblEfficiencyProfit = new Label(String.format("%,.1f", efficiencyProfit) + " [MoneyUnit]");
		HBox hbEfficiencyResult = new HBox(10);
		hbEfficiencyResult.setAlignment(Pos.CENTER);
		
		if (efficiency < 0) {
			lblEfficiency.setTextFill(Color.RED);
			lblEfficiencyProfit.setTextFill(Color.RED);

		} else {
			lblEfficiency.setTextFill(Color.GREEN);
			lblEfficiencyProfit.setTextFill(Color.RED);
		}
		
		hbEfficiencyResult.getChildren().addAll(new Label("Extra Efficiency: "), lblEfficiency, new Label(","),  lblEfficiencyProfit);

		vbDataCompanyEmployees.getChildren().addAll(hbCompanyTitle, bcCompanyEfficiency, hbEfficiencyResult,
				bcEmployeesEfficiency);
		return vbDataCompanyEmployees;
	}

// <===============================>
// <= Build Pane: Department Data =>
// ================================>
	public VBox setDataDepartmentPane(ViewListenable l, Department activeDepartment) {
		final double barLimit = CompanyModel.EFFICIENCY * CompanyModel.WORKING_HOURS_IN_DAY;
		
		final NumberAxis xAxis = new NumberAxis(-(barLimit), barLimit, 1);
		final CategoryAxis yAxis = new CategoryAxis();
		final BarChart<Number, String> bcEfficiency = new BarChart<Number, String>(xAxis, yAxis);
		
		final CategoryAxis xAxisRoles = new CategoryAxis();
		final NumberAxis yAxisRoles = new NumberAxis(-(barLimit), barLimit, 1);
		final BarChart<String, Number> bcRolesEfficiency = new BarChart<String, Number>(xAxisRoles,
				yAxisRoles);

		VBox vbDataDepartment = new VBox(5);
		vbDataDepartment.setPadding(new Insets(10));

		Label lblHeadLineDepName = new Label(activeDepartment.getName());
		lblHeadLineDepName.setFont(MainCompanyView.FONT_HEAD_LINE);
		HBox hbCompanyTitle = new HBox(10, lblHeadLineDepName);
		hbCompanyTitle.setAlignment(Pos.CENTER);

		bcEfficiency.setTitle("Efficiency");
		xAxis.setLabel("Extra Efficiency");

		XYChart.Series<Number, String> dsEfficiencyDaya = new Series<Number, String>();
		dsEfficiencyDaya.getData()
				.add(new XYChart.Data<Number, String>(l.viewCalcEfficiencyDepartment(activeDepartment), ""));

		bcEfficiency.getData().addAll(dsEfficiencyDaya);
		bcEfficiency.setMaxHeight(10);

		if ((double) dsEfficiencyDaya.getData().get(0).getXValue() > 0) {
			dsEfficiencyDaya.getData().get(0).getNode().setStyle("-fx-bar-fill: green;");
		} else {
			dsEfficiencyDaya.getData().get(0).getNode().setStyle("-fx-bar-fill: red;");
		}

		GridPane gpDepEfficiencyData = new GridPane();
		gpDepEfficiencyData.setPadding(new Insets(10));
		gpDepEfficiencyData.setHgap(50);
		gpDepEfficiencyData.setVgap(5);
		Label lblTitleDepID = new Label("ID");
		Label lblTitleDepName = new Label("Name");
		Label lblTitleDepType = new Label("Type");
		Label lblTitleEfficiency = new Label("Efficiency status");
		Label lblTitleEfficiencyProfit = new Label("Efficiency Profit");
		Label lblDepID = new Label(activeDepartment.getNumID() + "");
		Label lblDepName = new Label(activeDepartment.getName());
		Label lblDepType = new Label(activeDepartment.getDepartmentType().toString());
		double efficiency = l.viewCalcEfficiencyDepartment(activeDepartment);
		double efficiencyProfit = l.viewCalcEfficiencyProfitDepartment(activeDepartment);
		Label lblEfficiency = new Label(String.format("%,.1f", efficiency) + " [hour]");
		Label lblEfficiencyProfit = new Label(String.format("%,.1f", efficiencyProfit) + " [MoneyUnit]");
		if (efficiency < 0) {
			lblEfficiency.setTextFill(Color.RED);
			lblEfficiencyProfit.setTextFill(Color.RED);
		} else {
			lblEfficiency.setTextFill(Color.GREEN);
			lblEfficiencyProfit.setTextFill(Color.GREEN);
		}
		gpDepEfficiencyData.addRow(0, lblTitleDepID, lblTitleDepName, lblTitleDepType, lblTitleEfficiency, lblTitleEfficiencyProfit);
		gpDepEfficiencyData.addRow(1, new Separator(), new Separator(), new Separator(), new Separator(), new Separator());
		gpDepEfficiencyData.addRow(2, lblDepID, lblDepName, lblDepType, lblEfficiency, lblEfficiencyProfit);
		gpDepEfficiencyData.setAlignment(Pos.CENTER);
		gpDepEfficiencyData.setBorder(new Border(
				new BorderStroke(Color.SILVER, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		bcRolesEfficiency.setTitle("Role Efficiency");
		xAxisRoles.setLabel("Role name");
		yAxisRoles.setLabel("Efficient value");

		XYChart.Series<String, Number> dsRolesData = new Series<String, Number>();

		for (Role R : activeDepartment.getRoles()) {
			dsRolesData.getData().add(new Data<String, Number>(R.getName(), R.calcEfficiency()));
		}

		bcRolesEfficiency.getData().addAll(dsRolesData);

		
		vbDataDepartment.getChildren().addAll(hbCompanyTitle, bcEfficiency, gpDepEfficiencyData, bcRolesEfficiency);
		return vbDataDepartment;
	}

// <=============================>
// <= Build Pane: Employee Data =>
// ==============================>
	public VBox setDataEmployeePane(ViewListenable l, Employee emp) {
		final double barLimit = CompanyModel.EFFICIENCY * CompanyModel.WORKING_HOURS_IN_DAY;
		final NumberAxis xAxis = new NumberAxis(-(barLimit), barLimit, CompanyModel.EFFICIENCY);
		final CategoryAxis yAxis = new CategoryAxis();
		final BarChart<Number, String> bcEfficiency = new BarChart<Number, String>(xAxis, yAxis);

		VBox vbDataEmployee = new VBox(5);
		vbDataEmployee.setPadding(new Insets(10));

		Label lblHeadLineDepName = new Label(emp + " - " + emp.getRole() + " Efficiency");
		lblHeadLineDepName.setFont(MainCompanyView.FONT_HEAD_LINE);
		HBox hbCompanyTitle = new HBox(10, lblHeadLineDepName);
		hbCompanyTitle.setAlignment(Pos.CENTER);

		bcEfficiency.setTitle("Efficiency");
		xAxis.setLabel("Extra Efficiency");

		XYChart.Series<Number, String> dsEfficiencyDaya = new Series<Number, String>();
		dsEfficiencyDaya.getData().add(new XYChart.Data<Number, String>(l.viewCalcEfficiencyEmployee(emp), ""));

		bcEfficiency.getData().addAll(dsEfficiencyDaya);
		bcEfficiency.setMaxHeight(10);

		if ((double) dsEfficiencyDaya.getData().get(0).getXValue() > 0) {
			dsEfficiencyDaya.getData().get(0).getNode().setStyle("-fx-bar-fill: green;");
		} else {
			dsEfficiencyDaya.getData().get(0).getNode().setStyle("-fx-bar-fill: red;");
		}

		GridPane gpEmpEfficiencyData = new GridPane();
		gpEmpEfficiencyData.setPadding(new Insets(10));
		gpEmpEfficiencyData.setHgap(50);
		gpEmpEfficiencyData.setVgap(5);
		Label lblTitleEmpPref = new Label("Employee work hours Prefered");
		Label lblTitleRoleWorkHours = new Label("Role actual work hours");
		Label lblTitleSalary = new Label("Salary per hour");
		Label lblTitleEfficiency = new Label("Efficiency status");
		Label lblTitleEfficiencyProfit = new Label("Efficiency Profit");
		Label lblEmpPref = new Label(emp.getWorkHoursPreferred().toString());
		Label lblRoleWorkHours = new Label(emp.getRole().getWorkHoursActual().toString());
		Label lblEmpSalary = new Label(emp.getSalaryPerHour() + " [MoneyUnit]");
		double efficiency = l.viewCalcEfficiencyEmployee(emp);
		double efficiencyProfit = l.viewCalcEfficiencyProfitEmployee(emp);
		Label lblEfficiency = new Label(String.format("%,.1f", efficiency) + " [hour]");
		Label lblEfficiencyProfit = new Label(String.format("%,.1f", efficiencyProfit) + " [MoneyUnit]");

		if (efficiency < 0) {
			lblEfficiency.setTextFill(Color.RED);
			lblEfficiencyProfit.setTextFill(Color.RED);
		} else {
			lblEfficiency.setTextFill(Color.GREEN);
			lblEfficiencyProfit.setTextFill(Color.GREEN);
		}
		gpEmpEfficiencyData.addRow(0, lblTitleEmpPref, lblTitleRoleWorkHours, lblTitleSalary, lblTitleEfficiency, lblTitleEfficiencyProfit);
		gpEmpEfficiencyData.addRow(1, new Separator(), new Separator(), new Separator(), new Separator(), new Separator());
		gpEmpEfficiencyData.addRow(2, lblEmpPref, lblRoleWorkHours, lblEmpSalary, lblEfficiency, lblEfficiencyProfit);
		gpEmpEfficiencyData.setAlignment(Pos.CENTER);
		gpEmpEfficiencyData.setBorder(new Border(
				new BorderStroke(Color.SILVER, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		vbDataEmployee.getChildren().addAll(hbCompanyTitle, bcEfficiency, gpEmpEfficiencyData);
		return vbDataEmployee;
	}

}