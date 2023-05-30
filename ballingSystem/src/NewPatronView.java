/* AddPartyView.java
 *
 *  Version
 *  $Id$
 * 
 *  Revisions:
 * 		$Log: NewPatronView.java,v $
 * 		Revision 1.3  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a patron
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;


import java.util.*;
import java.text.*;

public class NewPatronView {
	private static final int TEXT_FIELD_SIZE = 15;
	private int maxSize;
	private ButtonCommand command;
	private JFrame win;
	private JButton abort, finished;
	private JLabel nickLabel, fullLabel, emailLabel;
	private JTextField nickField, fullField, emailField;
	private String nick, full, email;

	private boolean done;

	private String selectedNick, selectedMember;
	private AddPartyView addParty;

	public NewPatronView(AddPartyView v) {

		addParty=v;	
		done = false;

		win = new JFrame("Add Patron");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new BorderLayout());

		// Patron Panel
		JPanel patronPanel = new JPanel();
		patronPanel.setLayout(new GridLayout(3, 1));
		patronPanel.setBorder(new TitledBorder("Your Info"));

		nickField = new JTextField("", TEXT_FIELD_SIZE);
        JPanel nickPanel = createFieldPanel("Nick Name", nickField);

        fullField = new JTextField("", TEXT_FIELD_SIZE);
        JPanel fullPanel = createFieldPanel("Full Name", fullField);

        emailField = new JTextField("", TEXT_FIELD_SIZE);
        JPanel emailPanel = createFieldPanel("E-Mail", emailField);

        NewPatronViewClickEvent listener = new NewPatronViewClickEvent();
        patronPanel.add(nickPanel);
        patronPanel.add(fullPanel);
        patronPanel.add(emailPanel);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		finished = createButton("Add Patron", new JPanel(), listener);
		abort = createButton("Abort", new JPanel(), listener);

		buttonPanel.add((JPanel) finished.getParent());
		buttonPanel.add((JPanel) abort.getParent());

		// Clean up main panel
		colPanel.add(patronPanel, "Center");
		colPanel.add(buttonPanel, "East");

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.setVisible(true);

	}
	private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel(labelText);
        panel.add(label);
        panel.add(textField);
        return panel;
    }
	private JButton createButton(String buttonText, JPanel panel, NewPatronViewClickEvent listener) { // 버튼 객체 생성 
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
	public boolean done() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getFull() {
		return full;
	}
	public void setFull(String full) {
		this.full = full;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public JFrame getWindow() {
		return win;
	}
	public JTextField getNickField() {
		return nickField;
	}
	public JTextField getFullField() {
		return fullField;
	}
	public JTextField getEmailField() {
		return emailField;
	}
	public AddPartyView getAddParty() {
		return addParty;
	}
	public class NewPatronViewClickEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource().equals(abort)) {
				setCommand(new AbortNewPatronCommand(NewPatronView.this));
			}

			if (e.getSource().equals(finished)) {
				nick = nickField.getText();
				full = fullField.getText();
				email = emailField.getText();
				done = true;
				if (nick.isEmpty() || full.isEmpty() || email.isEmpty()) {
					// 예외 상황(아무 정보도 입력 안 한 상태)에 대해 사용자에게 메시지를 보여주는 코드 추가
					JOptionPane.showMessageDialog(win, "All fields must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					setCommand(new FinishedNewPatronCommand(NewPatronView.this));
				}
			}
			buttonPressed();
		}
	}
}
