package com.redditreader.reddit.tanishqdubey.redditmaterial;

import org.w3c.dom.Document;
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
                    String nodeName = thisNode.getNodeName();

                    theString = nodeChild.item(j).getFirstChild().getNodeValue();

                    if (theString != null){
                        if ("title".equals(nodeName)){
                            _item.set_title(theString);
                        } else if ("description".equals(nodeName)){
                            _item.set_description(theString);
                        } else if ("pubDate".equals(nodeName)){
                            String formattedDate = theString.replace(" +0000", "");
                            _item.set_date(formattedDate);
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
