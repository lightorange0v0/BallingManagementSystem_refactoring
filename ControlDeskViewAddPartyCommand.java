
public class ControlDeskViewAddPartyCommand implements ButtonCommand {
    private ControlDeskView theView;

    public ControlDeskViewAddPartyCommand(ControlDeskView theView) {
        this.theView = theView;
    }

    @Override
    public void execute() {
            AddPartyView addPartyWin = new AddPartyView(theView, theView.getMaxMembers());
    }
}