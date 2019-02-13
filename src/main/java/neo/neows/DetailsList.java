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

/**
 * @author Yong Liu
 * 
 *         The NEOList visit
 *         https://api.nasa.gov/neo/rest/v1/neo/id?api_key=DEMO_KEY to get the
 *         NEODetails list
 * 
 *         The important thing is to retrieve all 20390 NEOs to get the closest
 *         This requires 20390 visits of
 *         https://api.nasa.gov/neo/rest/v1/neo/page?api_key=DEMO_KEY pages
 * 
 *
 */
public class DetailsList {

	final static Logger logger = Logger.getLogger(DetailsList.class);

	private List<NEODetails> details;

	public DetailsList() {

	}

	public DetailsList(List<NEOData> data) {
		details = new ArrayList<NEODetails>();
		for (int i = 100; i < 5000; i++) {
			NEOData neo = data.get(i);
			try {
				NEODetails detail = computeDetails(neo);
				System.out.println(detail.toJson());
				if (detail.datalist != null && detail.datalist.size() > 0) {

					details.add(detail);
				} else {
					System.out.println(neo.neo_reference_id + " failed");
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public NEODetails computeDetails(NEOData neo) throws ClientProtocolException, IOException {
		StringBuilder myurl = new StringBuilder("https://api.nasa.gov/neo/rest/v1/neo/");
		myurl.append(neo.neo_reference_id);
		myurl.append("?api_key=").append(Constants.devkey);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(myurl.toString());
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		StatusLine status = response1.getStatusLine();
		if (status.getStatusCode() == 200) {
			HttpEntity entity1 = response1.getEntity();
			String json = EntityUtils.toString(entity1);

			NEODetails obj = new NEODetails();
			NEODetails mydetails = obj.fromJson(json);

			return mydetails;
		} else {
			return new NEODetails(neo);

		}
	}

	public NEODetails closest() {
		NEODetails mydetail = details.get(0);
		for (int i = 1; i < details.size(); i++) {
			NEODetails detail = details.get(i);
			if (detail.datalist.size() > 0) {
				int old = (int) Double.parseDouble(mydetail.minimum.miss_distance.kilometers);
				int now = (int) Double.parseDouble(detail.minimum.miss_distance.kilometers);
				if (now < old) {
					mydetail = detail;
				}
			}
		}

		return mydetail;
	}

	public List<NEODetails> getDetails() {
		return details;
	}

}
