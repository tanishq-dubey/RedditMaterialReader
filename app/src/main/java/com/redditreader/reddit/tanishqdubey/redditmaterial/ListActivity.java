package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;


@SuppressWarnings("EqualsBetweenInconvertibleTypes")
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
        
        //Set feed to a default value in case feed is bad or something.
        if(feed == null ||feed.get_itemcount() ==0){
            RSSItem defaultNullItem = new RSSItem();
            defaultNullItem.set_title("Error Retrieving Posts");
            defaultNullItem.set_date(" ");
            defaultNullItem.set_image("http://upload.wikimedia.org/wikipedia/commons/c/ce/Transparent.gif");
            defaultNullItem.set_description("Please refresh to try again!");
            feed.addItem(defaultNullItem);
            adapter.notifyDataSetChanged();
        }

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
                if(feed == null){
                    RSSItem defaultNullItem = new RSSItem();
                    defaultNullItem.set_title("Error Retrieving Posts");
                    defaultNullItem.set_date(" ");
                    defaultNullItem.set_image("http://upload.wikimedia.org/wikipedia/commons/c/ce/Transparent.gif");
                    defaultNullItem.set_description("Please refresh to try again!");
                    feed.addItem(defaultNullItem);
                    adapter.notifyDataSetChanged();
                }
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
        
        class feedViewHolder{
            
            ImageView imageView;
            TextView textTitle;
            TextView textDate;
            TextView textDescription;
            
            feedViewHolder(View v){
                imageView = (ImageView) v.findViewById(R.id.thumbImageView);
                textTitle = (TextView) v.findViewById(R.id.titleTextView);
                textDate =(TextView) v.findViewById(R.id.dateTextView);
                textDescription = (TextView) v.findViewById(R.id.descriptionTextView);
                
            }
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View listItem = convertView;
            feedViewHolder holder;
            
            
            if(listItem == null){
                listItem = layoutInflater.inflate(R.layout.listitem_layout,null);
                holder= new feedViewHolder(listItem);
                listItem.setTag(holder);
            }else {
                holder = (feedViewHolder) listItem.getTag();
            }

//            ImageView imageView = (ImageView) listItem.findViewById(R.id.thumbImageView);
//            TextView textTitle = (TextView) listItem.findViewById(R.id.titleTextView);
//            TextView textDate =(TextView) listItem.findViewById(R.id.dateTextView);
//            TextView textDescription = (TextView) listItem.findViewById(R.id.descriptionTextView);

            imageLoader.DisplayImage(feed.getItem(position).get_image(), holder.imageView);
            relativeListLayout = (RelativeLayout) listItem.findViewById(R.id.listItemLayout);
            
            if (feed.getItem(position).getTextPost()){
                holder.textDescription.setText(Html.fromHtml(feed.getItem(position).get_description()));
                holder.imageView.setImageResource(R.drawable.stubreplace);
            }else {
                holder.textDescription.setText("");
                
            }

            holder.textTitle.setText(feed.getItem(position).get_title());
            holder.textDate.setText(feed.getItem(position).get_date());

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
                        }else{
                            RSSItem defaultNullItem = new RSSItem();
                            defaultNullItem.set_title("Error Retrieving Posts");
                            defaultNullItem.set_date(" ");
                            defaultNullItem.set_image("http://upload.wikimedia.org/wikipedia/commons/c/ce/Transparent.gif");
                            defaultNullItem.set_description("Please refresh to try again!");
                            feed.addItem(defaultNullItem);
                            adapter.notifyDataSetChanged();
                        }
                        pullRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        
        inflater.inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                final AlertDialog.Builder alert = new AlertDialog.Builder(this);

                
                alert.setTitle("Subreddit Search");
                alert.setMessage("Enter a Subreddit:");
                
                final EditText searchText = new EditText(this);
                alert.setView(searchText);
                
                alert.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            if((searchText.getText() != null) || searchText.getText().equals("")){
                                String urlText = "http://www.reddit.com/r/" + searchText.getText() + "/.json";
                                feedURL = urlText;
                                refreshList(urlText);
                                dialog.dismiss();
                            }else{
                                dialog.dismiss();
                            }
                        }catch (NullPointerException e){
                            Log.e("Error in getting text", "error");
                        }

                    }
                });
                
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        
                    }
                });
                alert.show();
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    

}
