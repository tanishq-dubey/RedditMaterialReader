package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class splashActivity extends ActionBarActivity {
    
    public String FEEDNAME = "pics";
    private String RESSFEEDURL = "http://www.reddit.com/r/"+ FEEDNAME +".rss";

    RSSFeed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() == null &&
                !connectivityManager.getActiveNetworkInfo().isConnected() &&
                !connectivityManager.getActiveNetworkInfo().isAvailable()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Couldn't connect to Reddit, try again later?")
                    .setCancelable(false)
                    .setPositiveButton("Exit, and hope for better internet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            new AsyncLoadXMLFeed().execute();
        }
    }


    private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            DOMParser parser = new DOMParser();
            feed = parser.parseXml(RESSFEEDURL);
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);

            Bundle bundle = new Bundle();
            bundle.putSerializable("feed", feed);

            Intent intent = new Intent(splashActivity.this, com.redditreader.reddit.tanishqdubey.redditmaterial.ListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

            finish();
        }
    }
}
