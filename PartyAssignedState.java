public class PartyAssignedState implements LaneState {
    

    @Override
    public void handle(Lane lane){
        if(lane.isGameFinished() == false){
            lane.setState(lane.pauseGameState);
            lane.handleState();
        }
        else{
            lane.setState(lane.endOfGameState);
            lane.handleState();
        }
    }
}
