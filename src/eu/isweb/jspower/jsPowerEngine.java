package eu.isweb.jspower;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

public class jsPowerEngine {
	static String version = "1.0";
	
    Activity mActivity;
    jsPowerEngine instance;
    WebView browser;
    ArrayList<String> logList = new ArrayList<String>();

    jsPowerEngine(Activity a, WebView b) {
        mActivity = a;
        browser = b;
        instance = this;
    }
    
    private void callback(boolean parse, String funcion, String... parms) {
    	String command = funcion + "(";
    	
    	for (String parm : parms) {
    		if (parse) {
	    		parm = parm.replaceAll("\"","\\\\\"");
	    		parm = parm.replaceAll("(\r\n|\n)", "");
				command +=  "\"" + parm + "\",";
    		}else{
    			command +=  parm + ",";
    		}
        }
    	
    	command = command.substring(0,command.length()-1);
    	command += ");";

    	this.execute(command);
    }
    
    private void execute(final String command) {
    	browser.post(new Runnable() {
    		public void run() { 
    			browser.loadUrl("javascript:" + command); 
    		}  
        }); 
    }
    
    public void parseHTMLtag(String code, String tag, final String callback) {
    	new JsoupConnection(){
    		protected void onPostExecute(String result) {
    			instance.callback(false, callback, result);
    		};
    	}.execute(code, tag);
    }

    public void alert(String toast) {
        Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
    }
    
    public void getURLContent(final String url, final String callback) {
    	new HttpConnection(){
    		protected void onPostExecute(String result) {
    			instance.callback(true, callback, result);
    		};
    	}.execute(url);
    }
    
    public void log(String message) {
    	logList.add(message);
    	Log.d(IDs.JD, message);
    }
    
    public ArrayList<String> getLogs() {
    	return this.logList;
    }
    
    public String getVersion() {
    	return jsPowerEngine.version;

    }

	public void clearLogs() {
		this.logList.clear();
	}
    
}