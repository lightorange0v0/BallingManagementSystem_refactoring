/* ControlDeskView.java
 *
 *  Version:
 *			$Id$
 * 
 *  Revisions:
 * 		$Log$
 * 
 */

/**
 * Class for representation of the control desk
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

public class ControlDeskView implements ControlDeskObserver {

	private JButton addParty, finished, assign;
	private JFrame win;
	private JList partyList;
	
	/** The maximum  number of members in a party */
	private int maxMembers;
	
	private ControlDesk controlDesk;

	private ButtonCommand command;

	/**
	 * Displays a GUI representation of the ControlDesk
	 *
	 */

	public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

		this.controlDesk = controlDesk;
		this.maxMembers = maxMembers;
		int numLanes = controlDesk.getNumLanes();

		ControlDeskClickEvent listener = new ControlDeskClickEvent();

		win = new JFrame("Control Desk");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Controls Panel
		JPanel controlsPanel = createPanel("Controls", new GridLayout(3, 1));

		JPanel addPartyPanel = new JPanel();
		addParty = createButton("Add Party", addPartyPanel, listener);
		controlsPanel.add(addPartyPanel);

		JPanel assignPanel = new JPanel();
		assign = createButton("Assign Lanes", assignPanel, listener);
		assignPanel.add(assign);
//		controlsPanel.add(assignPanel);

		JPanel finishedPanel = new JPanel();
		finished = createButton("Finished", finishedPanel, listener);
		controlsPanel.add(finishedPanel);

		// Lane Status Panel
		JPanel laneStatusPanel = createPanel("Lane Status", new GridLayout(numLanes, 1));

		HashSet<Lane> lanes=controlDesk.getLanes();
		Iterator<Lane> it = lanes.iterator();
		int laneCount=0;
		while (it.hasNext()) {
			Lane curLane = it.next();
			LaneStatusView laneStat = new LaneStatusView(curLane,(laneCount+1));
			curLane.subscribe(laneStat);
			(curLane.getPinsetter()).subscribe(laneStat);
			JPanel lanePanel = laneStat.showLane();
			lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount ));
			laneStatusPanel.add(lanePanel);
		}

		// Party Queue Panel
		JPanel partyPanel = createPanel("Party Queue", new FlowLayout());

		Vector<String> empty = new Vector<>();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(10);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);
		//		partyPanel.add(partyList);

		// Clean up main panel
		colPanel.add(controlsPanel, "East");
		colPanel.add(laneStatusPanel, "Center");
		colPanel.add(partyPanel, "West");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		/* Close program when this window closes */
		win.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.setVisible(true);

	}

	private JButton createButton(String text, JPanel panel, ActionListener listener) {
		JButton button = new JButton(text);
		button.addActionListener(listener);
		panel.setLayout(new FlowLayout());
		panel.add(button);
		return button;
	}

	private JPanel createPanel(String title, LayoutManager mgr){
		JPanel panel = new JPanel();
		panel.setLayout(mgr);
		panel.setBorder(new TitledBorder(title));
		return panel;
	}

	// The Invoker holds a command and can use it to call a method
	public void setCommand(ButtonCommand command) {
		this.command = command;
	}
	// Call the execute() method of the command
	public void buttonPressed() {
		command.execute();
	}

	/**
	 * Receive a new party from andPartyView.
	 *
	 * @param addPartyView	the AddPartyView that is providing a new party
	 *
	 */

	public void updateAddParty(AddPartyView addPartyView) {
		controlDesk.addPartyQueue(addPartyView.getParty());
	}

	/**
	 * Receive a broadcast from a ControlDesk
	 *
	 * @param pe	the ControlDeskEvent that triggered the handler
	 *
	 */

	public void receiveControlDeskEvent(PartyQueueEvent pe) {
		partyList.setListData(pe.getPartyQueue());
	}

	public int getMaxMembers() {
		return maxMembers;
	}

	public JFrame getWin() {
		return win;
	}

	public ControlDesk getControlDesk() {
		return controlDesk;
	}

	public class ControlDeskClickEvent implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(addParty)) {
				setCommand(new ControlDeskViewAddPartyCommand(ControlDeskView.this));
			}
			else if (e.getSource().equals(assign)) {
				setCommand(new ControlDeskViewAssignCommand(ControlDeskView.this));
			}
			else if (e.getSource().equals(finished)) {
				setCommand(new ExitProgramCommand(ControlDeskView.this));
			}

			buttonPressed();
		}
	}
}
