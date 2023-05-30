import java.io.IOException;

public class BowlerRegistrationManager {
    private ControlDesk controlDesk;

    public BowlerRegistrationManager(ControlDesk controlDesk) {
        this.controlDesk = controlDesk;
    }

    /**
     * Retrieves a matching Bowler from the bowler database.
     *
     * @param nickName	The NickName of the Bowler
     *
     * @return a Bowler object.
     *
     */

    public Bowler registerPatron(String nickName) {
        Bowler patron = null;

        try {
            // only one patron / nick.... no dupes, no checks

            patron = BowlerFile.getBowlerInfo(nickName);

        } catch (IOException e) {
            System.err.println("Error..." + e);
        }

        return patron;
    }

}
