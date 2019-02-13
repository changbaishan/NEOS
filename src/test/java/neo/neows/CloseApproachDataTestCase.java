package neo.neows;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Yong Liu
 * 
 * test fromJson method of CloseApproachData class
 *
 */
public class CloseApproachDataTestCase {

	@Test
	public void testFromJson_CloseApproachData() {
		CloseApproachData cad = new CloseApproachData();
		cad.close_approach_date = "1938-08-24";
		cad.miss_distance = new CloseApproachData.Distance();
		cad.miss_distance.kilometers = "63049368";
		String json = cad.toJson();
		JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
		JsonElement elem = jsonObject.get("close_approach_date");

		assertEquals("1938-08-24", elem.toString().replaceAll("^\"|\"$", ""));
		
	}

}
