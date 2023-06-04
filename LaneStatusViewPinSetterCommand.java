class LaneStatusViewPinSetterCommand implements ButtonCommand {
    private LaneStatusView theView;

    public LaneStatusViewPinSetterCommand(LaneStatusView theView) {
        this.theView = theView;
    }

    public void execute() {
        if (theView.psShowing == false) {
            theView.getPsv().show();
            theView.psShowing = true;
        } else if (theView.psShowing == true) {
            theView.getPsv().hide();
            theView.psShowing = false;
        }
    }
}
