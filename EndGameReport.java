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
import java.util.*;


public class EndGameReport {

	private ButtonCommand command;
	private JFrame win;
	private JButton printButton, finished;
	private JList<String> memberList;
	private Vector<String> retVal;
	private int result;

	private String selectedMember;

	public EndGameReport( String partyName, Party party ) {
	
		result =0;
		retVal = new Vector<>();
		win = new JFrame("End Game Report for " + partyName + "?" );
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout( 1, 2 ));

		// Member Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Party Members"));
		
		Vector<String> myVector = new Vector<>();
		Iterator<Bowler> iter = (party.getMembers()).iterator();
		while (iter.hasNext()){
		  myVector.add((iter.next()).getNickName() );
		}	
		EndGameReportClickEvent listener = new EndGameReportClickEvent();
		memberList = new JList<>(myVector);
		memberList.setFixedCellWidth(120);
		memberList.setVisibleRowCount(5);
		memberList.addListSelectionListener(listener);
		JScrollPane partyPane = new JScrollPane(memberList);
		partyPanel.add(partyPane);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 1));

		printButton = createButton("Print Report", listener);
		finished = createButton("Finished", listener);

		buttonPanel.add(printButton);
		buttonPanel.add(finished);

		// Clean up main panel
		colPanel.add(partyPanel);
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
	private JButton createButton(String text, EndGameReportClickEvent listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }
	public void setCommand(ButtonCommand command) {
        this.command = command;
    }
	public void buttonPressed() {
        command.execute();
    }

	public Vector<String> waitForResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return retVal;	
	}
	
	public void destroy() {
		win.setVisible(false);
	}
	public String getSelectMember() {
		return selectedMember;
	}
	public void setResultNumber(int i) {
		result = i;
	}
	public JFrame getWin(){
		return win;
	}
	public Vector<String> getretVal(){
		return retVal;
	}
	public static void main( String args[] ) {
		Vector<Bowler> bowlers = new Vector<>();
		for ( int i=0; i<4; i++ ) {
			bowlers.add( new Bowler( "aaaaa", "aaaaa", "aaaaa" ) );
		}
		Party party = new Party( bowlers );
		String partyName="wank";
		EndGameReport e = new EndGameReport( partyName, party );
	}
	public class EndGameReportClickEvent implements ActionListener, ListSelectionListener {
	
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(printButton)) {		
				setCommand(new PrintGameCommand(EndGameReport.this));
			}
			if (e.getSource().equals(finished)) {	
				setCommand(new FinishedGameCommand(EndGameReport.this));	
				
			}
			buttonPressed();
		}
		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() instanceof JList) {
				JList<?> source = (JList<?>) e.getSource();
				selectedMember = (String) source.getSelectedValue();
			}
			
		}
	}
}