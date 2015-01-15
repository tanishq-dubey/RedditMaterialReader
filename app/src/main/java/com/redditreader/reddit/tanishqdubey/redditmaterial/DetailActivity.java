package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    RSSFeed feed;
    WebView descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        feed = (RSSFeed) getIntent().getExtras().get("feed");
        int pos = getIntent().getExtras().getInt("pos");

        descriptionView =(WebView) findViewById(R.id.detailWebView);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Material Reddit");
        actionBar.setSubtitle(feed.getItem(pos).get_title());

        WebSettings webSettings =  descriptionView.getSettings();
        webSettings.getPluginState();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);

        descriptionView.setWebViewClient(new WebViewClient());

        descriptionView.loadUrl(feed.getItem(pos).get_link());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
