package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.util.List;


public class ListActivity extends ActionBarActivity {

    Application myApplication;
    RSSFeed feed;
    ListView lView;
    CustomListAdapter adapter;
    Context mContext;
    RelativeLayout relativeListLayout;
    PullRefreshLayout pullRefreshLayout;
    String feedURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = this;

        myApplication = getApplication();

        feed = (RSSFeed) getIntent().getExtras().get("feed");
        feedURL = (String) getIntent().getExtras().get("url");

        Log.e("Grabbed URL: ", feedURL);

        pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pullToRefresh);

        lView = (ListView) findViewById(R.id.listView);
        lView.setVerticalFadingEdgeEnabled(true);

        adapter = new CustomListAdapter(this);
        lView.setAdapter(adapter);

        lView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("feed", feed);
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("pos",position );
                startActivity(intent);
            }
        });
        
        /*
         *  Initiates the pull to refresh action which calls the refreshList method in this class
         */
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList(feedURL);
            }
        });
    }

    class CustomListAdapter extends BaseAdapter{
        private LayoutInflater layoutInflater;
        public ImageLoader imageLoader;

        public CustomListAdapter(ListActivity activity){
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageLoader = new ImageLoader(activity.getApplicationContext());

        }

        @Override
        public int getCount() {
            return feed.get_itemcount();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;
            int pos = position;
            if(listItem == null){
                listItem = layoutInflater.inflate(R.layout.listitem_layout,null);
            }

            ImageView imageView = (ImageView) listItem.findViewById(R.id.thumbImageView);
            TextView textTitle = (TextView) listItem.findViewById(R.id.titleTextView);
            TextView textDate =(TextView) listItem.findViewById(R.id.dateTextView);
            TextView textDescription = (TextView) listItem.findViewById(R.id.descriptionTextView);

            imageLoader.DisplayImage(feed.getItem(pos).get_image(), imageView);
            relativeListLayout = (RelativeLayout) listItem.findViewById(R.id.listItemLayout);
            
            if (feed.getItem(pos).getTextPost()){
                textDescription.setText(Html.fromHtml(feed.getItem(pos).get_description()));
                imageView.setImageResource(R.drawable.stubreplace);
            }else {
                textDescription.setText("");
                
            }

            textTitle.setText(feed.getItem(pos).get_title());
            textDate.setText(feed.getItem(pos).get_date());

            return listItem;
        }
    }
    
    /*
     *  refreshList
     *  -----------
     *  Arguments:
     *      String refreshURL : The JSON URL that will be parsed to create a new feed array
     *
     *  Description:
     *      This method simply takes a JSON URL and parses it using the JSONParser and sets the
     *      existing feed array to the newly parsed JSON file. Then the list is reset and populated
     *      with the new feed information and any refresh actions are stopped. This all happens in a
     *      new thread to avoid any app hangs
     *      
     *      Can also be used to set a new feed instead of just refreshing as the name suggests
     */
    public void refreshList (final String refreshURL){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.v("About to parse this: ", refreshURL);
                JSONParser tempjsonParser = new JSONParser();
                feed = tempjsonParser.parseJSON(refreshURL);
                
                ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(feed != null && feed.get_itemcount() >0){
                            adapter.notifyDataSetChanged();
                        }
                        pullRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    thread.start();
    }
    
}
