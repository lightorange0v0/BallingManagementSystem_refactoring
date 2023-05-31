class LaneStatusViewPinSetterCommand implements ButtonCommand {
    private LaneStatusView theView;

    public LaneStatusViewPinSetterCommand(LaneStatusView theView) {
        this.theView = theView;
    }

    public void execute() {
        if (theView.psShowing == false) {
            theView.psv.show();
            theView.psShowing = true;
        } else if (theView.psShowing == true) {
            theView.psv.hide();
            theView.psShowing = false;
        }
    }
}
