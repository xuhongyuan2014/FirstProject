package com.example.first.player;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.first.model.PlayInfo;

public class PlayerAddressHandler extends DefaultHandler  {



		    private ArrayList<PlayInfo> playInfos;

		    private PlayInfo playInfo;

		    private String content;

		 

		    public ArrayList<PlayInfo> getPlayInfos() {

		        return playInfos;

		    }

		 
		
		    @Override
		
		    public void startDocument() throws SAXException {
		
		        super.startDocument();
		
		        playInfos = new ArrayList<PlayInfo>();
		
		        System.out.println("----------Start Parse Document----------" );
		
		    }
		
		     
		
		    @Override
		
		    public void endDocument() throws SAXException {
		
		        System.out.println("----------End Parse Document----------" );
		
		    }
		
		     
		
		    @Override
		
		    public void characters(char[] ch, int start, int length)
		
		            throws SAXException {
		
		        super.characters(ch, start, length);
		
		        // 获得标签中的文本
		
		        content = new String(ch, start, length);
		
		    }
		
		 
		
		    @Override
		
		    public void startElement(String uri, String localName, String qName,
		
		            Attributes attributes) throws SAXException {
		
		        super.startElement(uri, localName, qName, attributes);
		
		        // 打印出localname和qName
		        System.out.println("LocalName->" + localName);
		
		        System.out.println("QName->" + qName);
		
		        if ("playinfo".equals(localName)) {
		
		        	playInfo = new PlayInfo();
		        	
		if(attributes.getValue("id")!=null&&!attributes.getValue("id").equals(""))
		            playInfo.setId(Integer.parseInt(attributes.getValue("id")));
		
		        }
		
		    }
		
		 
		
		    @Override
		
		    public void endElement(String uri, String localName, String qName)
		
		            throws SAXException {
		
		        super.endElement(uri, localName, qName);
		
		        if ("name".equals(localName)) {
		
		            playInfo.setName(content);
		
		        } else if ("address".equals(localName)) {
		
		            playInfo.setAddress(content);
		
		        } else if ("playinfo".equals(localName)) {
		
		            playInfos.add(playInfo);
		
		        }
		
		    }
		
		

}
