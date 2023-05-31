
public class RemovePatronCommand implements ButtonCommand {
	AddPartyView addPartyView;

    public RemovePatronCommand(AddPartyView addPartyView) {
        this.addPartyView = addPartyView;
    }

    @Override
    public void execute() {
        if (addPartyView.getSelectedMember() != null) {
        	addPartyView.getParty().removeElement(addPartyView.getSelectedMember());
        	addPartyView.getPartyList().setListData(addPartyView.getParty());
        }
    }
}
