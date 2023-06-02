public class PauseGameState extends Thread implements LaneState {
    @Override
    public void handle(Lane lane){
        while(lane.isGameHalted()){
            doSleep(10);
        }
        lane.setState(lane.gameInProgressState);
        lane.handleState();
    }

    private void doSleep(int millis){ // 중복된 sleep()을 메소드로 변경
		try{
			sleep(millis);
		} catch (Exception e) {}
	}
}
