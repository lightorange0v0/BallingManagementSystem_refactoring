
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */

import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Lane extends Thread implements PinsetterObserver {	
	final LaneState noPartyAssignedState,
		partyAssignedState, gameInProgressState, decisionMakingState,
		endOfGameState, pauseGameState;
	private LaneState state;
	private Party party;
	private Pinsetter setter;
	private HashMap<Bowler , int[]> scores;
	private Vector<LaneObserver> subscribers;

	private boolean gameIsHalted;

	private boolean partyAssigned;
	private boolean gameFinished;
	private Iterator<Bowler> bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;

	private int[] curScores;
	private int[][] cumulScores;
	private boolean canThrowAgain;
	
	private int[][] finalScores;
	private int gameNumber;
	private ScoreHistoryFile shf;
	private Bowler currentThrower;			// = the thrower who just took a throw

	private ScoringStrategy strategy; // 점수 계산 전략 패턴
	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() { 
		setter = new Pinsetter();
		scores = new HashMap<>();
		subscribers = new Vector<>();
		shf = new ScoreHistoryFile();

		this.noPartyAssignedState = new NoPartyAssignedState();
		this.partyAssignedState = new PartyAssignedState();
		this.gameInProgressState = new GameInProgressState();
		this.decisionMakingState = new DecisionMakingState();
		this.endOfGameState = new EndOfGameState();
		this.pauseGameState = new PauseGameState();
		state = noPartyAssignedState;

		gameIsHalted = false;
		partyAssigned = false;

		gameNumber = 0;

		setter.subscribe( this );
		
		this.start();
	}

	/** run()
	 * 
	 * entry point for execution of this lane 
	 */
	public void run() {
		
		// Long Method Refactoring
		while (true) {
			handleState();
			//doSleep(10);
		}
	}

	public void setState(LaneState state) {
        this.state = state;
    }

	public void handleState() {
        state.handle(this);
    }

	public void prepareNextThrow(){ // 다음 볼이 던지도록 반복
		currentThrower = bowlerIterator.next();
		canThrowAgain = true;
		tenthFrameStrike = false;
		ball = 0;
		while (canThrowAgain) {
			setter.ballThrown();		// simulate the thrower's ball hiting
			ball++;
		}
	}

	public void recordFinalScore(){ // 마지막 점수 저장
		if (frameNumber == 9) {
			finalScores[bowlIndex][gameNumber] = cumulScores[bowlIndex][9];
			saveScore();
		}
	}

	private void saveScore(){ // 현재 점수 저장
		try{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm MM/dd/yyyy");
			LocalDateTime now = LocalDateTime.now();
			String dateString = now.format(formatter);

			shf.addScore(currentThrower.getNickName(), dateString, Integer.toString(cumulScores[bowlIndex][9]));
		} catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
	}

	public void resetThrow(){ // 다음 throw를 위해 모든 상태 초기화
		setter.reset();
		bowlIndex++;
	}

	public void proceedToNextFrame(){ // 다음 프레임을 위한 동작처리
		frameNumber++;
		resetBowlerIterator();
		bowlIndex = 0;
		if (frameNumber > 9) {
			gameFinished = true;
			gameNumber++;
			setState(this.endOfGameState);
			handleState();
		}
	}

	public int promptEndGame(){ // 게임 종료를 위한 응답 반영
		EndGamePrompt egp = new EndGamePrompt( ((Bowler) party.getMembers().get(0)).getNickName() + "'s Party" );
		int result = egp.getResult();
		egp.distroy();
		egp = null;
		System.out.println("result was: " + result);
		return result;
	}

	public void printEndGameReportAndNotifyMembers(){ // 점수 보고서 생성 및 출력
		Vector<String> printVector;
		EndGameReport egr = new EndGameReport((party.getMembers().get(0)).getNickName() + "'s Party", party);
		printVector = egr.waitForResult();
		partyAssigned = false;
		Iterator<Bowler> scoreIt = party.getMembers().iterator();
		party = null;
		partyAssigned = false;
		publish(lanePublish());

		sendScoreReports(printVector, scoreIt);
	}

	private void sendScoreReports(Vector<String> printVector, Iterator<Bowler> scoreIt){ // 최종 점수 보고서 이메일 보내기와 출력
		int myIndex = 0;
		while (scoreIt.hasNext()){
			Bowler thisBowler = scoreIt.next();
			ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
			sr.setSender(new EmailReportSender());
			sr.sendTo(thisBowler.getEmail());
			printScoreReportForMembers(printVector, thisBowler, sr);

		}
	}

	private void printScoreReportForMembers(Vector<String> printVector, Bowler thisBowler, ScoreReport sr){ // 각 멤버 점수 보고서 출력
		Iterator<String> printIt = printVector.iterator();
		while (printIt.hasNext()){
			if (thisBowler.getNickName() == printIt.next()){
				System.out.println("Printing " + thisBowler.getNickName());
				sr.setSender(new PrintReportSender());
				sr.sendTo("Printer");
			}
		}
	}

	/** recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param pe 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {
		
			if (pe.pinsDownOnThisThrow() >=  0) {			// this is a real throw
				markScore(currentThrower, frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());
	
				// next logic handles the ?: what conditions dont allow them another throw?
				// handle the case of 10th frame first
				if (frameNumber == 9) {
					if (pe.totalPinsDown() == 10) {
						setter.resetPins();
						if(pe.getThrowNumber() == 1) {
							tenthFrameStrike = true;
						}
					}
				
					if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false)) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}
				
					if (pe.getThrowNumber() == 3) {
						canThrowAgain = false;
						//publish( lanePublish() );
					}
				} else { // its not the 10th frame
			
					if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
						canThrowAgain = false;
						//publish( lanePublish() );
					} else if (pe.getThrowNumber() == 2) {
						canThrowAgain = false;
						//publish( lanePublish() );
					} else if (pe.getThrowNumber() == 3)  
						System.out.println("I'm here...");
				}
			} else {								//  this is not a real throw, probably a reset
			}
	}
	
	/** resetBowlerIterator()
	 * 
	 * sets the current bower iterator back to the first bowler
	 * 
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	public void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}

	/** resetScores()
	 * 
	 * resets the scoring mechanism, must be called before scoring starts
	 * 
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	public void resetScores() {
		Iterator<Bowler> bowlIt = (party.getMembers()).iterator();

		while ( bowlIt.hasNext() ) {
			int[] toPut = new int[25];
			for ( int i = 0; i != 25; i++){
				toPut[i] = -1;
			}
			scores.put( bowlIt.next(), toPut );
		}
	
		gameFinished = false;
		frameNumber = 0;
	}
		
	/** assignParty()
	 * 
	 * assigns a party to this lane
	 * 
	 * @pre none
	 * @post the party has been assigned to the lane
	 * 
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;
		
		curScores = new int[party.getMembers().size()];
		cumulScores = new int[party.getMembers().size()][10];
		finalScores = new int[party.getMembers().size()][128]; //Hardcoding a max of 128 games, bite me.
		gameNumber = 0;
		
		resetScores();
	}

	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 * 
	 * @param Cur		The current bowler
	 * @param frame	The frame that bowler is on
	 * @param ball		The ball the bowler is on
	 * @param score	The bowler's score 
	 */
	private void markScore( Bowler Cur, int frame, int ball, int score ){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + ball);

		curScore = scores.get(Cur);

	
		curScore[ index - 1] = score;
		scores.put(Cur, curScore);
		getScore( Cur, frame );
		publish( lanePublish() );
	}

	/** lanePublish()
	 *
	 * Method that creates and returns a newly created laneEvent
	 * 
	 * @return		The new lane event
	 */
	private LaneEvent lanePublish(  ) {
		LaneEvent laneEvent = new LaneEvent(party, bowlIndex, currentThrower, cumulScores, scores, frameNumber+1, curScores, ball, gameIsHalted);
		return laneEvent;
	}

	/** getScore()
	 *
	 * Method that calculates a bowlers score
	 * 
	 * @param Cur		The bowler that is currently up
	 * @param frame	The frame the current bowler is on
	 * 
	 * @return			The bowlers total score
	 */
	private void getScore( Bowler Cur, int frame) { // 전략 패턴 도입
		int[] curScore;
		curScore = scores.get(Cur);
		for (int i = 0; i != 10; i++){
			cumulScores[bowlIndex][i] = 0;
		}
		int current = 2*(frame - 1)+ball-1;
		//Iterate through each ball until the current one.

		for (int i = 0; i != current+2; i++){ // spare, strike, normal 나누기
			if (i%2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19){
				strategy = new SpareScoringStrategy();
			} else if (i < current && i%2 == 0 && curScore[i] == 10  && i < 18){
				strategy = new StrikeScoringStrategy();
			} else {
				strategy = new DefaultScoringStrategy();
			}
				strategy.computeScore(curScore, cumulScores, bowlIndex, current, i);
			} 
	}

	/** isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}
	
	/** isGameFinished
	 * 
	 * @return true if the game is done, false otherwise
	 */
	public boolean isGameFinished() {
		return gameFinished;
	}

	public boolean isGameHalted(){
		return gameIsHalted;
	}

	public Iterator<Bowler> getBowlIterator(){
		return bowlerIterator;
	}
	/** subscribe
	 * 
	 * Method that will add a subscriber
	 * 
	 * @param adding	Observer that is to be added
	 */

	public void subscribe( LaneObserver adding ) {
		subscribers.add( adding );
	}

	/** unsubscribe
	 * 
	 * Method that unsubscribes an observer from this object
	 * 
	 * @param removing	The observer to be removed
	 */
	
	public void unsubscribe( LaneObserver removing ) {
		subscribers.remove( removing );
	}

	/** publish
	 *
	 * Method that publishes an event to subscribers
	 * 
	 * @param event	Event that is to be published
	 */

	public void publish( LaneEvent event ) {
		if( subscribers.size() > 0 ) {
			Iterator<LaneObserver> eventIterator = subscribers.iterator();
			
			while ( eventIterator.hasNext() ) {
				( eventIterator.next()).receiveLaneEvent( event );
			}
		}
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 * 
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;	
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		setState(pauseGameState);
		handleState();
		publish(lanePublish());
	}
	
	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		publish(lanePublish());
	}

}
