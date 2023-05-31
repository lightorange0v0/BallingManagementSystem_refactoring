public class FinishedGameCommand implements ButtonCommand{
    EndGameReport endGameReport;
    public FinishedGameCommand(EndGameReport endGameReport){
        this.endGameReport = endGameReport;
    }
    public void execute(){
        endGameReport.getWin().setVisible(false);
		endGameReport.setResultNumber(1);
    }
}
