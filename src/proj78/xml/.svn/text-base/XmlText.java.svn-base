/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents the text content of an element or attribute.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlText extends XmlCharacterData {
	/**
	 * Internal <code>XmlText</code> constructor.
	 * 
	 * @param data the character data of this node.
	 * @see proj78.xml.XmlDocument#createTextNode(java.lang.String)
	 */
	/* internal */ XmlText(String data) {
		super(data);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return TEXT_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return "#text"; 
	}
	
	/**
	 * Breaks this text node into two distinct objects at the specified offset 
	 * keeping both in the tree as siblings.
	 * 
	 * @param offset the position at which to split.
	 * @return the resulting text node.
	 * @throws XmlException <em>INDEX_SIZE_ERR</em>: if the character offset was
	 *	out of bounds.
	 */
	public XmlText splitText(int offset) {
		if (offset < 0 || offset > length()) {
			throw new XmlException("An out of bounds character offset was specified.", XmlException.INDEX_SIZE_ERR);
		}
		XmlText newText = (XmlText)cloneNode(false);
		newText.setData(substringData(offset, length() - offset));
		setData(substringData(0, offset));
		if (getParentNode() != null) {
			getParentNode().insertBefore(newText, getNextSibling());
		}
		return newText;
	}
	
	/**
	 * Determines whether this text node consists entirely of whitespace characters 
	 * and occurs as part of an element's content.
	 * 
	 * <p>Note that a validating parser would be able to recognize <em>element 
	 * content whitespace</em> beforehand (i.e., during the parsing process) 
	 * based on the DTD.
	 * 
	 * @return <code>true</code> if this text node respresents <em>element content 
	 * 	whitespace</em>, <code>false</code> otherwise.
	 * @since DOM Level 3
	 */
	public boolean isElementContentWhitespace() {
		return getParentNode() != null && getParentNode().getNodeType() == ELEMENT_NODE
				&& XmlUtility.isWhitespace(getData());
	}
	
	/*
	 * (non-Javadoc)
	 * @see XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		return getOwnerDocument().createTextNode(getData());
	}
}