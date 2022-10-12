package view;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import listeners.*;
import model.*;

import javax.swing.JOptionPane;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.stage.*;

public class MainCompanyView {

	public static final Color COLOR_MAIN_MENU_FILL = Color.ROYALBLUE;
	public static final Color COLOR_MAIN_MENU_FILL_SELECTED = Color.CORNFLOWERBLUE;
	public static final Color COLOR_MAIN_MENU_TEXT = Color.WHITE;
	public static final Color COLOR_MNG_BUTTON_REMOVE = Color.INDIANRED;
	public static final Color COLOR_MNG_BUTTON_REMOVE_SELECTED = Color.LIGHTCORAL;
	public static Background BACKGROUND_MAIN_MENU = new Background(
			new BackgroundFill(COLOR_MAIN_MENU_FILL, CornerRadii.EMPTY, Insets.EMPTY));
	public static Background BACKGROUND_MAIN_MENU_SELECTED = new Background(
			new BackgroundFill(COLOR_MAIN_MENU_FILL_SELECTED, CornerRadii.EMPTY, Insets.EMPTY));
	public static final Background BACKGROUND_MNG_BUTTONS = new Background(
			new BackgroundFill(COLOR_MAIN_MENU_FILL, new CornerRadii(10), Insets.EMPTY));
	public static final Background BACKGROUND_MNG_BUTTONS_SELECTED = new Background(
			new BackgroundFill(COLOR_MAIN_MENU_FILL_SELECTED, new CornerRadii(10), Insets.EMPTY));
	public static final Background BACKGROUND_MNG_BUTTONS_REMOVE = new Background(
			new BackgroundFill(COLOR_MNG_BUTTON_REMOVE, new CornerRadii(10), Insets.EMPTY));
	public static final Background BACKGROUND_MNG_BUTTONS_REMOVE_SELECTED = new Background(
			new BackgroundFill(COLOR_MNG_BUTTON_REMOVE_SELECTED, new CornerRadii(10), Insets.EMPTY));

	public static Font FONT_MAIN_MENU = Font.font("Tahoma", 16);
	public static final Font FONT_SUB_MENU = Font.font("Tahoma", 14);
	public static final Font FONT_HEAD_LINE = Font.font("Tahoma", FontWeight.BOLD, 22);
	public static int MAIN_MENU_WIDHT = 75;
	public static final long TOOLTIP_SHOW_DEALAY_MILISEC = 350;

	protected enum eSideMenuButtons {
		HOME, MODEL, WORKERS
	};

	protected enum eBottomMenuButtons {
		MANAGE, DATA
	};

	private eSideMenuButtons selectedSideMenuButton;
	private eBottomMenuButtons selectedBottonMenuButton;

	protected Vector<ViewListenable> allListeners;

	private Stage primaryStage;

	private BorderPane bpRoot;
	private ViewMenusPanes menuViewManager;
	private ViewMngPanes companyMngViewManager;
	private ViewDataPanes dataViewManager;

	private HBox hbSideMenu;
	private HBox hbHomePage;
	private TreeView<CompanyEntity> tvCompany;
	private TreeItem<CompanyEntity> tiCompanyRoot;

	Button btnSideMenuHome = new Button();
	Button btnSideMenuModel = new Button();
	Button btnSideMenuEmployees = new Button();
	Button btnBottomMenuMng = new Button();
	Button btnBottomMenuData = new Button();

