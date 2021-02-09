/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.serializer;

import proj78.xml.XmlAttribute;
import proj78.xml.XmlCDataSection;
import proj78.xml.XmlComment;
import proj78.xml.XmlDeclaration;
import proj78.xml.XmlDocument;
import proj78.xml.XmlDocumentType;
import proj78.xml.XmlElement;
import proj78.xml.XmlEntity;
import proj78.xml.XmlException;
import proj78.xml.XmlNode;
import proj78.xml.XmlNotation;
import proj78.xml.XmlProcessingInstruction;
import proj78.xml.XmlText;
import proj78.xml.XmlUtility;

/**
 * Serializes DOM nodes to their textual (i.e., XML) representations
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlSerializer {
	/**
	 * Internal <code>XmlSerializer</code> constructor.
	 */
	/* internal */ XmlSerializer() {
	}
	
	/**
	 * Serializes the specified node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 * @throws XmlException if the node could not be serialized (i.e., 
	 * 	serialization is not supported for that type of node).
	 */
	public final String serialize(XmlNode node) {
		switch (node.getNodeType()) {
			case XmlNode.ELEMENT_NODE:
				return serialize((XmlElement)node);
			case XmlNode.ATTRIBUTE_NODE:
				return serialize((XmlAttribute)node);
			case XmlNode.TEXT_NODE:
				return serialize((XmlText)node);
			case XmlNode.CDATA_SECTION_NODE:
				return serialize((XmlCDataSection)node);
			case XmlNode.PROCESSING_INSTRUCTION_NODE:
				if (node.getNodeName().equals("xml")) {
					try {
						return serialize((XmlDeclaration)node);
					} catch (ClassCastException ex) {
					}
				}
				return serialize((XmlProcessingInstruction)node);
			case XmlNode.COMMENT_NODE:
				return serialize((XmlComment)node);
			case XmlNode.DOCUMENT_TYPE_NODE:
				return serialize((XmlDocumentType)node);
			case XmlNode.DOCUMENT_NODE:
				return serialize((XmlDocument)node);
			case XmlNode.ENTITY_NODE:
				return serialize((XmlEntity)node);
			case XmlNode.NOTATION_NODE:
				return serialize((XmlNotation)node);
			default:
				throw new XmlException("Serialization is not supported for the specified type of node.", XmlException.NOT_SUPPORTED_ERR);
		}
	}
	
	/**
	 * Serializes an attribute node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlAttribute node) {
		String str = String.format("%s=\"", node.getName());
		if (node.hasChildNodes()) {
			str += escapeAmpersands(node.getValue())
					.replace("<", "&lt;")
					.replace("\"", "&quot;");
		}
		str += '"';
		return str;
	}
	
	/**
	 * Serializes an element node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlElement node) {
		StringBuffer str = new StringBuffer();
		str.append('<').append(node.getTagName());
		if (node.hasAttributes()) {
			for (XmlNode attr : node.getAttributes()) {
				str.append(' ').append(serialize(attr));
			}
		}
		if (node.hasChildNodes()) {
			str.append('>');
			for (XmlNode childNode : node.getChildNodes()) {
				str.append(serialize(childNode));
			}
			str.append("</").append(node.getTagName()).append('>');
		} else {
			str.append(" />");
		}
		return str.toString();
	}
	
	/**
	 * Serializes a text node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlText node) {
		if (node.getData() != null) {
			return escapeAmpersands(node.getData())
					.replace("<", "&lt;")
					.replace(">", "&gt;");
		}
		return "";
	}
	
	/**
	 * Serializes a CDATA-section node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlCDataSection node) {
		String str = "<![CDATA[";
		if (node.getData() != null) {
			str += node.getData().replace("]]>", "]]&gt;");
		}
		str += "]]>";
		return str;
	}
	
	/**
	 * Serializes a processing instruction node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlProcessingInstruction node) {
		String str = String.format("<?%s ", node.getTarget());
		if (node.getData() != null) {
			str += node.getData().replace("?>", "?&gt;");
		}
		str += "?>";
		return str;
	}
	
	/**
	 * Serializes an XML declaration node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlDeclaration node) {
		String str = String.format("<?xml version=\"%s\"", node.getVersion());
		if (node.getEncoding() != null) {
			str += String.format(" encoding=\"%s\"", node.getEncoding());
			if (node.getStandalone() != null) {
				str += String.format(" standalone=\"%s\"", node.getStandalone());
			}
		}
		str += "?>";
		return str;
	}
	
	/**
	 * Serializes a comment node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlComment node) {
		String str = "<!--";
		if (node.getData() != null) {
			str += node.getData().replace("-->", "--&gt;");
		}
		str += "-->";
		return str;
	}
	
	/**
	 * Serializes a document type node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlDocumentType node) {
		String str = String.format("<!DOCTYPE %s", node.getName());
		if (node.getPublicId() != null) {
			str += String.format(" PUBLIC \"%s\"", node.getPublicId());
			if (node.getSystemId() != null) {
				str += String.format(" \"%s\"", node.getSystemId());
			}
		} else if (node.getSystemId() != null) {
			str += String.format(" SYSTEM \"%s\"", node.getSystemId());
		}
		if (node.getInternalSubset() != null) {
			str += String.format(" [%s]", node.getInternalSubset());
		}
		str += '>';
		return str;
	}
	
	/**
	 * Serializes a document node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlDocument node) {
		StringBuffer str = new StringBuffer();
		if (node.hasChildNodes()) {
			for (XmlNode childNode : node.getChildNodes()) {
				if (str.length() != 0) {
					str.append(String.format("%n"));
				}
				str.append(serialize(childNode));
			}
		}
		return str.toString();
	}
	
	/**
	 * Serializes an entity node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlEntity node) {
		return ""; // not implemented
	}
	
	/**
	 * Serializes a notation node.
	 * 
	 * @param node the node to serialize.
	 * @return the textual representation of the specified node.
	 */
	public String serialize(XmlNotation node) {
		return ""; // not implemented
	}
	
	/**
	 * Replaces all occurrences of the ampersand character inside a string by 
	 * the corresponding predefined XML entity reference (i.e., "&amp;" becomes 
	 * "&amp;amp;" but the "&amp;" in "&amp;amp;" remains untouched).
	 * 
	 * @param str the string to escape ampersand characters in.
	 * @return a copy of the input string with all occurrences of the ampersand 
	 * 	character replaced by its entity reference equivalent.
	 */
	protected static final String escapeAmpersands(String str) {
		int ampersand, i = 0;
		while ((ampersand = str.indexOf('&', i)) >= 0) {
			int semicolon = str.indexOf(';', ampersand + 1);
			if (semicolon >= 0 && XmlUtility.isEntityReference(str.substring(ampersand, semicolon + 1))) {
				i = semicolon + 1;
			} else {
				i = ampersand + "amp;".length() + 1;
				str = String.format("%s&amp;%s", 
						str.substring(0, ampersand), 
						str.substring(ampersand + 1)
					);
			}
		}
		return str;
	}
}