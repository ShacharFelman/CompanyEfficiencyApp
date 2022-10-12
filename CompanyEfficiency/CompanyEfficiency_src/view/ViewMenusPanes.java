package view;

import java.io.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.util.*;
import view.MainCompanyView.*;
import viewDesignEvents.*;

public class ViewMenusPanes {
	private Button btnSideMenuHome;
	private Button btnSideMenuModel;
	private Button btnSideMenuEmployees;
	private Label lblSideMenuHome;
	private Label lblSideMenuModel;
	private Label lblSideMenuEmployees;

	private Button btnBottomMenuMng;
	private Button btnBottomMenuData;
	private Label lblBottomMenuMng;
	private Label lblBottomMenuData;

	protected ViewMenusPanes(Button btnSideMenuHome, Button btnSideMenuModel, Button btnSideMenuEmployees,
			Button btnBottomMenuMng, Button btnBottomMenuData) throws FileNotFoundException {
		this.btnSideMenuHome = btnSideMenuHome;
		this.btnSideMenuModel = btnSideMenuModel;
		this.btnSideMenuEmployees = btnSideMenuEmployees;
		lblSideMenuHome = new Label(eSideMenuButtons.HOME.toString());
		lblSideMenuModel = new Label(eSideMenuButtons.MODEL.toString());
		lblSideMenuEmployees = new Label(eSideMenuButtons.WORKERS.toString());
		this.btnBottomMenuMng = btnBottomMenuMng;
		this.btnBottomMenuData = btnBottomMenuData;
		lblBottomMenuMng = new Label(eBottomMenuButtons.MANAGE.toString());
		lblBottomMenuData = new Label(eBottomMenuButtons.DATA.toString());

		btnSideMenuHome.setGraphic(new ImageView(new Image(new FileInputStream("icons\\size48\\home\\home-2-48.png"))));
		Tooltip tooltipSideMenuHome = new Tooltip("Home Page");
		tooltipSideMenuHome.setShowDelay(new Duration(MainCompanyView.TOOLTIP_SHOW_DEALAY_MILISEC));
		btnSideMenuHome.setTooltip(tooltipSideMenuHome);

		btnSideMenuModel.setGraphic(
				new ImageView(new Image(new FileInputStream("icons\\size48\\flowchart\\flow-chart-48.png"))));
		Tooltip tooltipSideMenuModel = new Tooltip("Company Business Model");
		tooltipSideMenuHome.setShowDelay(new Duration(MainCompanyView.TOOLTIP_SHOW_DEALAY_MILISEC));
		btnSideMenuModel.setTooltip(tooltipSideMenuModel);

		btnSideMenuEmployees
				.setGraphic(new ImageView(new Image(new FileInputStream("icons\\size48\\user\\workers-48.png"))));
		Tooltip tooltipSideMenuEmployees = new Tooltip("Company Employees");
		tooltipSideMenuHome.setShowDelay(new Duration(MainCompanyView.TOOLTIP_SHOW_DEALAY_MILISEC));
		btnSideMenuEmployees.setTooltip(tooltipSideMenuEmployees);

		btnBottomMenuMng.setGraphic(
				new ImageView(new Image(new FileInputStream("icons\\size32\\settings\\settings-5-32.png"))));
		Tooltip tooltipBottomMenuMng = new Tooltip("Manage Selected Item");
		tooltipSideMenuHome.setShowDelay(new Duration(MainCompanyView.TOOLTIP_SHOW_DEALAY_MILISEC));
		btnBottomMenuMng.setTooltip(tooltipBottomMenuMng);

		btnBottomMenuData
				.setGraphic(new ImageView(new Image(new FileInputStream("icons\\size32\\charts\\combo-32.png"))));
		Tooltip tooltipBottomMenuData = new Tooltip("Show Selected Item Efficiency Data");
		tooltipSideMenuHome.setShowDelay(new Duration(MainCompanyView.TOOLTIP_SHOW_DEALAY_MILISEC));
		btnBottomMenuData.setTooltip(tooltipBottomMenuData);
		
		setMenusButtonsDesign();
	}

//===========================>
//<= Build side menu area	=>
//===========================>
	protected VBox setSideMenuPane() {
		VBox vbSideMenu = new VBox();
		vbSideMenu.setSpacing(10);
		vbSideMenu.setPadding(new Insets(10));
		vbSideMenu.setAlignment(Pos.CENTER_LEFT);
		vbSideMenu.getChildren().addAll(btnSideMenuHome, lblSideMenuHome, new Label(), btnSideMenuModel,
				lblSideMenuModel, new Label(), btnSideMenuEmployees, lblSideMenuEmployees);
		vbSideMenu.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		return vbSideMenu;
	}
	
// ===========================>
// <= Build bottom menu area =>
// ===========================>
	protected HBox setBottomMenuPane() {
		HBox hbBottomMenu = new HBox();
		hbBottomMenu.setSpacing(100);
		hbBottomMenu.setPadding(new Insets(3));
		hbBottomMenu.setAlignment(Pos.CENTER);
		hbBottomMenu.getChildren().addAll(new VBox(btnBottomMenuMng, lblBottomMenuMng),
				new VBox(btnBottomMenuData, lblBottomMenuData));
		hbBottomMenu.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		return hbBottomMenu;
	}

// =====================================================>
// <= Build main menu buttons and set fonts and colors =>
// =====================================================>
	protected void setMenusButtonsDesign() {

		btnSideMenuHome.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnSideMenuModel.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnSideMenuEmployees.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnBottomMenuMng.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnBottomMenuData.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblSideMenuHome.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblSideMenuModel.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblSideMenuEmployees.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblBottomMenuMng.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblBottomMenuData.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		
		btnSideMenuHome.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		btnSideMenuModel.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		btnSideMenuEmployees.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		btnBottomMenuMng.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		btnBottomMenuData.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		lblSideMenuHome.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		lblSideMenuModel.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		lblSideMenuEmployees.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		lblBottomMenuMng.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);
		lblBottomMenuData.setMinWidth(MainCompanyView.MAIN_MENU_WIDHT);

