import java.util.Vector;

public class PartyQueueManager {
    private ControlDesk controlDesk;

    /** The party wait queue */
    private Queue partyQueue;

    public PartyQueueManager(ControlDesk controlDesk) {
        this.controlDesk = controlDesk;
        partyQueue = new Queue();
    }

    /**
     * Creates a party from a Vector of nickNAmes and adds them to the wait queue.
     *
     * @param partyNicks	A Vector of NickNames
     *
     */

    public void addPartyQueue(Vector<String> partyNicks) {
        Vector<Bowler> partyBowlers = new Vector<>();
        for (String partyNick : partyNicks) {
            Bowler newBowler = controlDesk.registerPatron(partyNick);
            partyBowlers.add(newBowler);
        }
        Party newParty = new Party(partyBowlers);
        partyQueue.add(newParty);
        controlDesk.publish(new PartyQueueEvent(getPartyQueue()));
    }

    /**
     * Returns a Vector of party names to be displayed in the GUI representation of the wait queue.
     *
     * @return a Vecotr of Strings
     *
     */

    public Vector<String> getPartyQueue() {
        Vector<String> displayPartyQueue = new Vector<>();
        for ( int i=0; i < ( partyQueue.asVector()).size(); i++ ) {
            String nextParty =
                    ((Bowler) ( ((Party) partyQueue.asVector().get( i ) ).getMembers())
                            .get(0))
                            .getNickName() + "'s Party";
            displayPartyQueue.addElement(nextParty);
        }
        return displayPartyQueue;
    }

    public Queue getWaitQueue(){
        return this.partyQueue;
    }
}
