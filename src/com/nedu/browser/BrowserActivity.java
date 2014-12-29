package com.nedu.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;



public class BrowserActivity extends Activity {
	private WebView webView;
	private EditText urlBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Request a window feature to show progress bar in the application
		// title
		requestWindowFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.browser);
		setProgressBarVisibility(true);
		


		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {
			// Load opened URL in the application instead of standard browser
			// application
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

	
		webView.setWebChromeClient(new WebChromeClient() {
			// Set progress bar during loading
			public void onProgressChanged(WebView view, int progress) {
				BrowserActivity.this.setProgress(progress * 100);
			}
		});

		// Enable some feature like Javascript and pinch zoom
		WebSettings websettings = webView.getSettings();
		websettings.setJavaScriptEnabled(true);						// Warning! You can have XSS vulnerabilities!
		websettings.setBuiltInZoomControls(true);
	    websettings.setSupportZoom(true);

	    websettings.setJavaScriptCanOpenWindowsAutomatically(true);

	    websettings.setPluginsEnabled(true);//support flash
	    websettings.setUseWideViewPort(true);// 这个很关键
	    websettings.setLoadWithOverviewMode(true);


		urlBox = (EditText) findViewById(R.id.url);
		urlBox.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_ENTER:
						webView.loadUrl(urlBox.getText().toString());
						InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputManager.hideSoftInputFromWindow(
								urlBox.getWindowToken(), 0);
						return true;
					default:
						break;
					}
				}
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Create a menu that is append to main activity
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	

	@Override
	public void onResume() {
		super.onResume();
		// Reload URL
		String bookmarkUrl = getIntent().getStringExtra("url");
		if (bookmarkUrl == null) {
			webView.loadUrl(urlBox.getText().toString());
		} else {
			urlBox.setText(bookmarkUrl);
			webView.loadUrl(bookmarkUrl);
		}
		
	}

	@Override
	protected void onPause() {
		
		super.onPause();
	}

	/*
	 * WebView methods
	 */


	public void reload(View v) {
		webView.reload();
	}

	public void stop(View v) {
		webView.stopLoading();
	}

	public void back(View v) {
		webView.goBack();
	}

	public void forward(View v) {
		webView.goForward();
	}
}
