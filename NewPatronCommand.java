
public class NewPatronCommand implements ButtonCommand {
    AddPartyView addPartyView;

    public NewPatronCommand(AddPartyView addPartyView) {
        this.addPartyView = addPartyView;
    }

    @Override
    public void execute() {
        NewPatronView newPatron = new NewPatronView(addPartyView);
    }
}
