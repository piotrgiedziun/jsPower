package eu.isweb.jspower;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;
	
	class JsoupConnection extends AsyncTask<String, Void, String> {
	
		protected String doInBackground(String... attributes) {
        	Document doc = null;
        	ArrayList<String> result = new 	ArrayList<String>();
        	if(attributes[0].substring(0,4).equals("http")) {
        		try {
					doc = Jsoup.connect(attributes[0]).get();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}else{
        		doc = Jsoup.parse(attributes[0]);
        	}
			Elements elements = doc.select(attributes[1]);
			for (Element element : elements) {
				JSONObject json = new JSONObject();
				try {
					json.put("html", element.outerHtml());
					json.put("text", element.text());
					json.put("attributes", element.attributes());
				} catch (JSONException e) {
				}
			    result.add(json.toString());
			}
			Log.d(IDs.JD, new JSONArray(result).toString());
			return result.toString();
        }
        
}