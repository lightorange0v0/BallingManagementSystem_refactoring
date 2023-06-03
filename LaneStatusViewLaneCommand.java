class LaneStatusViewLaneCommand implements ButtonCommand {
    private LaneStatusView theView;

    public LaneStatusViewLaneCommand(LaneStatusView theView) {
        this.theView = theView;
    }

    public void execute() {
        if (theView.lane.isPartyAssigned()) {
            if (theView.laneShowing == false) {
                theView.lv.show();
                theView.laneShowing = true;
            } else if (theView.laneShowing == true) {
                theView.lv.hide();
                theView.laneShowing = false;
            }
        }
    }
}
