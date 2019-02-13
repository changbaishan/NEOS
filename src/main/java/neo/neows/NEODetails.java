package neo.neows;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * @author Yong Liu
 * 
 *         The NEODetails is designed to capture NEOdata with a list of
 *         CloseApproachData
 * 
 *         Each NEODetails is contained in a
 *         https://api.nasa.gov/neo/rest/v1/neo/id?api_key=DEMO_KEY page
 * 
 * 
 *         Assumption: the closest being closest for all time listed by NASA
 * 
 */

public class NEODetails extends NEOData {

	final static Logger logger = Logger.getLogger(NEODetails.class);

	public boolean is_potentially_hazardous_asteroid;

	public List<CloseApproachData> datalist;

	public CloseApproachData minimum;

	public NEODetails() {
		// TODO Auto-generated constructor stub
	}

	public NEODetails(NEOData data) {
		this.neo_reference_id = data.neo_reference_id;
		this.absolute_magnitude_h = data.absolute_magnitude_h;
		this.designation = data.designation;
		this.estimated_diameter = data.estimated_diameter;
		this.name = data.name;
		this.nasa_jpl_url = data.nasa_jpl_url;
		this.is_potentially_hazardous_asteroid = true;
	}

	public NEODetails fromJson(String json) {
		NEOData mydata = super.fromJson(json);
		NEODetails mydetails = new NEODetails(mydata);
		JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
		JsonElement elem = jsonObject.get("close_approach_data");
		String listContent = elem.toString();

		Type listType = new TypeToken<List<CloseApproachData>>() {
		}.getType();
		mydetails.datalist = new Gson().fromJson(listContent, listType);
		mydetails.computeMinimum();
		return mydetails;
	}

	public String toJson() {
		return new Gson().toJson(this, NEODetails.class);
	}

	public void computeMinimum() {
		minimum = new CloseApproachData();
		if (datalist.size() > 0) {
			System.out.println(datalist.get(0).toJson());

			minimum.miss_distance.kilometers = datalist.get(0).miss_distance.kilometers;
			minimum.close_approach_date = datalist.get(0).close_approach_date;
			if (datalist.size() > 1) {
				for (int i = 1; i < datalist.size(); i++) {
					try {
						int min = Integer.parseInt(datalist.get(i).miss_distance.kilometers);
						if (min < Integer.parseInt(minimum.miss_distance.kilometers)) {
							minimum.miss_distance.kilometers = datalist.get(i).miss_distance.kilometers;
							minimum.close_approach_date = datalist.get(i).close_approach_date;
						}
					} catch (NumberFormatException ex) {
						int min = (int) Double.parseDouble(datalist.get(i).miss_distance.kilometers);
						int now = (int) Double.parseDouble(minimum.miss_distance.kilometers);
						if (min < now) {
							minimum.miss_distance.kilometers = datalist.get(i).miss_distance.kilometers;
							minimum.close_approach_date = datalist.get(i).close_approach_date;
						}
					}
				}
			}
		}

	}
}