	public MainCompanyView(Stage theStage) throws FileNotFoundException {
		primaryStage = theStage;
		primaryStage.setTitle("Company Efficiency Manager");

		allListeners = new Vector<ViewListenable>();

		menuViewManager = new ViewMenusPanes(btnSideMenuHome, btnSideMenuModel, btnSideMenuEmployees, btnBottomMenuMng,
				btnBottomMenuData);
		companyMngViewManager = new ViewMngPanes(this);
		dataViewManager = new ViewDataPanes();

		bpRoot = new BorderPane();
		bpRoot.setLeft(hbSideMenu);

		setHomePage();
		bpRoot.setCenter(hbHomePage);

		btnSideMenuHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				selectedSideMenuButton = eSideMenuButtons.HOME;
				menuViewManager.selectedMenusButtonsChange(selectedSideMenuButton, selectedBottonMenuButton);
				setSideMenuPane();
				setHomePage();
				bpRoot.setCenter(hbHomePage);
				bpRoot.setBottom(null);
			}
		});

		btnSideMenuModel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				selectedSideMenuButton = eSideMenuButtons.MODEL;
				if (selectedBottonMenuButton == null) {
					selectedBottonMenuButton = eBottomMenuButtons.MANAGE;
				}

				setSideMenuPane();
				menuViewManager.selectedMenusButtonsChange(selectedSideMenuButton, selectedBottonMenuButton);
				bpRoot.setBottom(menuViewManager.setBottomMenuPane());
				changePaneToPresent(null, null);
			}
		});

		btnSideMenuEmployees.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				selectedSideMenuButton = eSideMenuButtons.WORKERS;
				if (selectedBottonMenuButton == null) {
					selectedBottonMenuButton = eBottomMenuButtons.MANAGE;
				}
				setSideMenuPane();
				menuViewManager.selectedMenusButtonsChange(selectedSideMenuButton, selectedBottonMenuButton);
				bpRoot.setBottom(menuViewManager.setBottomMenuPane());
				changePaneToPresent(null, null);
			}
		});
		btnBottomMenuMng.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (selectedBottonMenuButton != eBottomMenuButtons.MANAGE) {
					selectedBottonMenuButton = eBottomMenuButtons.MANAGE;
					changePaneToPresent(null, null);
					menuViewManager.selectedMenusButtonsChange(selectedSideMenuButton, selectedBottonMenuButton);
				}
			}
		});

		btnBottomMenuData.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (selectedBottonMenuButton != eBottomMenuButtons.DATA) {
					selectedBottonMenuButton = eBottomMenuButtons.DATA;
					changePaneToPresent(null, null);
					menuViewManager.selectedMenusButtonsChange(selectedSideMenuButton, selectedBottonMenuButton);
				}
			}
		});
	}

//===========================================================>
//<= Show view - primary stage, initialized from controller	=>
//===========================================================>
	public void showView() {
		updateCompanyName();
		primaryStage.setScene(new Scene(bpRoot, 1200, 600));
		primaryStage.show();
		setSideMenuPane();
	}

//===================================================>
//<= Register listener (controller) to this view	=>
//===================================================>
	public void registerListener(ViewListenable l) {
		allListeners.add(l);
	}

