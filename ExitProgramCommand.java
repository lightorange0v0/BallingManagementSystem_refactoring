public class ExitProgramCommand implements ButtonCommand{

    private ControlDeskView theView;

    public ExitProgramCommand(ControlDeskView theView) {
        this.theView = theView;
    }

    @Override
    public void execute() {
        theView.getWin().setVisible(false);
        System.exit(0);
    }
}