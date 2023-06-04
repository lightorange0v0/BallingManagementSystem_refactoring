/* BowlerFile.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: BowlerFile.java,v $
 * 		Revision 1.5  2003/02/02 17:36:45  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for interfacing with Bowler database
 *
 */

import java.util.*;
import java.io.*;

class BowlerFile implements BowlerDatabase {

	/** The location of the bowelr database */
	private static String BOWLER_DAT = "BOWLERS.DAT";

	private BowlerFile() {}

    private static class LazyHolder {
        static final BowlerFile INSTANCE = new BowlerFile();
    }

    public static BowlerFile getInstance() {
        return LazyHolder.INSTANCE;
    }
    /**
     * Retrieves bowler information from the database and returns a Bowler objects with populated fields.
     *
     * @param nickName	the nickName of the bolwer to retrieve
     *
     * @return a Bowler object
     * 
     */
	private static List<String[]> readBowlersFromFile() throws IOException {
	    List<String[]> bowlers = new ArrayList<>();

	    try (BufferedReader in = new BufferedReader(new FileReader(BOWLER_DAT))) {
	        String data;
	        while ((data = in.readLine()) != null) {
	            // File format is nick\tfname\te-mail
	            String[] bowler = data.split("\t");
	            bowlers.add(bowler);
	        }
	    }

	    return bowlers;
	}
	@Override
	public Bowler getBowlerInfo(String nickName) throws IOException {
	    for (String[] bowler : readBowlersFromFile()) {
	        if (nickName.equals(bowler[0])) {
	            return new Bowler(bowler[0], bowler[1], bowler[2]);
	        }
	    }
	    return null;
	}

    /**
     * Stores a Bowler in the database
     *
     * @param nickName	the NickName of the Bowler
     * @param fullName	the FullName of the Bowler
     * @param email	the E-mail Address of the Bowler
     *
     */
	@Override
	public void putBowlerInfo(
		String nickName,
		String fullName,
		String email)
		throws IOException, FileNotFoundException {

		String data = nickName + "\t" + fullName + "\t" + email + "\n";

		RandomAccessFile out = new RandomAccessFile(BOWLER_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

    /**
     * Retrieves a list of nicknames in the bowler database
     *
     * @return a Vector of Strings
     * 
     */
	@Override
	public List<String> getBowlers() throws IOException {
	    List<String> allBowlers = new ArrayList<>();
	    for (String[] bowler : readBowlersFromFile()) {
	        allBowlers.add(bowler[0]);
	    }
	    return allBowlers;
	}

}