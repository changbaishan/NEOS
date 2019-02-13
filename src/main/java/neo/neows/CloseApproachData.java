package neo.neows;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * @author Yong Liu
 * 
 * The CloseApproachData is designed to capture data distance of a NEO
 * 
 * 	
 *
 */

public class CloseApproachData {
	
	final static Logger logger = Logger.getLogger(CloseApproachData.class);

	public String close_approach_date;
	
	public Distance miss_distance;
	
	
	public CloseApproachData() {
		miss_distance = new Distance();
	}
	
	static class Distance implements Comparable {
		
		public String kilometers;
		
		public Distance() {
			
		}

		public int compareTo(Object arg0) {
			if (arg0 instanceof Distance) {
				Distance dist = (Distance) arg0;
				return Integer.parseInt(this.kilometers) - Integer.parseInt(dist.kilometers);
			} else {
				return -1;
			}
		}
		
		public Distance fromJson(String json) {
			return (Distance) new Gson().fromJson(json, Distance.class);
		}
		
		public String toJson() {
			return new Gson().toJson(this, Distance.class);
		}
	}
	
	public CloseApproachData fromJson(String json) {
		return (CloseApproachData) new Gson().fromJson(json, CloseApproachData.class);
	}
	
	public String toJson() {
		return new Gson().toJson(this, CloseApproachData.class);
	}

}
