
public class AbortNewPatronCommand implements ButtonCommand {
	NewPatronView newPatronView;
	
	public AbortNewPatronCommand(NewPatronView newPatronView) {
		this.newPatronView = newPatronView;
	}
	
	@Override
	public void execute() {
		newPatronView.setDone(true);
		newPatronView.getWindow().setVisible(false);
	}
}
