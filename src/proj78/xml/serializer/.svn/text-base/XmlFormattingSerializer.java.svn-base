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
import proj78.xml.XmlElement;
import proj78.xml.XmlNode;
import proj78.xml.XmlProcessingInstruction;
import proj78.xml.XmlText;

/**
 * Subclass of <code>XmlSerializer</code> that applies basic formatting to 
 * the XML that is generated.
 * 
 * @author K.A., copyright (c) 2008
 */
/* internal */ class XmlFormattingSerializer extends XmlSerializer {
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.serializer.XmlSerializer#serialize(proj78.xml.XmlAttribute)
	 */
	public String serialize(XmlAttribute node) {
		return normalizeWhitespace(super.serialize(node));
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.serializer.XmlSerializer#serialize(proj78.xml.XmlElement)
	 */
	public String serialize(XmlElement node) {
		return serialize(node, 0);
	}
	
	/**
	 * Helper method for <code>serialize(XmlElement)</code>.
	 */
	private String serialize(XmlElement node, int depth) {
		StringBuffer str = new StringBuffer();
		str.append('<').append(node.getTagName());
		if (node.hasAttributes()) {
			for (XmlNode attr : node.getAttributes()) {
				str.append(' ').append(serialize(attr));
			}
		}
		if (node.hasChildNodes()) {
			str.append('>');
			depth++;
			for (XmlNode childNode : node.getChildNodes()) {
				if (childNode.getNodeType() == XmlNode.TEXT_NODE){
					if (!((XmlText)childNode).isElementContentWhitespace()) {
						if (containsNonTextNodes(node)) {
							str.append(String.format("%n")).append(getIndentation(depth));
						}
						str.append(serialize(childNode));
					}
				} else {
					if (containsNonTextNodes(node)) {
						str.append(String.format("%n")).append(getIndentation(depth));
					}
					if (childNode.getNodeType() == XmlNode.ELEMENT_NODE) {
						str.append(serialize((XmlElement)childNode, depth));
					} else {
						str.append(serialize(childNode));
					}
				}
			}
			depth--;
			if (containsNonTextNodes(node)) {
				str.append(String.format("%n")).append(getIndentation(depth));
			}
			str.append("</").append(node.getTagName()).append('>');
		} else {
			str.append(" />");
		}
		return str.toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.serializer.XmlSerializer#serialize(proj78.xml.XmlText)
	 */
	public String serialize(XmlText node) {
		return normalizeWhitespace(super.serialize(node));
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.serializer.XmlSerializer#serialize(proj78.xml.XmlCDataSection)
	 */
	public String serialize(XmlCDataSection node) {
		return normalizeWhitespace(super.serialize(node));
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.serializer.XmlSerializer#serialize(proj78.xml.XmlProcessingInstruction)
	 */
	public String serialize(XmlProcessingInstruction node) {
		return normalizeWhitespace(super.serialize(node));
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.serializer.XmlSerializer#serialize(proj78.xml.XmlComment)
	 */
	public String serialize(XmlComment node) {
		return normalizeWhitespace(super.serialize(node));
	}
	
	/**
	 * Returns a series of space characters for formatting purposes given the 
	 * depth level.
	 */
	private static String getIndentation(int depth) {
		StringBuffer spaces = new StringBuffer();
		for (int i = 0; i < (depth * 4); i++) {
			spaces.append(' ');
		}
		return spaces.toString();
	}
	
	/**
	 * Determines whether the specified node contains child nodes other than
	 * text nodes.
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
	 * Returns a normalized copy of the input string where all leading and 
	 * and trailing XML whitespace characters have been removed and any 
	 * consecutive XML whitespace characters in the middle collapsed to a
	 * single space character.
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