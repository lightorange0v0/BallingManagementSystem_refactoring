public class PrintGameCommand implements ButtonCommand{
    EndGameReport endGameReport;
    public PrintGameCommand(EndGameReport endGameReport){
        this.endGameReport = endGameReport;
    }
    public void execute(){
        endGameReport.getretVal().add(endGameReport.getSelectMember());
    }
}
