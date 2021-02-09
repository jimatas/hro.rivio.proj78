/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser2;

import java.util.Map;
import java.util.Map.Entry;

import proj78.xml.XmlCDataSection;
import proj78.xml.XmlComment;
import proj78.xml.XmlDocument;
import proj78.xml.XmlElement;
import proj78.xml.XmlException;
import proj78.xml.XmlNode;
import proj78.xml.XmlText;
import proj78.xml.XmlUtility;

/**
 * An {@link proj78.xml.parser2.XmlParseEventListener XmlParseEventListener} implementation 
 * that builds a DOM tree.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlDocumentBuilder implements XmlParseEventListener {
	/**
	 * The node that is currently active (being added, inserted into, etc).
	 */
	private XmlNode currentNode;
	
	/**
	 * Indicates whether to print debug output.
	 */
	private boolean debug;
	
	/**
	 * Initializes a new instance of the <code>XmlDocumentBuilder</code> class.
	 * 
	 * @param doc the document node in which the DOM tree will be constructed.
	 */
	public XmlDocumentBuilder(XmlDocument doc) {
		this(doc, false);
	}
	
	/**
	 * Initializes a new instance of the <code>XmlDocumentBuilder</code> class.
	 * 
	 * @param doc the document node in which the DOM tree will be constructed.
	 * @param debug a boolean value indicating whether to output debug messages.
	 */
	public XmlDocumentBuilder(XmlDocument doc, boolean debug) {
		while (doc.hasChildNodes()) {
			doc.removeChild(doc.getFirstChild());
		}
		currentNode = doc;
		this.debug = debug;
	}
	
	public void onXmlDeclaration(String version, String encoding, String standalone) {
		if (debug) {
			System.out.printf("onXmlDeclaration(version=%s, encoding=%s, standalone=%s)%n", version, encoding, standalone);
		}
		if (currentNode.getNodeType() != XmlNode.DOCUMENT_NODE || currentNode.hasChildNodes()) {
			throw new XmlException("Markup before XML declaration.", XmlException.MALFORMED_XML_ERR);
		}
		currentNode.appendChild(((XmlDocument)currentNode).getDomImplementation()
				.createXmlDeclaration(version, encoding, standalone));
	}
	
	public void onDoctypeDeclaration(String dtdName, String publicId, String systemId, String internalSubset) {
		if (debug) {
			System.out.printf("onDoctypeDeclaration(dtdName=%s, publicId=%s, systemId=%s, internalSubset=%s)%n", dtdName, publicId, systemId, internalSubset);
		}
		if (currentNode.getNodeType() != XmlNode.DOCUMENT_NODE 
				|| (currentNode.hasChildNodes() 
						&& currentNode.getLastChild().getNodeType() != XmlNode.PROCESSING_INSTRUCTION_NODE 
						&& currentNode.getLastChild().getNodeType() != XmlNode.COMMENT_NODE)) {
			throw new XmlException("Invalid markup before DOCTYPE declaration.", XmlException.MALFORMED_XML_ERR);
		}
		currentNode.appendChild(((XmlDocument)currentNode).getDomImplementation()
				.createDocumentType(dtdName, publicId, systemId, internalSubset));
	}
	
	public void onTagStart(String tagName, Map<String, String> attrs) {
		if (debug) {
			System.out.printf("onTagStart(tagName=%s, attrs=%s)%n", tagName, attrs.toString());	
		}
		XmlElement element;
		if (currentNode.getNodeType() == XmlNode.DOCUMENT_NODE) {
			element = ((XmlDocument)currentNode).createElement(tagName);
		} else {
			element = currentNode.getOwnerDocument().createElement(tagName);
		}
		for (Entry<String, String> attr : attrs.entrySet()) {
			element.setAttribute(attr.getKey(), attr.getValue());
		}
		currentNode = currentNode.appendChild(element);
	}
	
	public void onTagEnd(String tagName) {
		if (debug) {
			System.out.printf("onTagEnd(tagName=%s)%n", tagName);
		}
		if (!currentNode.getNodeName().equals(tagName)) {
			throw new XmlException(String.format("Mismatched tag name '%s'; '%s' expected.", tagName, currentNode.getNodeName()), XmlException.MALFORMED_XML_ERR);
		}
		currentNode = currentNode.getParentNode();
	}
	
	public void onProcessingInstruction(String target, String data) {
		if (debug) {
			System.out.printf("onProcessingInstruction(target=%s, data=%s)%n", target, data);
		}
		if (currentNode.getNodeType() == XmlNode.DOCUMENT_NODE) {
			currentNode.appendChild(((XmlDocument)currentNode).createProcessingInstruction(target, data));
		} else {
			currentNode.appendChild(currentNode.getOwnerDocument().createProcessingInstruction(target, data));
		}
	}
	
	public void onCDataSection(String content) {
		if (debug) {
			System.out.printf("onCDataSection(content=%s)%n", content);
		}
		if (currentNode.hasChildNodes() && currentNode.getLastChild().getNodeType() == XmlNode.CDATA_SECTION_NODE) {
			((XmlCDataSection)currentNode).appendData(content);
		} else if (currentNode.getNodeType() == XmlNode.DOCUMENT_NODE) {
			currentNode.appendChild(((XmlDocument)currentNode).createCDataSection(content));
		} else {
			currentNode.appendChild(currentNode.getOwnerDocument().createCDataSection(content));
		}
	}
	
	public void onComment(String content) {
		if (debug) {
			System.out.printf("onComment(content=%s)%n", content);
		}
		if (currentNode.hasChildNodes() && currentNode.getLastChild().getNodeType() == XmlNode.COMMENT_NODE) {
			((XmlComment)currentNode).appendData(content);
		} else if (currentNode.getNodeType() == XmlNode.DOCUMENT_NODE) {
			currentNode.appendChild(((XmlDocument)currentNode).createComment(content));
		} else {
			currentNode.appendChild(currentNode.getOwnerDocument().createComment(content));
		}
	}
	
	public void onCharacters(String chars) {
		if (debug) {
			System.out.printf("onCharacters(chars=%s)%n", chars);
		}
		if (currentNode.hasChildNodes() && currentNode.getLastChild().getNodeType() == XmlNode.TEXT_NODE) {
			((XmlText)currentNode.getLastChild()).appendData(chars);
		} else if (currentNode.getNodeType() != XmlNode.DOCUMENT_NODE) {
			currentNode.appendChild(currentNode.getOwnerDocument().createTextNode(chars));
		} else if (!XmlUtility.isWhitespace(chars)) {
			currentNode.appendChild(((XmlDocument)currentNode).createTextNode(chars));
		}
	}
}