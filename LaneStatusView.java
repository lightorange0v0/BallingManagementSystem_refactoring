/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LaneStatusView implements LaneObserver, PinsetterObserver {

	private JPanel jp;

	private JLabel curBowler, foul, pinsDown;
	private JButton viewLane;
	private JButton viewPinSetter, maintenance;

	private PinSetterView psv;
	private LaneView lv;
	private Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;
	private ButtonCommand command;  // the Command instance for Command pattern


	public LaneStatusView(Lane lane, int laneNum ) {

		LaneStatusViewClickEvent listener = new LaneStatusViewClickEvent(); // view와 event 분리

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing=false;
		psShowing=false;

		psv = new PinSetterView( laneNum );
		Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		lv = new LaneView( lane, laneNum );
		lane.subscribe(lv);


		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JLabel cLabel = new JLabel( "Now Bowling: " );
		curBowler = new JLabel( "(no one)" );
		JLabel fLabel = new JLabel( "Foul: " );
		foul = new JLabel( " " );
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		viewLane = createButton("View Lane", listener);
		viewPinSetter = createButton("Pinsetter", listener);
		maintenance = createButton("     ", listener);
		maintenance.setBackground( Color.GREEN ); // !

		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );


		buttonPanel.add(viewLane);
		buttonPanel.add(viewPinSetter);
		buttonPanel.add(maintenance);

		jp.add( cLabel );
		jp.add( curBowler );
//		jp.add( fLabel );
//		jp.add( foul );
		jp.add( pdLabel );
		jp.add( pinsDown );
		
		jp.add(buttonPanel);

	}
	private JButton createButton(String text, LaneStatusViewClickEvent listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		return button;
	}

	public JPanel showLane() {
		return jp;
	}

	public class LaneStatusViewClickEvent implements ActionListener{
		public void actionPerformed( ActionEvent e ) {
			if ( lane.isPartyAssigned() && e.getSource().equals(viewPinSetter)) {
				setCommand(new LaneStatusViewPinSetterCommand(LaneStatusView.this));
			}
			else if (e.getSource().equals(viewLane)) {
				setCommand(new LaneStatusViewLaneCommand(LaneStatusView.this));
			}
			else if (e.getSource().equals(maintenance)) {
				setCommand(new LaneStatusMaintenanceCommand(LaneStatusView.this));
			}
			buttonPressed(); // 하나만 발생하도록 else if

		}
	}


	// The Invoker holds a command and can use it to call a method
	public void setCommand(ButtonCommand command) {
		this.command = command;
	}
	// Call the execute() method of the command
	public void buttonPressed() {
		command.execute();
	}


	public void receiveLaneEvent(LaneEvent le) {
		curBowler.setText( ( (Bowler)le.getBowler()).getNickName() );
		if ( le.isMechanicalProblem() ) {
			maintenance.setBackground( Color.RED );
		}	
		if ( lane.isPartyAssigned() == false ) {
			viewLane.setEnabled( false );
			viewPinSetter.setEnabled( false );
		} else {
			viewLane.setEnabled( true );
			viewPinSetter.setEnabled( true );
		}
	}

	public void receivePinsetterEvent(PinsetterEvent pe) {
		pinsDown.setText(String.valueOf(pe.totalPinsDown()));
//		foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );
		
	}

	public JButton getMaintenance() {
		return maintenance;
	}

	public PinSetterView getPsv() {
		return psv;
	}

	public LaneView getLv() {
		return lv;
	}

	public Lane getLane() {
		return lane;
	}

}
