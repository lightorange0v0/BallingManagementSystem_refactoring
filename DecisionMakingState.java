public class DecisionMakingState implements LaneState {
    private int result;

    @Override
    public void handle(Lane lane){
        if (result == 1) {					// yes, want to play again
			lane.resetScores();
			lane.resetBowlerIterator();
            lane.setState(lane.gameInProgressState);
            lane.handleState();
		} else if (result == 2) { // no, dont want to play another game
			lane.printEndGameReportAndNotifyMembers();
            lane.setState(lane.noPartyAssignedState);
            lane.handleState();
		}
    }

    public void setResult(int result){
        this.result = result;
    }
}
