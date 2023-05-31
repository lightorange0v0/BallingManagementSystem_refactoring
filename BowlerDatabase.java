import java.io.IOException;
import java.util.List;

public interface BowlerDatabase {
	Bowler getBowlerInfo(String nickName) throws IOException;
    void putBowlerInfo(String nickName, String fullName, String email) throws IOException;
    List<String> getBowlers() throws IOException;
}
