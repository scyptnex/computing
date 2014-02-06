package scyp.io;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class SimpleXML{

	public static Element readXMLFile(File fi) throws ParserConfigurationException, SAXException, IOException{
		//convert to URL
		String path = fi.getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		String url = "file:" + path;
		SimpleXMLReader sx = new SimpleXMLReader();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(sx);
		xmlReader.parse(url);
		return sx.top;
	}

	private static class SimpleXMLReader extends DefaultHandler{
		private Element top = null;
		private Element cur = null;
		public void characters(char[] ch, int start, int length){
			new Chars(cur, new String(ch).substring(start, start + length));
		}
		public void startElement(String uri, String localName, String qName, Attributes attributes){
			String[] aNames = new String[attributes.getLength()];
			String[] aVals = new String[attributes.getLength()];
			for(int i=0; i<aNames.length; i++){
				aNames[i] = attributes.getLocalName(i);
				aVals[i] = attributes.getValue(i);
			}
			cur = new Element(cur, localName, aNames, aVals);
		}
		public void endElement(String uri, String localName, String qName){
			if(cur.parent == null) top = cur;
			cur = cur.parent;
		}
		public void endDocument(){
			assert cur == null;
		}
	}

	public abstract static class Node{
		public final Element parent;
		public Node(Element parent){
			this.parent = parent;
			if(parent != null) this.parent.append(this);
		}
		public abstract void write(PrintStream ps);
	}

	public static class Element extends Node implements Iterable<Node>{
		private final String type;
		private final ArrayList<String> attrNames;
		private final HashMap<String, String> attrValues;
		private final ArrayList<Node> children;
		public Element(Element parent, String type, String[] attrNams, String[] attrVals){
			super(parent);
			assert attrNams.length == attrVals.length;
			this.type = type;
			this.children = new ArrayList<Node>();
			this.attrNames = new ArrayList<String>();
			this.attrValues = new HashMap<String, String>();
			for(int i=0; i<attrNams.length; i++){
				attrNames.add(attrNams[i]);
				attrValues.put(attrNams[i], attrVals[i]);
			}
		}
		public void writeToFile(File fi) throws IOException{
			FileOutputStream fos = new FileOutputStream(fi);
			PrintStream ps = new PrintStream(fos);
			write(ps);
			ps.println();//blank line at end of file
			ps.close();
			fos.close();
		}
		public void write(PrintStream ps){
			ps.print("<" + type);
			for(String an : attrNames){
				//quotation marks are invalid
				ps.print(" " + an + "=\"" + attrValues.get(an) + "\"");
			}
			if(children.size() > 0){
				ps.print(">");
				for(Node chi : this) chi.write(ps);
				ps.print("</" + type + ">");
			}
			else{
				ps.print(" />");
			}
		}
		private void append(Node n){
			children.add(n);
		}
		@Override
		public Iterator<Node> iterator() {
			return children.iterator();
		}
	}

	public static class Chars extends Node{
		public final String chars;
		public Chars(Element parent, String chars){
			super(parent);
			this.chars = chars;
		}
		public void write(PrintStream ps){
			ps.print(chars);
		}
	}

}
