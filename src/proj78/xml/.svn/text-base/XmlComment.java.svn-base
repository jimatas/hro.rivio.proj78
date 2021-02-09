/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents the character content of an XML comment.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlComment extends XmlCharacterData {
	/**
	 * Internal <code>XmlComment</code> constructor.
	 * 
	 * @param data the character content of this comment.
	 * @see proj78.xml.XmlDocument#createComment(java.lang.String)
	 */
	/* internal */ XmlComment(String data) {
		super(data);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return COMMENT_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return "#comment";
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		return getOwnerDocument().createComment(getData());
	}
}