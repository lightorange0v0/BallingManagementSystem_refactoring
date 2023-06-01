public class GameInProgressState implements LaneState {
    @Override
    public void handle(Lane lane){
		if (lane.getBowlIterator().hasNext()) {
			lane.prepareNextThrow();
			lane.recordFinalScore();
			lane.resetThrow();
		} else {
			lane.proceedToNextFrame();
		}
    }
}
