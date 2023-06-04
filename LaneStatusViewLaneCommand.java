class LaneStatusViewLaneCommand implements ButtonCommand {
    private LaneStatusView theView;

    public LaneStatusViewLaneCommand(LaneStatusView theView) {
        this.theView = theView;
    }

    public void execute() {
        if (theView.getLane().isPartyAssigned()) {
            if (theView.laneShowing == false) {
                theView.getLv().show();
                theView.laneShowing = true;
            } else if (theView.laneShowing == true) {
                theView.getLv().hide();
                theView.laneShowing = false;
            }
        }
    }
}
