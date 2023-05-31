public class ControlDeskViewAssignCommand implements ButtonCommand{
    private ControlDeskView theView;

    public ControlDeskViewAssignCommand(ControlDeskView theView) {
        this.theView = theView;
    }

    @Override
    public void execute() {
        theView.getControlDesk().assignLane();
    }
}