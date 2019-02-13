package neo.neows;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import neo.neows.NEOData.Diameter;
import neo.neows.NEOData.Meters;



/**
 * @author Yong Liu
 * 
 * The NEOList visit
 * https://api.nasa.gov/neo/rest/v1/neo/browse?api_key=DEMO_KEY
 * to get the NEOData list
 * 
 * The important thing is to visit all 1019 pages to get the
 * full NASA NEO list
 *
 */
public class NEOList {
	
	final static Logger logger = Logger.getLogger(NEOList.class);

	private List<NEOData> list;

	public NEOList() {
		list = new ArrayList<NEOData>();
		for (int i = 0; i < Constants.total; i++) {
			List<NEOData> sub;
			try {
				sub = pageData(i);
				list.addAll(sub);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public List<NEOData> pageData(int i) throws ClientProtocolException, IOException {
		StringBuilder myurl = new StringBuilder("https://api.nasa.gov/neo/rest/v1/neo/browse?page=");
		myurl.append(i);
		myurl.append("&size=20&api_key=").append(Constants.devkey);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(myurl.toString());
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		StatusLine status = response1.getStatusLine();
		if (status.getStatusCode() == 200) {
			HttpEntity entity1 = response1.getEntity();
			String json = EntityUtils.toString(entity1);
			// System.out.println(json);
			JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
			JsonElement elem = jsonObject.get("near_earth_objects");
			String listContent = elem.toString();
			Type listType = new TypeToken<List<NEOData>>() {
			}.getType();
			return new Gson().fromJson(listContent, listType);
		} else {
			return new ArrayList<NEOData>();
		}
	}

	static class PageList {

		public List<NEOData> near_earth_objects;

		public PageList() {

		}

		public PageList fromJson(String json) {
			return (PageList) new Gson().fromJson(json, PageList.class);
		}

		public String toJson() {
			Type listType = new TypeToken<List<NEOData>>() {
			}.getType();
			return new Gson().toJson(this.near_earth_objects, listType);
		}

	}

	public List<NEOData> getList() {
		return list;
	}

}
