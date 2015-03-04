package com.redditreader.reddit.tanishqdubey.redditmaterial;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Created by Tanishq Dubey on 1/14/2015.
 * RSS Feed Object*
 */
public class RSSFeed implements Serializable {

    private List<RSSItem> _itemList;
    private static final Long serialVersionUID = 1L;
    private int _itemcount = 0;

    RSSFeed(){
        _itemList = new Vector(0);
    }

    void addItem(RSSItem item){
        _itemList.add(item);
        _itemcount++;
    }

    public RSSItem getItem(int location){
        return _itemList.get(location);
    }

    public int get_itemcount(){
        return _itemcount;
    }
}
