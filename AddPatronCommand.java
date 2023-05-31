
public class AddPatronCommand implements ButtonCommand{
	AddPartyView addPartyView;
	
	public AddPatronCommand(AddPartyView addPartyView) {
		this.addPartyView = addPartyView;
	}
	
	@Override
	public void execute() {
		if (addPartyView.getSelectedNick() != null && addPartyView.getParty().size() < addPartyView.getMaxSize()) {
            if (addPartyView.getParty().contains(addPartyView.getSelectedNick())) {
                System.err.println("Member already in Party");
            } else {
            	addPartyView.getParty().add(addPartyView.getSelectedNick());
            	addPartyView.getPartyList().setListData(addPartyView.getParty());
            }
        }		
	}

}
