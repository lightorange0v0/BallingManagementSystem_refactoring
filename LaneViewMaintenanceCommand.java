class LaneViewMaintenanceCommand implements ButtonCommand {
    private LaneView theView;

    public LaneViewMaintenanceCommand(LaneView theView) {
        this.theView = theView;
    }

    public void execute() {
        theView.lane.pauseGame();
    }
}
