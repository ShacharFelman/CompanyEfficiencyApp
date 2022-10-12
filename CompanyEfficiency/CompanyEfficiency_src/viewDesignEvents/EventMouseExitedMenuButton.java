package viewDesignEvents;

import javafx.event.*;
import javafx.scene.control.*;

public class EventMouseExitedMenuButton implements EventHandler<Event> {
	@Override
	public void handle(Event ae) {
		Button btn = (Button)ae.getSource();
		btn.setBorder(null);
	}
	
}
