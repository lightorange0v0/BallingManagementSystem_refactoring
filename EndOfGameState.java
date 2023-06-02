public class EndOfGameState implements LaneState {
    @Override
    public void handle(Lane lane){
        int result = lane.promptEndGame();
        ((DecisionMakingState)lane.decisionMakingState).setResult(result);
        lane.setState(lane.decisionMakingState);
        lane.handleState();
    }
}
