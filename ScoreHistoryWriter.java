import java.io.*;
public interface ScoreHistoryWriter {
    void addScore(String nick, String date, String score) throws IOException, FileNotFoundException;
}
