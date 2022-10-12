package viewDesignEvents;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;

public class EventMouseEnteredSubMenuButton implements EventHandler<Event> {
	@Override
	public void handle(Event ae) {
		Button btn = (Button)ae.getSource();
		btn.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	}

}
