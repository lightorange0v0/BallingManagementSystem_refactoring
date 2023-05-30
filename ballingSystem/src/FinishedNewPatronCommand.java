
public class FinishedNewPatronCommand implements ButtonCommand {
	NewPatronView newPatronView;
	AddPartyView.AddPartyViewClickEvent addPartyEvent;
	
	public FinishedNewPatronCommand(NewPatronView newPatronView) {
		this.newPatronView = newPatronView;
	}
	
	@Override
	public void execute() {
		addPartyEvent = newPatronView.getAddParty().new AddPartyViewClickEvent();
		addPartyEvent.updateNewPatron(newPatronView);
		newPatronView.getWindow().setVisible(false);
	}
}
