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
import javax.swing.border.*;
import javax.swing.event.*;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

	private JPanel jp;

	private JLabel curBowler, foul, pinsDown;
	private JButton viewLane;
	private JButton viewPinSetter;
	public JButton maintenance;

	public PinSetterView psv;
	public LaneView lv;
	public Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;
	private ButtonCommand command;  // the Command instance for Command pattern


	public LaneStatusView(Lane lane, int laneNum ) {

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

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		viewLane = new JButton("View Lane");
		JPanel viewLanePanel = new JPanel();
		viewLanePanel.setLayout(new FlowLayout());
		viewLane.addActionListener(this);
		viewLanePanel.add(viewLane);

		viewPinSetter = new JButton("Pinsetter");
		JPanel viewPinSetterPanel = new JPanel();
		viewPinSetterPanel.setLayout(new FlowLayout());
		viewPinSetter.addActionListener(this);
		viewPinSetterPanel.add(viewPinSetter);

		maintenance = new JButton("     ");
		maintenance.setBackground( Color.GREEN );
		JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(this);
		maintenancePanel.add(maintenance);

		viewLane.setEnabled( false );
		viewPinSetter.setEnabled( false );


		buttonPanel.add(viewLanePanel);
		buttonPanel.add(viewPinSetterPanel);
		buttonPanel.add(maintenancePanel);

		jp.add( cLabel );
		jp.add( curBowler );
//		jp.add( fLabel );
//		jp.add( foul );
		jp.add( pdLabel );
		jp.add( pinsDown );
		
		jp.add(buttonPanel);

	}

	public JPanel showLane() {
		return jp;
	}

	public void actionPerformed( ActionEvent e ) {
		if ( lane.isPartyAssigned() && e.getSource().equals(viewPinSetter)) {
			setCommand(new LaneStatusViewPinSetterCommand(this));
		}
		else if (e.getSource().equals(viewLane)) {
			setCommand(new LaneStatusViewLaneCommand(this));
		}
		else if (e.getSource().equals(maintenance)) {
			setCommand(new LaneStatusMaintenanceCommand(this));
		}
		buttonPressed(); // 하나만 발생하도록 else if

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
		pinsDown.setText( ( new Integer(pe.totalPinsDown()) ).toString() );
//		foul.setText( ( new Boolean(pe.isFoulCommited()) ).toString() );
		
	}

}
