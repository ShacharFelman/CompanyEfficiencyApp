import javafx.application.Application;
import javafx.stage.Stage;
import model.*;
import view.*;

import java.io.IOException;
import controller.Controller;

public class CompanyMainProgram extends Application {

	private Controller theController;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainCompanyView theView = new MainCompanyView(primaryStage);
		CompanyModel theModel = new CompanyModel("Your Company Name");
		theController = new Controller(theModel, theView);
		theView.showView();
	}
	
	@Override
	public void stop(){
			try {
				theController.saveToFile();
			} catch (IOException e) {
				theController.modelErrorMessage("Save To File", e.getMessage());
			}
	}
}