		lblSideMenuHome.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);
		lblSideMenuModel.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);
		lblSideMenuEmployees.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);
		lblBottomMenuMng.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);
		lblBottomMenuData.setTextFill(MainCompanyView.COLOR_MAIN_MENU_TEXT);

		btnSideMenuHome.setAlignment(Pos.CENTER);
		btnSideMenuModel.setAlignment(Pos.CENTER);
		btnSideMenuEmployees.setAlignment(Pos.CENTER);
		btnBottomMenuMng.setAlignment(Pos.CENTER);
		btnBottomMenuData.setAlignment(Pos.CENTER);
		lblSideMenuHome.setAlignment(Pos.CENTER);
		lblSideMenuModel.setAlignment(Pos.CENTER);
		lblSideMenuEmployees.setAlignment(Pos.CENTER);
		lblBottomMenuMng.setAlignment(Pos.CENTER);
		lblBottomMenuData.setAlignment(Pos.CENTER);

		btnSideMenuHome.setFont(MainCompanyView.FONT_MAIN_MENU);
		btnSideMenuModel.setFont(MainCompanyView.FONT_MAIN_MENU);
		btnSideMenuEmployees.setFont(MainCompanyView.FONT_MAIN_MENU);
		btnBottomMenuMng.setFont(MainCompanyView.FONT_MAIN_MENU);
		btnBottomMenuData.setFont(MainCompanyView.FONT_MAIN_MENU);
		lblSideMenuHome.setFont(MainCompanyView.FONT_MAIN_MENU);
		lblSideMenuModel.setFont(MainCompanyView.FONT_MAIN_MENU);
		lblSideMenuEmployees.setFont(MainCompanyView.FONT_MAIN_MENU);
		lblBottomMenuMng.setFont(MainCompanyView.FONT_MAIN_MENU);
		lblBottomMenuData.setFont(MainCompanyView.FONT_MAIN_MENU);

		btnSideMenuHome.setOnMouseEntered(new EventMouseEnteredMainMenuButton());
		btnSideMenuModel.setOnMouseEntered(new EventMouseEnteredMainMenuButton());
		btnSideMenuEmployees.setOnMouseEntered(new EventMouseEnteredMainMenuButton());
		btnBottomMenuMng.setOnMouseEntered(new EventMouseEnteredMainMenuButton());
		btnBottomMenuData.setOnMouseEntered(new EventMouseEnteredMainMenuButton());
		btnSideMenuHome.setOnMouseExited(new EventMouseExitedMenuButton());
		btnSideMenuModel.setOnMouseExited(new EventMouseExitedMenuButton());
		btnSideMenuEmployees.setOnMouseExited(new EventMouseExitedMenuButton());
		btnBottomMenuMng.setOnMouseExited(new EventMouseExitedMenuButton());
		btnBottomMenuData.setOnMouseExited(new EventMouseExitedMenuButton());
	}

// ======================================>
// <= Change main menus selection color =>
// ======================================>
	protected void selectedMenusButtonsChange(eSideMenuButtons selectedSideMenuButton,
			eBottomMenuButtons selectedBottonMenuButton) {

		btnSideMenuHome.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnSideMenuModel.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnSideMenuEmployees.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnBottomMenuMng.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		btnBottomMenuData.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblSideMenuHome.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblSideMenuModel.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblSideMenuEmployees.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblBottomMenuMng.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);
		lblBottomMenuData.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU);

		if (selectedSideMenuButton != null) {
			switch (selectedSideMenuButton) {
			case HOME:
				btnSideMenuHome.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU_SELECTED);
				break;
			case MODEL:
				btnSideMenuModel.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU_SELECTED);
				break;
			case WORKERS:
				btnSideMenuEmployees.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU_SELECTED);
				break;
			default:
				break;
			}
		}
		if (selectedBottonMenuButton != null) {
			switch (selectedBottonMenuButton) {
			case MANAGE:
				btnBottomMenuMng.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU_SELECTED);
				break;
			case DATA:
				btnBottomMenuData.setBackground(MainCompanyView.BACKGROUND_MAIN_MENU_SELECTED);
				break;
			default:
				break;
			}
		}
	}
}
