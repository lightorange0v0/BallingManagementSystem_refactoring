/*
 *  constructs a prototype Lane View
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class LaneView implements LaneObserver, ActionListener {

	private int roll;
	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int cur;
	Iterator bowlIt;

	JPanel[][] balls;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;

	JButton maintenance;
	Lane lane;

	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;

		initDone = true;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {

		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
				BorderFactory.createTitledBorder(
					((Bowler) bowlers.get(i)).getNick()));
			pins[i].setLayout(new GridLayout(0, 10));
			for (int k = 0; k != 10; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}

	public void receiveLaneEvent(LaneEvent le) { // 레인 이벤트를 처리하는 메소드 리팩토링
		if (lane.isPartyAssigned()) {
			int numBowlers = le.getParty().getMembers().size();
			waitDoSleep();

			if (isFirstFrameFirstBall(le)) {
				setupFrame(le);
			}

			int[][] lescores = le.getCumulScore();
			updateScoreLabels(numBowlers, lescores, le);
		}
	}


	private void waitDoSleep(){ // 초기화 대기 메소드
		while (!initDone) {
			//System.out.println("chillin' here.");
			try {
				Thread.sleep(1);
			} catch (Exception e) {
			}
		}
	}

	private boolean isFirstFrameFirstBall(LaneEvent le) { // 첫번째 프레임의 시작인지 확인 메소드
		return le.getFrameNum() == 1 && le.getBall() == 0 && le.getIndex() == 0;
	}

	private void setupFrame(LaneEvent le){ // 프레임을 초기화하는 메소드
		System.out.println("Making the frame.");
		cpanel.removeAll();
		cpanel.add(makeFrame(le.getParty()), "Center");

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		maintenance = new JButton("Maintenance Call");
		JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(this);
		maintenancePanel.add(maintenance);

		buttonPanel.add(maintenancePanel);

		cpanel.add(buttonPanel, "South");

		frame.pack();
	}


	private void updateScoreLabels(int numBowlers, int[][] lescores, LaneEvent le){ // 점수와 라벨을 업데이트
		for (int k = 0; k < numBowlers; k++) {
			updateCumlativeScores(lescores, le.getFrameNum(), k);
			updateBallScores(le, k);
		}
	}
	private void updateCumlativeScores(int[][] lescores, int frameNum, int k){ // 누적 점수 업데이트 메소드
		for (int i = 0; i <= frameNum - 1; i++) {
			if (lescores[k][i] != 0)
				scoreLabel[k][i].setText(
						(new Integer(lescores[k][i])).toString());
		}
	}


	private void updateBallScores(LaneEvent le, int k){ // 점수에 대해서 전략 패턴 적용 => 추후 다른 점수 규칙 도입에 용
		List<ScoringStrategy> strategies = Arrays.asList(
				new StrikeScoringStrategy(),
				new SpareScoringStrategy(),
				new FoulScoringStrategy(),
				new DefaultScoringStrategy());

		for (int i = 0; i < 21; i++) {
			int[] bowlerScores = ((HashMap<String, int[]>) le.getScore()).get(bowlers.get(k));
			if (bowlerScores[i] != -1) {
				for (ScoringStrategy strategy : strategies) { // 4가지 유형 중 하나임.
					String scoreSymbol = strategy.getScoreSymbol(bowlerScores, i);
					if (scoreSymbol != null) {
						ballLabel[k][i].setText(scoreSymbol);
						break;
					}
				}
			}
		}
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
	}

}
