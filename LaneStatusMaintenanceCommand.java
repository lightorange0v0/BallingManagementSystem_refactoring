import java.awt.*;

class LaneStatusMaintenanceCommand implements ButtonCommand {
    private LaneStatusView theView;

    public LaneStatusMaintenanceCommand(LaneStatusView theView) {
        this.theView = theView;
    }

    public void execute() {
        if (theView.lane.isPartyAssigned()) {
            theView.lane.unPauseGame();
            theView.maintenance.setBackground(Color.GREEN);
        }
    }
}