//===========================>
//<= Build Home Page		=>
//===========================>
	public void setHomePage() {
		hbHomePage = new HBox(10);
		VBox vbHomePage = new VBox(10);
		vbHomePage.setPadding(new Insets(10));
		vbHomePage.setAlignment(Pos.TOP_CENTER);

		hbHomePage.getChildren().add(vbHomePage);
		try {
			Image image = new Image(new FileInputStream("icons\\HomePageGraph.png"));
			ImageView imageView = new ImageView(image);
		      imageView.setFitWidth(300); 
		      imageView.setPreserveRatio(true); 
		      VBox vbImage = new VBox(imageView);
		      vbImage.setAlignment(Pos.CENTER);
			hbHomePage.getChildren().add(vbImage);
		} catch (FileNotFoundException e1) {
		}  
	      
		Label lblProgramTitle = new Label("Welcome to your company efficiency manager!");
		lblProgramTitle.setFont(FONT_HEAD_LINE);
		HBox hbProgramTitle = new HBox(10, lblProgramTitle);
		hbProgramTitle.setAlignment(Pos.CENTER);

		VBox vbInstBottom = new VBox();
		Label lblInstBottom1 = new Label("You can change view type using bottom menu:");
		Label lblInstBottom2 = new Label(
				"Manage -\tManage your company Departments, Roles and Employees (Add, Edit, Remove, Change Hours).");
		Label lblInstBottom3 = new Label("Data -\tShow efficiency data of your company or any selected entity.");
		lblInstBottom1.setFont(FONT_SUB_MENU);
		lblInstBottom2.setFont(FONT_SUB_MENU);
		lblInstBottom3.setFont(FONT_SUB_MENU);

		vbInstBottom.getChildren().addAll(lblInstBottom1, new Label(), lblInstBottom2, lblInstBottom3);

		VBox vbInstSide = new VBox();
		Label lblInstSide1 = new Label("You can select your company view layout using left side menu:");
		Label lblInstSide2 = new Label("Model -\tWork with model point of view of your company (Departments, Roles).");
		Label lblInstSide3 = new Label("Workers -\tWork with manpower point of view of your company (Employees).");
		lblInstSide1.setFont(FONT_SUB_MENU);
		lblInstSide2.setFont(FONT_SUB_MENU);
		lblInstSide3.setFont(FONT_SUB_MENU);
		vbInstSide.getChildren().addAll(lblInstSide1, new Label(), lblInstSide2, lblInstSide3);

		VBox vbInstInfo = new VBox();
		Label lblInfo = new Label("For additional information you can check the readme file or the video manual.");
		lblInfo.setFont(FONT_SUB_MENU);
		vbInstInfo.getChildren().add(lblInfo);

		HBox hbButtons = new HBox(20);
		Button btnReadme = new Button("readme");
		Button btnVideo = new Button("Video Manual");
		companyMngViewManager.setSubMenuButtonDesign(btnReadme);
		companyMngViewManager.setSubMenuButtonDesign(btnVideo);

		btnReadme.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (Desktop.isDesktopSupported()) {
					try {
						if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
							Desktop.getDesktop().open(new File("readme.docx"));
						}
					} catch (IOException e) {
						msgPopupError("Open readme file", e.getMessage());
					}
				}
			}
		});
		btnVideo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if (Desktop.isDesktopSupported()) {
					try {
						if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
							Desktop.getDesktop().open(new File("FinalProjectDemo.mp4"));
						}
					} catch (IOException | IllegalArgumentException e) {
						msgPopupError("Open video manual", e.getMessage());
					}
				}
			}
		});
		
		hbButtons.getChildren().addAll(btnReadme, btnVideo);

		vbHomePage.getChildren().addAll(lblProgramTitle, new Separator(Orientation.HORIZONTAL), vbInstSide,
				vbInstBottom, new Label(), vbInstInfo, hbButtons);
		
	}

//========================>
//<= Update Company Name =>
//========================>
	public void updateCompanyName() {
		if (tiCompanyRoot != null) {
			tiCompanyRoot.setValue(allListeners.get(0).viewGetCompany());
		}
	}

//===========================>
//<= Build side menu area	=>
//===========================>
	public void setSideMenuPane() {
		hbSideMenu = new HBox();
		menuViewManager.selectedMenusButtonsChange(selectedSideMenuButton, selectedBottonMenuButton);
		hbSideMenu.getChildren().add(menuViewManager.setSideMenuPane());

		bpRoot.setLeft(hbSideMenu);
		if (selectedSideMenuButton == eSideMenuButtons.MODEL) {
			setCompanyModelTree();
		} else if (selectedSideMenuButton == eSideMenuButtons.WORKERS) {
			setCompanyEmployeesTree();
		}
	}

