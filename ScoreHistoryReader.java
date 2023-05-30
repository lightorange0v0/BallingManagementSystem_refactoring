import java.io.*;
import java.util.*;
public interface ScoreHistoryReader {
    Vector<Score> getScores(String nick) throws IOException,FileNotFoundException;
}
