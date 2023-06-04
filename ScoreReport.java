/**
 * 
 * SMTP implementation based on code by R�al Gagnon mailto:real@rgagnon.com
 * 
 */
import java.util.Vector;
import java.util.Iterator;

public class ScoreReport {
	private ScoreReportSender sender;
	private String content;
	
	public ScoreReport( Bowler bowler, int[] scores, int games ) {
		String nick = bowler.getNickName();
		String full = bowler.getFullName();
		Vector<Score> v = null;
		ScoreHistoryFile shf = new ScoreHistoryFile();
		try{
			v = shf.getScores(nick);
		} catch (Exception e){System.err.println("Error: " + e);}
		
		Iterator<Score> scoreIt = v.iterator();
		
		content = "";
		content += "--Lucky Strike Bowling Alley Score Report--\n";
		content += "\n";
		content += "Report for " + full + ", aka \"" + nick + "\":\n";
		content += "\n";
		content += "Final scores for this session: ";
		content += scores[0];
		for (int i = 1; i < games; i++){
			content += ", " + scores[i];
		}
		content += ".\n";
		content += "\n";
		content += "\n";
		content += "Previous scores by date: \n";
		while (scoreIt.hasNext()){
			Score score = (Score) scoreIt.next();
			content += "  " + score.getDate() + " - " +  score.getScore();
			content += "\n";
		}
		content += "\n\n";
		content += "Thank you for your continuing patronage.";

	}
	public void setSender(ScoreReportSender sender) {
		this.sender = sender;
	}

	public void sendTo(String recipient) {
		sender.send(recipient, this.content);
	}
}
