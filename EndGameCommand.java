public class EndGameCommand implements ButtonCommand {
    EndGamePrompt endGamePrompt;
    public EndGameCommand(EndGamePrompt endGamePrompt){
        this.endGamePrompt = endGamePrompt;
    }
    public void execute(){
        endGamePrompt.setResult(2);
    }
}
