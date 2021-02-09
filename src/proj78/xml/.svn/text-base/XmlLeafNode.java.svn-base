/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Serves as a base class for nodes without children. 
 * 
 * @author K.A., copyright (c) 2008
 */
/* internal */ abstract class XmlLeafNode extends XmlNode {
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#hasChildNodes()
	 */
	public final boolean hasChildNodes() {
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getChildNodes()
	 */
	public final XmlNodeList getChildNodes() {
		return new XmlNodeList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getFirstChild()
	 */
	public final XmlNode getFirstChild() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getLastChild()
	 */
	public final XmlNode getLastChild() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#insertBefore(proj78.xml.XmlNode, proj78.xml.XmlNode)
	 */
	public final XmlNode insertBefore(XmlNode newChild, XmlNode refChild) {
		throw new XmlException("Attempted to insert a child into a node that does not allow for children.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#appendChild(proj78.xml.XmlNode)
	 */
	public final XmlNode appendChild(XmlNode newChild) {
		return insertBefore(newChild, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#removeChild(proj78.xml.XmlNode)
	 */
	public final XmlNode removeChild(XmlNode oldChild) {
		throw new XmlException("The specified node must be a child of the node from which it is being removed.", XmlException.NOT_FOUND_ERR);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#replaceChild(proj78.xml.XmlNode, proj78.xml.XmlNode)
	 */
	public final XmlNode replaceChild(XmlNode newChild, XmlNode oldChild) {
		throw new XmlException("Attempted to replace a child in a node that does not allow for children.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#isAncestorOf(proj78.xml.XmlNode)
	 */
	/* internal */ final boolean isAncestorOf(XmlNode node) {
		return false;
	}
}