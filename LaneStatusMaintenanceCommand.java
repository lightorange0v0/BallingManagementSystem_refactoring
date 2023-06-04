import java.awt.*;

class LaneStatusMaintenanceCommand implements ButtonCommand {
    private LaneStatusView theView;

    public LaneStatusMaintenanceCommand(LaneStatusView theView) {
        this.theView = theView;
    }

    public void execute() {
        if (theView.getLane().isPartyAssigned()) {
            theView.getLane().unPauseGame();
            theView.getMaintenance().setBackground(Color.GREEN);
        }
    }
}
