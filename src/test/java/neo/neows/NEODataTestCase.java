package neo.neows;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Yong Liu
 * 
 * test fromJson method of NEOData class
 *
 */
public class NEODataTestCase {

	@Test
	public void testFromJson_NEOData() {
		NEOData nd = new NEOData();
		nd.neo_reference_id = "2162038";
		nd.absolute_magnitude_h = 16.6;
		nd.designation = "162038";
		nd.estimated_diameter = new NEOData.Diameter();
		nd.estimated_diameter.meters = new NEOData.Meters();
		nd.estimated_diameter.meters.estimated_diameter_max = 2844.7229650327;
		nd.estimated_diameter.meters.estimated_diameter_min = 1272.1987853936;
		nd.name = "162038 (1996 DH)";
		String json = nd.toJson();
		
		JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
		JsonElement elem = jsonObject.get("designation");

		assertEquals("162038", elem.toString().replaceAll("^\"|\"$", ""));
	}

}
