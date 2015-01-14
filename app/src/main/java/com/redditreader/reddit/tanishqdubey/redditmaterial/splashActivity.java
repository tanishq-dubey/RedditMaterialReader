package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class splashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
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
                new AsyncLoadXMLFeed().execute;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }
}
