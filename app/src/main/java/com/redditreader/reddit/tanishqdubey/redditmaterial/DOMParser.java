package com.redditreader.reddit.tanishqdubey.redditmaterial;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Tanishq Dubey on 1/14/2015.
 */
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
            }
        }
    }

}