//===================================>
//<= Build company tree view area	=>
//===================================>
	public void setCompanyModelTree() {
		tiCompanyRoot = new TreeItem<CompanyEntity>(allListeners.get(0).viewGetCompany());
		Set<Department> allDepartments = allListeners.get(0).viewGetDepartments();

		for (Department D : allDepartments) {
			Set<? extends Role> depAllRoles = D.getRoles();
			TreeItem<CompanyEntity> department = new TreeItem<CompanyEntity>(D);
			for (Role R : depAllRoles) {
				department.getChildren().add(new TreeItem<CompanyEntity>(R));
			}
			tiCompanyRoot.getChildren().add(department);
		}
		tvCompany = new TreeView<CompanyEntity>(tiCompanyRoot);
		tvCompany.setPrefWidth(200);
		tvCompany.getRoot().setExpanded(true);
		tvCompany.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> changePaneToPresent(null, null));

		hbSideMenu.getChildren().add(tvCompany);
	}

//===================================>
//<= Build company tree view area	=>
//===================================>
	public void setCompanyEmployeesTree() {
		tiCompanyRoot = new TreeItem<CompanyEntity>(allListeners.get(0).viewGetCompany());
		Set<Employee> allEmployees = allListeners.get(0).viewGetEmployees();
		for (Employee E : allEmployees) {
			TreeItem<CompanyEntity> emp = new TreeItem<CompanyEntity>(E);
			tiCompanyRoot.getChildren().add(emp);
		}

		tvCompany = new TreeView<CompanyEntity>(tiCompanyRoot);
		tvCompany.setPrefWidth(200);
		tvCompany.getRoot().setExpanded(true);
		tvCompany.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> changePaneToPresent(null, null));
		hbSideMenu.getChildren().add(tvCompany);
	}

//=======================================================>
//<-------------- Change pane to display ----------------> 
//<= Selected company entity changed or					=>
//<= Selected data to display changed (MANAGE / DATA)) 	=>
//=======================================================>
	public void changePaneToPresent(CompanyEntity activeCompanyItem, CompanyEntity parentCompanyItem) {
		if (selectedBottonMenuButton == eBottomMenuButtons.MANAGE) {
			if (activeCompanyItem != null) {
				if (activeCompanyItem instanceof Department) {
					bpRoot.setCenter(companyMngViewManager.setMngDepartmentPane(allListeners.get(0),
							(Department) activeCompanyItem));
					return;
				} else if (activeCompanyItem instanceof Role && parentCompanyItem != null) {
					bpRoot.setCenter(companyMngViewManager.addSetRoleFromUser(allListeners.get(0),
							(Role) activeCompanyItem, (Department) parentCompanyItem));
					return;
				} else if (activeCompanyItem instanceof Employee) {
					bpRoot.setCenter(companyMngViewManager.addSetEmplpoyeeFromUser(allListeners.get(0),
							(Employee) activeCompanyItem));
					return;
				}
			} else if (tvCompany.getSelectionModel().getSelectedItem() != null) {
				CompanyEntity selectedCompanyItem = tvCompany.getSelectionModel().getSelectedItem().getValue();
				if (selectedCompanyItem instanceof Department) {
					bpRoot.setCenter(companyMngViewManager.setMngDepartmentPane(allListeners.get(0),
							(Department) selectedCompanyItem));
					return;
				} else if (selectedCompanyItem instanceof Role) {
					bpRoot.setCenter(companyMngViewManager.addSetRoleFromUser(allListeners.get(0),
							(Role) selectedCompanyItem,
							(Department) tvCompany.getSelectionModel().getSelectedItem().getParent().getValue()));
					return;
				} else if (selectedCompanyItem instanceof Employee) {
					bpRoot.setCenter(companyMngViewManager.addSetEmplpoyeeFromUser(allListeners.get(0),
							(Employee) selectedCompanyItem));
					return;
				}
			}
			if (selectedSideMenuButton == eSideMenuButtons.WORKERS) {
				bpRoot.setCenter(companyMngViewManager.setMngEmployeesPane(allListeners.get(0)));
			} else {
				bpRoot.setCenter(companyMngViewManager.setMngCompanyPane(allListeners.get(0)));
			}
		} else if (selectedBottonMenuButton == eBottomMenuButtons.DATA) {
			if (tvCompany.getSelectionModel().getSelectedItem() != null) {
				CompanyEntity selectedCompanyItem = tvCompany.getSelectionModel().getSelectedItem().getValue();
				if (selectedCompanyItem instanceof Department) {
					bpRoot.setCenter(dataViewManager.setDataDepartmentPane(allListeners.get(0),
							(Department) selectedCompanyItem));
					return;
				} else if (selectedCompanyItem instanceof Role) {
					if (((Role) selectedCompanyItem).getEmployee() != null) {
						bpRoot.setCenter(dataViewManager.setDataEmployeePane(allListeners.get(0),
								((Role) selectedCompanyItem).getEmployee()));

					} else {
						msgPopupError("Show Role Efficiency",
								((Role) selectedCompanyItem) + " is not occupied with employee.");
						bpRoot.setCenter(dataViewManager.setDataCompanyModelPane(allListeners.get(0)));
					}
					return;
				} else if (selectedCompanyItem instanceof Employee) {
					if (((Employee) selectedCompanyItem).getRole() != null) {
						bpRoot.setCenter(dataViewManager.setDataEmployeePane(allListeners.get(0),
								(Employee) selectedCompanyItem));

					} else {
						msgPopupError("Show Employee Efficiency",
								((Employee) selectedCompanyItem) + " is not working in any role.");
						bpRoot.setCenter(dataViewManager.setDataCompanyEmployeesPane(allListeners.get(0)));
					}
					return;
				}
			}
			if (selectedSideMenuButton == eSideMenuButtons.WORKERS) {
				bpRoot.setCenter(dataViewManager.setDataCompanyEmployeesPane(allListeners.get(0)));
			} else {
				bpRoot.setCenter(dataViewManager.setDataCompanyModelPane(allListeners.get(0)));
			}
		}
	}

