package com.redditreader.reddit.tanishqdubey.redditmaterial;

import java.io.Serializable;

/**
 * Created by Tanishq Dubey on 1/14/2015.
 */
public class RSSItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String _title = null;
    private String _description = null;
    private String _date = null;
    private String _image = null;
    private String _link = null;

    public String get_link() {
        return _link;
    }

    public void set_link(String link) {
      _link = link;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String title) {
        _title = title;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String description) {
        _description = description;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String pubdate) {
        _date = pubdate;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String image) {
        _image = image;
    }


}
