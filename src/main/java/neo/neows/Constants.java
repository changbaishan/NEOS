package neo.neows;

/**
 * @author Yong Liu
 * 
 * Two important parameters:
 * 
 * (1) total defines all 1019 pages to get the
 * full NASA NEO list;
 * 
 * (2) 1019 page visit exceeds NASA demo key privilege.
 * A registered devkey must be used
 * 
 * For the sake of time, the devkey is stored in an Interface file.
 * The next iteration will save devkey in a property file.
 * 
 * Note that the total value published by NASA changes from day to day
 *
 */
public interface Constants {
	
	public static String devkey = "evfI2fXpMg1DWJbXdSSvc7jcLrwllmir1I3aWW73";
	
	public static int total = 1020;
}
