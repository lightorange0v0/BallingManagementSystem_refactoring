/* ControlDesk.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: ControlDesk.java,v $
 * 		Revision 1.13  2003/02/02 23:26:32  ???
 * 		ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 * 		
 * 		Revision 1.12  2003/02/02 20:46:13  ???
 * 		Added " 's Party" to party names.
 * 		
 * 		Revision 1.11  2003/02/02 20:43:25  ???
 * 		misc cleanup
 * 		
 * 		Revision 1.10  2003/02/02 17:49:10  ???
 * 		Fixed problem in getPartyQueue that was returning the first element as every element.
 * 		
 * 		Revision 1.9  2003/02/02 17:39:48  ???
 * 		Added accessor for lanes.
 * 		
 * 		Revision 1.8  2003/02/02 16:53:59  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.7  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 		Revision 1.6  2003/02/02 06:09:39  ???
 * 		Updated many classes to support the ControlDeskView.
 * 		
 * 		Revision 1.5  2003/01/26 23:16:10  ???
 * 		Improved thread handeling in lane/controldesk
 * 		
 * 
 */

/**
 * Class that represents control desk
 *
 */

import java.util.*;

class ControlDesk extends Thread {
	private static final int SLEEPMS = 250;

	private BowlerRegistrationManager registrationManager;

	private PartyQueueManager partyQueueManager;

	private LaneManager laneManager;

	private ScoreManager scoreManager;

	/** The collection of subscribers */
	private Vector<ControlDeskObserver> subscribers;

    /**
     * Constructor for the ControlDesk class
     *
     * @param numLanes	the numbler of lanes to be represented
     *
     */

	public ControlDesk(int numLanes) {
		registrationManager = new BowlerRegistrationManager(this);
		partyQueueManager = new PartyQueueManager(this);
		laneManager = new LaneManager(this, numLanes);
		scoreManager = new ScoreManager(this);

		subscribers = new Vector<>();

		this.start();

	}
	
	/**
	 * Main loop for ControlDesk's thread
	 * 
	 */
	public void run() {
		while (true) {
			
			assignLane();
			
			try {
				sleep(SLEEPMS);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
		

    /**
     * Retrieves a matching Bowler from the bowler database.
     *
     * @param nickName	The NickName of the Bowler
     *
     * @return a Bowler object.
     *
     */

	public Bowler registerPatron(String nickName) {
		return registrationManager.registerPatron(nickName);
	}

    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     *
     */

	public void assignLane() {
		laneManager.assignLane();
	}

    /**
     */

	public void viewScores(Lane ln) {
		// TODO: attach a LaneScoreView object to that lane
		scoreManager.viewScores(ln);
	}

    /**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks	A Vector of NickNames
     *
     */

	public void addPartyQueue(Vector<String> partyNicks) {
		partyQueueManager.addPartyQueue(partyNicks);
	}

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
	 *
     * @return a Vecotr of Strings
     *
     */

	public Vector<String> getPartyQueue() {
		return partyQueueManager.getPartyQueue();
	}

    /**
     * Accessor for the number of lanes represented by the ControlDesk
     *
     * @return an int containing the number of lanes represented
     *
     */

	public int getNumLanes() {
		return laneManager.getNumLanes();
	}

    /**
     * Allows objects to subscribe as observers
     * 
     * @param adding	the ControlDeskObserver that will be subscribed
     *
     */

	public void subscribe(ControlDeskObserver adding) {
		subscribers.add(adding);
	}

    /**
     * Broadcast an event to subscribing objects.
     * 
     * @param event	the ControlDeskEvent to broadcast
     *
     */

	public void publish(PartyQueueEvent event) {
		Iterator<ControlDeskObserver> eventIterator = subscribers.iterator();
		while (eventIterator.hasNext()) {
			eventIterator
				.next()
					.receiveControlDeskEvent(
				event);
		}
	}

    /**
     * Accessor method for lanes
     *
     * @return a HashSet of Lanes
     *
     */

	public HashSet<Lane> getLanes() {
		return laneManager.getLanes();
	}

	public Queue getWaitQueue(){
		return partyQueueManager.getWaitQueue();
	}
}
