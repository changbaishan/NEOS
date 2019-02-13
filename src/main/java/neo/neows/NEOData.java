package neo.neows;

import com.google.gson.Gson;



/**
 * @author Yong Liu
 * 
 * The NEOData is designed to capture data returned from
 * https://api.nasa.gov/neo/rest/v1/neo/browse?api_key=DEMO_KEY
 * Assumption: estimated_diameter is required measurement for NEO size	
 *
 */
public class NEOData implements Comparable {

	public String neo_reference_id;
	
	public String name;
	
	public String designation;
	
	public String nasa_jpl_url;
	
	public double absolute_magnitude_h;
	
	public Diameter estimated_diameter;

	public NEOData() {
		// TODO Auto-generated constructor stub
	}
	
	public NEOData fromJson(String json) {
		return (NEOData) new Gson().fromJson(json, NEOData.class);
	}
	
	public String toJson() {
		return new Gson().toJson(this, NEOData.class);
	}
	
	static class Diameter  implements Comparable {
		
		public Meters meters;
		
		public Diameter() {
			
		}
		
		
		public Diameter fromJson(String json) {
			return (Diameter) new Gson().fromJson(json, Diameter.class);
		}
		
		public String toJson() {
			return new Gson().toJson(this, Diameter.class);
		}


		public int compareTo(Object arg0) {
			if (arg0 instanceof Diameter) {
				Diameter dia = (Diameter) arg0;
				return (int) this.meters.estimated_diameter_max - (int) dia.meters.estimated_diameter_max;
			} else {
				return -1;
			}
		}
 		
	}
	
	static class Meters {
		
		public double estimated_diameter_min;
		
		public double estimated_diameter_max;
		
		public Meters () {
			
		}
		
		public Meters fromJson(String json) {
			return (Meters) new Gson().fromJson(json, Meters.class);
		}
		
		public String toJson() {
			return new Gson().toJson(this, Meters.class);
		}
 		
	}

	public int compareTo(Object arg0) {
		if (arg0 instanceof NEOData) {
			NEOData dia = (NEOData) arg0;
			return (int) this.estimated_diameter.meters.estimated_diameter_max - (int) dia.estimated_diameter.meters.estimated_diameter_max;
		} else {
			return -1;
		}
	}

}
