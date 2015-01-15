package com.redditreader.reddit.tanishqdubey.redditmaterial;

import android.graphics.Color;
import android.support.v7.graphics.Palette;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class DOMParser {

    private RSSFeed _feed = new RSSFeed();

    public RSSFeed parseXml(String xml){
        URL url = null;

        try{
            url = new URL(xml);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try{
            DocumentBuilderFactory documentBuilderFactory;
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("item");
            int length = nodeList.getLength();

            for (int i = 0; i < length; i++){
                Node currentNode = nodeList.item(i);
                RSSItem _item = new RSSItem();

                NodeList nodeChild = currentNode.getChildNodes();
                int childNodeLength = nodeChild.getLength();

                for (int j = 0; j < childNodeLength; j = j + 1){
                    Node thisNode = nodeChild.item(j);
                    String theString = null;
                    String imageURL = null;
                    String nodeName = thisNode.getNodeName();

                    if ("media:thumbnail".equals(nodeName)){
                        imageURL = nodeChild.item(j).getAttributes().getNamedItem("url").getNodeValue();
                        _item.set_image(imageURL);
                        Palette palette = Palette.generate(ImageLoader.simpleGetBitmapFromURL(imageURL));
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if (swatch != null){
                            _item.set_backGroundColor(swatch.getRgb());
                            _item.setTextColor(swatch.getTitleTextColor());
                        }else {
                            _item.set_backGroundColor(Color.GRAY);
                            _item.setTextColor(Color.DKGRAY);
                        }

                        theString = null;
                    }else {
                        theString = nodeChild.item(j).getFirstChild().getNodeValue();
                    }

                    if (theString != null){
                        if ("title".equals(nodeName)){
                            _item.set_title(theString);
                        } else if ("description".equals(nodeName)){
                            _item.set_description(theString);

                            //In case no 'media:thumbnail' comes up, placeholder image.
                            _item.set_image("http://www.imagemagick.org/Usage/canvas/gradient_bilinear.jpg");
                        } else if ("pubDate".equals(nodeName)){
                            String formattedDate = theString.replace(" +0000", "");
                            _item.set_date(formattedDate);
                        } else if ("link".equals(nodeName)){
                            _item.set_link(theString);
                        }
                    }
                }
                _feed.addItem(_item);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return _feed;
    }

}
