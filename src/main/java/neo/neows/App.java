package neo.neows;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Modification of an Eclipse generated class
 * Serves as driver/example of my API. 
 * The API is designed for meeting the following three requirements.
 * 
 * (1)  browseList() meets the requirement:
 * Write a Java application to get a list of “Near Earth Objects” 
 * using the NASA RESTful Web Service https://api.nasa.gov/api.html#NeoWS
 * 
 * (2) largestinSize() meets the requirement:
 * Identify which NEO is the largest in size, 
 * output the details retrieved for the largest identified.
 * 
 * (3) closestToEarth() meets the requirement:
 * Identify which NEO is closest to earth, 
 * output the details retrieved for the closest identified.
 *
 */

public class App 
{
	final static Logger logger = Logger.getLogger(App.class);
	
	/*
	 * browseList() meets the requirement:
	 * Write a Java application to get a list of “Near Earth Objects” 
	 * using the NASA RESTful Web Service https://api.nasa.gov/api.html#NeoWS
	 * 
	 * See NEOList.json
	 */
	public static void browseList() {
		NEOList list = new NEOList();
    	List<NEOData> mylist = list.getList();
    	Type listType = new TypeToken<List<NEOData>>() {}.getType();
    	String json = new Gson().toJson(mylist, listType);
    	logger.info(json);
	}
	
	/*
	 * largestinSize() meets the requirement:
	 * Identify which NEO is the largest in size, 
	 * output the details retrieved for the largest identified.
	 * 
	 * Estimated diameter is not uniformly present.
	 * It is essential to filter out null elements.
	 * 
	 * see LargestInSize.json
	 */
	public static NEOData largestinSize() {
		NEOList list = new NEOList();
    	List<NEOData> mylist = list.getList();
		List<NEOData> all = new ArrayList<NEOData>();
		for (int i = 0; i < mylist.size(); i++) {
			NEOData dt = mylist.get(i);
			NEOData.Diameter dia = dt.estimated_diameter;
			if (dia != null) {
				NEOData.Meters meters = dia.meters;
				if (meters != null) {
					all.add(dt);
				}
			}
		}
		Collections.sort(all);
    	Collections.reverse(all);
    	return all.get(0);
	}
	
	/*
	 * (3) closestToEarth() meets the requirement:
	 * Identify which NEO is closest to earth,
	 * output the details retrieved for the closest identified.
	 * 
	 * Minimum may be missing in NASA data.
	 * It is important to exclude missing data
	 * see ClosestToEarth.json
	 */
	
	public static NEODetails closestToEarth() {
		NEOList list = new NEOList();
    	List<NEOData> mylist = list.getList();
    	DetailsList samples = new DetailsList(mylist);
		List<NEODetails> mydetails =  samples.getDetails();
		List<NEODetails> details = new ArrayList<NEODetails>();
		for (int i = 0; i < mydetails.size(); i++) {
			NEODetails mydetail = mydetails.get(i);
			if ((mydetail.datalist != null ) && (mydetail.datalist.size()>0)){
				if ((mydetail.minimum != null) && (mydetail.minimum.miss_distance != null)) {
					details.add(mydetail);
				}
			}
		}
		NEODetails mydetail = details.get(0);
		for (int i = 1; i < details.size(); i++) {
			NEODetails detail = details.get(i);
			if (detail.minimum !=null) {
				int old =   (int) Double.parseDouble(mydetail.minimum.miss_distance.kilometers);
				int now =   (int) Double.parseDouble(detail.minimum.miss_distance.kilometers);
				if (now < old) {
					mydetail = detail;
				}
			}
		}
		return mydetail;
	}
	
	
    public static void main( String[] args )
    {
    	// (1) Produce the NEO list, takes about 6-7 minutes on my computer
    	browseList();
    	
    	// (2) Compute the largest in size,  takes about 6-7 minutes on my computer
    	NEOData large = largestinSize();
    	logger.info(large.toJson());
    	
    	// (3) Compute the closest to earth,  takes about 10 minutes on my computer
    	NEODetails detail = closestToEarth();
    	logger.info(detail.toJson());
    	
    }
}
