import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EndGamePromptClickEvent implements ActionListener {
    private EndGamePrompt endGamePrompt;
    EndGamePromptClickEvent(EndGamePrompt endGamePrompt){
        this.endGamePrompt = endGamePrompt;
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(endGamePrompt.getYesButton())) {		
            endGamePrompt.setCommand(new AgainGameCommand(endGamePrompt));
        }
        if (e.getSource().equals(endGamePrompt.getNoButton())) {		
            endGamePrompt.setCommand(new EndGameCommand(endGamePrompt));
        }
        endGamePrompt.buttonPressed();
    }
}
