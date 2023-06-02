public class NoPartyAssignedState extends Thread implements LaneState {
    @Override
    public void handle(Lane lane){
        if(lane.isPartyAssigned() == true){
            lane.setState(lane.partyAssignedState);
            lane.handleState();
        }
        else{
            doSleep(10);
        }
    }

    private void doSleep(int millis){ // 중복된 sleep()을 메소드로 변경
		try{
			sleep(millis);
		} catch (Exception e) {}
	}
}
