package eu.isweb.jspower;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
	
	class HttpConnection extends AsyncTask<String, Void, String> {
        private final HttpClient Client = new DefaultHttpClient();
      
        protected String doInBackground(String... urls) {
            try {
                HttpGet httpget = new HttpGet(urls[0]);
                ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                    public String handleResponse(HttpResponse response)
                            throws HttpResponseException, IOException {
                            StatusLine statusLine = response.getStatusLine();
                            if (statusLine.getStatusCode() >= 300) {
                                throw new HttpResponseException(statusLine.getStatusCode(),
                                        statusLine.getReasonPhrase());
                            }

                            HttpEntity entity = response.getEntity();
                            return entity == null ? null : EntityUtils.toString(entity, "iso-8859-2");
                        }
                    };
                return Client.execute(httpget, responseHandler);
            } catch (ClientProtocolException e) {
                return e.getMessage();
            } catch (IOException e) {
                return e.getMessage();
            }
        }
        
}