package scyp.io;

import java.io.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class HTMLParse extends DefaultHandler{
	
	public HTMLParse(InputStream is) throws IOException, SAXException, ParserConfigurationException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(this);
		//xmlReader.parse(new File("tpb.html"));
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes){
		System.out.println("Element: " + uri + " - " + localName);
	}
	
	@Override
	public void startDocument(){
		System.out.println("doc start");
	}
	
	@Override
	public void endDocument(){
		System.out.println("doc end");
	}
	
}
