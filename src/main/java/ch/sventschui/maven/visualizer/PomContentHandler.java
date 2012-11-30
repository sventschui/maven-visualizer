package ch.sventschui.maven.visualizer;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class PomContentHandler implements ContentHandler {
    
    private boolean inDependency = false;

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if("dependency".equalsIgnoreCase(localName)) {
            //this.currentDependency = new Dependency();
            this.inDependency = true;
        }
        
        if(this.inDependency) {
            if("artifactId".equalsIgnoreCase(localName)) {
                
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("dependency".equalsIgnoreCase(localName)) {
            this.inDependency = false;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        // TODO Auto-generated method stub

    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        // TODO Auto-generated method stub

    }

    public void processingInstruction(String target, String data) throws SAXException {
        // TODO Auto-generated method stub

    }

    public void skippedEntity(String name) throws SAXException {
        // TODO Auto-generated method stub

    }

}
