package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ListActivity extends ActionBarActivity {

    Application myApplication;
    RSSFeed feed;
    ListView lView;
    CustomListAdapter adapter;
    Context mContext;
    RelativeLayout relativeListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = this;

        myApplication = getApplication();

        feed = (RSSFeed) getIntent().getExtras().get("feed");
        
        

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

            imageLoader.DisplayImage(feed.getItem(pos).get_image(), imageView);
            relativeListLayout = (RelativeLayout) listItem.findViewById(R.id.listItemLayout);
            relativeListLayout.setBackgroundColor(feed.getItem(pos).get_backGroundColor());
            
            textTitle.setText(feed.getItem(pos).get_title());
            textTitle.setTextColor(feed.getItem((pos)).getTextColor());
            textDate.setText(feed.getItem(pos).get_date());
            textDate.setTextColor(feed.getItem(pos).getTextColor());

            return listItem;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
