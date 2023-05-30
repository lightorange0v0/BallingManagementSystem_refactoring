/* ControlDeskEvent.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */

/**
 * Class that represents control desk event
 *
 */

import java.util.*;

public class PartyQueueEvent {

	/** A representation of the wait queue, containing party names */
	private Vector<String> partyQueue;

    /**
     * Contstructor for the ControlDeskEvent
     *
     * @param partyQueue	a Vector of Strings containing the names of the parties in the wait queue
     *
     */

	public PartyQueueEvent(Vector<String> partyQueue ) {
		this.partyQueue = partyQueue;
	}

    /**
     * Accessor for partyQueue
     *
     * @return a Vector of Strings representing the names of the parties in the wait queue
     *
     */

	public Vector<String> getPartyQueue() {
		return partyQueue;
	}

}
