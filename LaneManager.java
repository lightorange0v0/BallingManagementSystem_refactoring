import java.util.HashSet;
import java.util.Iterator;

public class LaneManager {
    private ControlDesk controlDesk;

    /** The collection of Lanes */
    private HashSet<Lane> lanes;

    /** The number of lanes represented */
    private int numLanes;

    public LaneManager(ControlDesk controlDesk, int numLanes) {
        this.controlDesk = controlDesk;
        this.lanes = new HashSet<>(numLanes);
        this.numLanes = numLanes;

        for (int i = 0; i < numLanes; i++) {
            lanes.add(new Lane());
        }
    }

    /**
     * Iterate through the available lanes and assign the paties in the wait queue if lanes are available.
     *
     */

    public void assignLane() {
        Iterator<Lane> it = lanes.iterator();

        while (it.hasNext() && controlDesk.getWaitQueue().hasMoreElements()) {
            Lane curLane = it.next();

            if (!curLane.isPartyAssigned()) {
                System.out.println("ok... assigning this party");
                curLane.assignParty(((Party) controlDesk.getWaitQueue().next()));
            }
        }
        controlDesk.publish(new PartyQueueEvent(controlDesk.getPartyQueue()));
    }

    /**
     * Accessor for the number of lanes represented by the ControlDesk
     *
     * @return an int containing the number of lanes represented
     *
     */

    public int getNumLanes() {
        return numLanes;
    }

    /**
     * Accessor method for lanes
     *
     * @return a HashSet of Lanes
     *
     */

    public HashSet<Lane> getLanes() {
        return lanes;
    }
}