// <==========================================>
// <= Build Pane: Manage Company Departments =>
// ===========================================>
	public void setMngCompanyPane() {
		bpRoot.setCenter(companyMngViewManager.setMngCompanyPane(allListeners.get(0)));
	}

// <==========================================>
// <= Build Pane: Manage Company Employees =>
// ===========================================>
	public void setMngEmployeesPane() {
		bpRoot.setCenter(companyMngViewManager.setMngEmployeesPane(allListeners.get(0)));
	}

// <==========================================>
// <= Build Pane: Manage Company Departments =>
// ===========================================>
	public void setMngDepartmentPane(Department D) {
		bpRoot.setCenter(companyMngViewManager.setMngDepartmentPane(allListeners.get(0), D));
	}

// <==================================>
// <= Build Pane: Add New department =>
// ===================================>
	public void addDepartmentFromUser(Department D) {
		bpRoot.setCenter(companyMngViewManager.addSetDepartmentFromUser(allListeners.get(0), D));
	}

// <============================>
// <= Build Pane: Add New Role =>
// =============================>
	public void addRoleFromUser(Department theDep) {
		bpRoot.setCenter(companyMngViewManager.addSetRoleFromUser(allListeners.get(0), null, theDep));
	}

// <================================>
// <= Build Pane: Add New Employee =>
// =================================>
	public void addEmplpoyeeFromUser() {
		bpRoot.setCenter(companyMngViewManager.addSetEmplpoyeeFromUser(allListeners.get(0), null));
	}

//<==================>
//<= Show messages:	=>
//<= JOption Popups	=> 
//===================>
	public void msgPopupInformation(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}

	public void msgPopupSuccess(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Operation Succeed", JOptionPane.INFORMATION_MESSAGE);
	}

	public void msgPopupError(String operation, String errorMsg) {
		JOptionPane.showMessageDialog(null, errorMsg, operation, JOptionPane.ERROR_MESSAGE);
	}
}
