package viewDesignEvents;

import javafx.event.*;
import javafx.scene.control.Button;
import view.MainCompanyView;

public class EventMouseEnteredMngButtonRemove implements EventHandler<Event> {
	@Override
	public void handle(Event ae) {
		Button btn = (Button)ae.getSource();
		btn.setBackground(MainCompanyView.BACKGROUND_MNG_BUTTONS_REMOVE_SELECTED);
	}
}
