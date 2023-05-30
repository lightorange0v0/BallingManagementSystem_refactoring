/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;
import java.text.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 *  
 */

public class AddPartyView {
	
	private int maxSize;
	private ButtonCommand command;
	private JFrame win;
	private JButton addPatron, newPatron, remPatron, finished;
	private JList partyList, allBowlers;
	private Vector party, bowlerdb;
	private Integer lock;

	private ControlDeskView controlDesk;

	private String selectedNick, selectedMember;

	public AddPartyView(ControlDeskView controlDesk, int max) {
		
		this.controlDesk = controlDesk;
		maxSize = max;

		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));

		// Party Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Your Party"));

		party = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");
		
		AddPartyViewClickEvent listener = new AddPartyViewClickEvent();
		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(listener);
		JScrollPane partyPane = new JScrollPane(partyList);
		//        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = new JPanel();
		bowlerPanel.setLayout(new FlowLayout());
		bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

		try {
			bowlerdb = new Vector(BowlerFile.getInstance().getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = new JList(bowlerdb);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(listener);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		addPatron = createButton("Add to Party", new JPanel(), listener);
		remPatron = createButton("Remove Member", new JPanel(), listener);
		newPatron = createButton("New Patron", new JPanel(), listener);
		finished = createButton("Finished", new JPanel(), listener);

		buttonPanel.add((JPanel) addPatron.getParent());
		buttonPanel.add((JPanel) remPatron.getParent());
		buttonPanel.add((JPanel) newPatron.getParent());
		buttonPanel.add((JPanel) finished.getParent());

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.setVisible(true);

	}
	private JButton createButton(String buttonText, JPanel panel, AddPartyViewClickEvent listener) { // 버튼 객체 생성 
	    JButton button = new JButton(buttonText);
	    button.addActionListener(listener);
	    panel.setLayout(new FlowLayout());
	    panel.add(button);
	    return button;
	}
	public void setCommand(ButtonCommand command) {
        this.command = command;
    }
	public void buttonPressed() {
        command.execute();
    }
	public String getSelectedNick() {
		return selectedNick;
	}
	public String getSelectedMember() {
		return selectedMember;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public JList getPartyList() {
		return partyList;
	}
	public JFrame getWindow() {
		return win;
	}
	public ControlDeskView getControlDesk() {
		return controlDesk;
	}
	/**
	 * Accessor for Party
	 */

	public Vector getParty() {
		return party;
	}
	/**
	 * Accessor for Party
	 */

	public Vector getNames() {
		return party;
	}
	public class AddPartyViewClickEvent implements ActionListener, ListSelectionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(addPatron)) {
				setCommand(new AddPatronCommand(AddPartyView.this));
			}
			if (e.getSource().equals(remPatron)) {
				setCommand(new RemovePatronCommand(AddPartyView.this));
			}
			if (e.getSource().equals(newPatron)) {
				setCommand(new NewPatronCommand(AddPartyView.this));
			}
			if (e.getSource().equals(finished)) {
				setCommand(new FinishedAddPartyCommand(AddPartyView.this));
			}
			buttonPressed();
		}
		/**
		 * Handler for List actions
		 * @param e the ListActionEvent that triggered the handler
		 */

			public void valueChanged(ListSelectionEvent e) {
				if (e.getSource().equals(allBowlers)) {
					selectedNick =
						((String) ((JList) e.getSource()).getSelectedValue());
				}
				if (e.getSource().equals(partyList)) {
					selectedMember =
						((String) ((JList) e.getSource()).getSelectedValue());
				}
			}

		/**
		 * Called by NewPatronView to notify AddPartyView to update
		 * 
		 * @param newPatron the NewPatronView that called this method
		 */

			public void updateNewPatron(NewPatronView newPatron) {
				try {
					Bowler checkBowler = BowlerFile.getInstance().getBowlerInfo( newPatron.getNick() );
					if ( checkBowler == null ) {
						BowlerFile.getInstance().putBowlerInfo(
							newPatron.getNick(),
							newPatron.getFull(),
							newPatron.getEmail());
						bowlerdb = new Vector(BowlerFile.getInstance().getBowlers());
						allBowlers.setListData(bowlerdb);
						party.add(newPatron.getNick());
						partyList.setListData(party);
					} else {
						String errMsg = "A Bowler with that name already exists.";
						System.err.println(errMsg);
						JOptionPane.showMessageDialog(win, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e2) {
					System.err.println("File I/O Error");
				}
			}
	}

}
