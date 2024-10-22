/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.serializer2;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
 * Serializes DOM nodes to their textual (i.e., XML) representation.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlSerializer {
	/**
	 * Indicates whether the generated XML output is to be formatted for readability.
	 */
	private boolean formatting;
	
	/**
	 * Initializes a new instance of the <code>XmlSerializer</code> class.
	 */
	public XmlSerializer() {
		this(false);
	}
	
	/**
	 * Initializes a new instance of the <code>XmlSerializer</code> class.
	 * 
	 * @param formatting a boolean value indicating whether the XML generated by 
	 * 	this serializer is to be formatted for readability (i.e., "pretty printed").
	 */
	public XmlSerializer(boolean formatting) {
		this.formatting = formatting;
	}
	
	/**
	 * Serializes a node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the node could not be 
	 * 	serialized (i.e., serialization of the specified type of node is not supported).<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public final void serialize(XmlNode node, Writer out) {
		switch (node.getNodeType()) {
			case XmlNode.ELEMENT_NODE:
				serialize((XmlElement)node, out);
				break;
			case XmlNode.ATTRIBUTE_NODE:
				serialize((XmlAttribute)node, out);
				break;
			case XmlNode.TEXT_NODE:
				serialize((XmlText)node, out);
				break;
			case XmlNode.CDATA_SECTION_NODE:
				serialize((XmlCDataSection)node, out);
				break;
			case XmlNode.PROCESSING_INSTRUCTION_NODE:
				if (node.getNodeName().equals("xml")) {
					try {
						serialize((XmlDeclaration)node, out);
						break;
					} catch (ClassCastException ex) {
					}
				}
				serialize((XmlProcessingInstruction)node, out);
				break;
			case XmlNode.COMMENT_NODE:
				serialize((XmlComment)node, out);
				break;
			case XmlNode.DOCUMENT_TYPE_NODE:
				serialize((XmlDocumentType)node, out);
				break;
			case XmlNode.DOCUMENT_NODE:
				serialize((XmlDocument)node, out);
				break;
			case XmlNode.ENTITY_NODE:
				serialize((XmlEntity)node, out);
				break;
			case XmlNode.NOTATION_NODE:
				serialize((XmlNotation)node, out);
				break;
			default:
				throw new XmlException("Serialization is not supported for the specified type of node.", XmlException.NOT_SUPPORTED_ERR);
		}
	}
	
	/**
	 * Serializes a node to the specified byte stream.
	 * 
	 * @param node the node to serialize.
	 * @param out an {@link java.io.OutputStream OutputStream} object representing the byte stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the node could not be 
	 * 	serialized (i.e., serialization of the specified type of node is not supported).<br />
	 * 	<em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified byte stream.
	 */
	public final void serialize(XmlNode node, OutputStream out) {
		serialize(node, new OutputStreamWriter(out));
	}
	
	/**
	 * Serializes an attribute node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlAttribute node, Writer out) {
		appendTo(out, node.getName(), "=\"");
		if (node.hasChildNodes()) {
			String value = escapeAmpersands(node.getValue()).replace("<", "&lt;").replace("\"", "&quot;");
			if (formatting) {
				value = normalizeWhitespace(value);
			}
			appendTo(out, value);
		}
		appendTo(out, "\"");
	}
	
	/**
	 * Serializes an element node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlElement node, Writer out) {
		serialize(node, out, 0);
	}
	
	/**
	 * Helper method for <code>serialize(XmlElement, Writer)</code>.
	 */
	private void serialize(XmlElement node, Writer out, int depth) {
		appendTo(out, "<", node.getTagName());
		if (node.hasAttributes()) {
			for (XmlNode attr : node.getAttributes()) {
				appendTo(out, " ");
				serialize(attr, out);
			}
		}
		if (node.hasChildNodes()) {
			appendTo(out, ">");
			depth++;
			for (XmlNode childNode : node.getChildNodes()) {
				if (childNode.getNodeType() == XmlNode.TEXT_NODE) {
					if (!(formatting && ((XmlText)childNode).isElementContentWhitespace())) {
						if (formatting && containsNonTextNodes(node)) {
							appendTo(out, String.format("%n"), getIndentation(depth));
						}
						serialize(childNode, out);
					}
				} else {
					if (formatting && containsNonTextNodes(node)) {
						appendTo(out, String.format("%n"), getIndentation(depth));
					}
					if (childNode.getNodeType() == XmlNode.ELEMENT_NODE) {
						serialize((XmlElement)childNode, out, depth);
					} else {
						serialize(childNode, out);
					}
				}
			}
			depth--;
			if (formatting && containsNonTextNodes(node)) {
				appendTo(out, String.format("%n"), getIndentation(depth));
			}
			appendTo(out, "</", node.getTagName(), ">");
		} else {
			appendTo(out, " />");
		}
	}
	
	/**
	 * Serializes a text node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlText node, Writer out) {
		if (node.getData() != null) {
			String data = escapeAmpersands(node.getData()).replace("<", "&lt;").replace(">", "&gt;");
			if (formatting) {
				data = normalizeWhitespace(data);
			}
			appendTo(out, data);
		}
	}
	
	/**
	 * Serializes a CDATA-section node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlCDataSection node, Writer out) {
		appendTo(out, "<![CDATA[");
		if (node.getData() != null) {
			String data = node.getData().replace("]]>", "]]&gt;");
			if (formatting) {
				data = String.format(" %s ", normalizeWhitespace(data));
			}
			appendTo(out, data);
		}
		appendTo(out, "]]>");
	}
	
	/**
	 * Serializes a processing instruction node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlProcessingInstruction node, Writer out) {
		appendTo(out, "<?%s ", node.getTarget());
		if (node.getData() != null) {
			String data = node.getData().replace("?>", "?&gt;");
			if (formatting) {
				data = normalizeWhitespace(data);
			}
			appendTo(out, data);
		}
		appendTo(out, "?>");
	}
	
	/**
	 * Serializes an XML declaration node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlDeclaration node, Writer out) {
		appendTo(out, String.format("<?xml version=\"%s\"", node.getVersion()));
		if (node.getEncoding() != null) {
			appendTo(out, String.format(" encoding=\"%s\"", node.getEncoding()));
			if (node.getStandalone() != null) {
				appendTo(out, String.format(" standalone=\"%s\"", node.getStandalone()));
			}
		}
		appendTo(out, "?>");
	}
	
	/**
	 * Serializes a comment node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlComment node, Writer out) {
		appendTo(out, "<!--");
		if (node.getData() != null) {
			String data = node.getData().replace("-->", "--&gt;");
			if (formatting) {
				data = String.format(" %s ", normalizeWhitespace(data));
			}
			appendTo(out, data);
		}
		appendTo(out, "-->");
	}
	
	/**
	 * Serializes a document type node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlDocumentType node, Writer out) {
		appendTo(out, "<!DOCTYPE ", node.getName());
		if (node.getPublicId() != null) {
			appendTo(out, String.format(" PUBLIC \"%s\"", node.getPublicId()));
			if (node.getSystemId() != null) {
				appendTo(out, String.format(" \"%s\"", node.getSystemId()));
			}
		} else if (node.getSystemId() != null) {
			appendTo(out, String.format(" SYSTEM \"%s\"", node.getSystemId()));
		}
		if (node.getInternalSubset() != null) {
			appendTo(out, String.format(" [%s]", node.getInternalSubset()));
		}
		appendTo(out, ">");
	}
	
	/**
	 * Serializes a document node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlDocument node, Writer out) {
		if (node.hasChildNodes()) {
			for (XmlNode childNode : node.getChildNodes()) {
				if (!childNode.equals(node.getFirstChild())) {
					appendTo(out, String.format("%n"));
				}
				serialize(childNode, out);
			}
		}
	}
	
	/**
	 * Serializes an entity node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlEntity node, Writer out) {
		// TODO: Not implemented.
	}
	
	/**
	 * Serializes a notation node to the specified character stream.
	 * 
	 * @param node the node to serialize.
	 * @param out a {@link java.io.Writer Writer} object representing the character stream 
	 * 	that the serialized representation of the node is to be written to.
	 * @throws XmlException <em>FILE_IO_ERR</em>: if an I/O error occurs writing to the specified character stream.
	 */
	public void serialize(XmlNotation node, Writer out) {
		// TODO: Not implemented.
	}
	
	/**
	 * Appends a variable number of strings to the output, writing them to the specified character stream.
	 */
	private XmlSerializer appendTo(Writer out, String... args) {
		try {
			for (String arg : args) {
				out.write(arg);
			}
			return this;
		} catch (IOException ex) {
			throw new XmlException(ex.getMessage(), XmlException.FILE_IO_ERR);
		}
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
	private static String escapeAmpersands(String str) {
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
	
	/**
	 * Returns a string consisting of spaces to indent the output with.
	 */
	private static String getIndentation(int depth) {
		StringBuffer spaces = new StringBuffer();
		for (int i = 0; i < (depth * 4); i++) {
			spaces.append(' ');
		}
		return spaces.toString();
	}
	
	/**
	 * Determines whether the specified node contains child nodes other than text nodes.
	 */
	private static boolean containsNonTextNodes(XmlNode parent) {
		if (parent.hasChildNodes()) {
			for (XmlNode childNode : parent.getChildNodes()) {
				if (childNode.getNodeType() != XmlNode.TEXT_NODE) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns a normalized copy of the input string where all leading and trailing 
	 * XML whitespace characters have been removed and any consecutive XML whitespace 
	 * characters in the middle collapsed to a single space character.
	 */
	private static String normalizeWhitespace(String str) {
		str = str.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ');
		while (str.contains("  ")) {
			str = str.replace("  ", " ");
		}
		if (str.startsWith(" ")) { // Note, cannot use String.trim()
			str = str.substring(1);
		}
		if (str.endsWith(" ")) { // Note, cannot use String.trim()
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
}