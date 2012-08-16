package eu.isweb.jspower;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class WebViewActivity extends Activity {

	public static boolean DEVELOPER_MODE = true;
	WebView browser;
	WebSettings browserSettings;
	jsPowerEngine jsPower;
	SharedPreferences pref;
	String URL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!DEVELOPER_MODE)
			//remove application title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_web_view);

		browser = (WebView) findViewById(R.id.webBrowserInstance);
		
		jsPower = new jsPowerEngine(this, browser);
		
		browser.addJavascriptInterface(jsPower, IDs.jsPower);
		browser.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				jsPower.log("error wihile loading page");
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		browser.setWebChromeClient(new WebChromeClient() {
			  public void onConsoleMessage(String message, int lineNumber, String sourceID) {
				  jsPower.log(message + "line " + lineNumber + " of " + sourceID);
			  }
		});
		
		browserSettings = browser.getSettings();
		browserSettings.setJavaScriptEnabled(true);
		
		if(DEVELOPER_MODE) {
			connectIPMode(false);
		}else{
			browser.loadUrl("file:///android_asset/index.html");
		}
	}
	
	private void showErrorList() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(jsPower.getLogs().toString())
		       .setCancelable(false)
		       .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		           }
		       })
		       .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		                jsPower.clearLogs();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void connectIPMode(boolean force) {
		pref = getSharedPreferences(IDs.devIP, Activity.MODE_PRIVATE);
		
		if (pref.getString("ip","") != "" && force == false) {
			browser.loadUrl("http://" + pref.getString("ip","") + "/index.html");
			return;
		}
		
		LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
        final EditText ipAddress = (EditText)textEntryView.findViewById(R.id.ip);
        
        AlertDialog alert = new AlertDialog.Builder(WebViewActivity.this)
            .setTitle(R.string.set_ip)
            .setView(textEntryView)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	SharedPreferences.Editor editor = pref.edit();
                	editor.putString("ip", ipAddress.getText().toString());
                	editor.commit();
                	URL = "http://"+ipAddress.getText().toString() + "/index.html";
                	browser.loadUrl("http://"+ipAddress.getText().toString() + "/index.html");
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	URL = "file:///android_asset/index.html";
                	browser.loadUrl("file:///android_asset/index.html");
                }
            })
            .create();
        	alert.show();

        	ipAddress.setText(pref.getString("ip",""));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {
	    	browser.goBack();
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
	        case R.id.menu_refresh:
	            browser.reload();
	            return true;
	        case R.id.menu_changeIP:
	        	connectIPMode(true);
	            return true;
	        case R.id.menu_home:
	        	browser.loadUrl(URL);
	            return true;
	        case R.id.menu_error:
	        	showErrorList();
	            return true;
    	}
    	return super.onOptionsItemSelected(item);
    }
}
