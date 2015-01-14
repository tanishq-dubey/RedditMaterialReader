package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    RSSFeed feed;
    TextView titleView;
    WebView descriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        feed = (RSSFeed) getIntent().getExtras().get("feed");
        int pos = getIntent().getExtras().getInt("pos");

        titleView = (TextView) findViewById(R.id.detailTitleText);
        descriptionView =(WebView) findViewById(R.id.detailWebView);

        WebSettings webSettings =  descriptionView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.getPluginState();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        titleView.setText(feed.getItem(pos).get_title());
        descriptionView.loadUrl("https://www.google.com/");
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
