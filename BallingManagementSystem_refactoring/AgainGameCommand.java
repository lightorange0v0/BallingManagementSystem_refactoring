public class AgainGameCommand implements ButtonCommand {
    EndGamePrompt endGamePrompt;
    public AgainGameCommand(EndGamePrompt endGamePrompt){
        this.endGamePrompt = endGamePrompt;
    }
    public void execute(){
        endGamePrompt.setResult(1);
    }
}
