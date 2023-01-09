package fr.uparis.informatique.cpoo5.Game;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * PlayGround mode exclusive <br>
 * 
 * @author rh4
 *
 */
public class LifeGainedEvent extends Event {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -5785803689240674438L;
    public static final EventType<LifeGainedEvent> LIFE_GAINED = new EventType<>(ANY, "LIFE_GAINED");

	public LifeGainedEvent(EventType<? extends Event> eventType) {
		super(eventType);
		// TODO Auto-generated constructor stub
	}

	

}
