package com.redditreader.reddit.tanishqdubey.redditmaterial;

/**
 * Created by Tanishq Dubey on 2/13/2015.
 * Parses the JSON File*
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {
    private static final String TAG_CHILDREN = "children";
    private static final String TAG_DATA = "data";
    private static final String TAG_DOMAIN = "domain";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SELFTEXT = "selftext";
    private static final String TAG_LINK = "url";
    private static final String TAG_AUTHOR = "author";
    
    JSONArray children = null;
    String jsonReturn = null;
    
    private RSSFeed _feed = new RSSFeed();
    
    public RSSFeed parseJSON(String json){
        
        JSONRetriver jsonRetriver = new JSONRetriver();
        
        jsonReturn = jsonRetriver.getJSONFromUrl(json);

        if (jsonReturn != null){
            try {
                JSONObject jsonObject = new JSONObject(jsonReturn);
                JSONObject dataObject = jsonObject.getJSONObject("data");
                children = dataObject.getJSONArray(TAG_CHILDREN);

                for (int i = 0; i < children.length(); i++){
                    RSSItem _item = new RSSItem();
                    JSONObject c = children.getJSONObject(i);

                    JSONObject data = c.getJSONObject(TAG_DATA);
                        _item.set_description(data.getString(TAG_SELFTEXT));
                        _item.set_title(data.getString(TAG_TITLE));
                        _item.set_link(data.getString(TAG_LINK));
                        _item.set_date(data.getString(TAG_AUTHOR) + " " +data.getString(TAG_DOMAIN));
                        if (data.getString(TAG_SELFTEXT).equals("")){
                            _item.setTextPost(false);
                            _item.set_image(data.getString(TAG_LINK));
                        } else {
                            _item.setTextPost(true);
                            _item.set_image("http://upload.wikimedia.org/wikipedia/commons/c/ce/Transparent.gif");
                        }

                    _feed.addItem(_item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        
        
        return _feed;
    }
}
