
public class FinishedAddPartyCommand implements ButtonCommand {
    private AddPartyView addPartyView;

    public FinishedAddPartyCommand(AddPartyView addPartyView) {
        this.addPartyView = addPartyView;
    }

    @Override
    public void execute() {
        if (addPartyView.getParty() != null && addPartyView.getParty().size() > 0) {
        	addPartyView.getControlDesk().updateAddParty(addPartyView);
        }
        addPartyView.getWindow().setVisible(false);
    }
}