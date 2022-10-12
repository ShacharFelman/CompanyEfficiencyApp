package viewDesignEvents;

import javafx.event.*;
import javafx.scene.control.*;
import view.*;

public class EventMouseEnteredMngButton implements EventHandler<Event> {
	@Override
	public void handle(Event ae) {
		Button btn = (Button)ae.getSource();
		btn.setBackground(MainCompanyView.BACKGROUND_MNG_BUTTONS_SELECTED);
	}
}
