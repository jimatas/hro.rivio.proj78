/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Serves as a base class for nodes with children. 
 * 
 * @author K.A., copyright (c) 2008
 */
/* internal */ abstract class XmlParentNode extends XmlNode {
	/**
	 * A node list containing the children of this node.
	 */
	private XmlNodeList childNodes;
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setOwnerDocument(proj78.xml.XmlDocument)
	 */
	/* internal */ void setOwnerDocument(XmlDocument ownerDoc) {
		super.setOwnerDocument(ownerDoc);
		if (hasChildNodes()) { // Recurse over children, updating the owner document if necessary.
			for (XmlNode childNode : getChildNodes()) {
				if (!childNode.getOwnerDocument().equals(ownerDoc)) {
					childNode.setOwnerDocument(ownerDoc);
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#hasChildNodes()
	 */
	public final boolean hasChildNodes() {
		return childNodes != null && childNodes.length() != 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getChildNodes()
	 */
	public final XmlNodeList getChildNodes() {
		if (childNodes == null) {
			childNodes = new XmlNodeList();
		}
		return childNodes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getFirstChild()
	 */
	public final XmlNode getFirstChild() {
		if (hasChildNodes()) {
			return getChildNodes().item(0);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getLastChild()
	 */
	public final XmlNode getLastChild() {
		if (hasChildNodes()) {
			return getChildNodes().item(getChildNodes().length() - 1);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#insertBefore(proj78.xml.XmlNode, proj78.xml.XmlNode)
	 */
	public XmlNode insertBefore(XmlNode newChild, XmlNode refChild) {
		if (refChild != null && (refChild.getParentNode() == null || !refChild.getParentNode().equals(this))) {
			throw new XmlException("The reference child must be an existing child of the node into which the new child is being inserted.", XmlException.NOT_FOUND_ERR);
		}
		if (newChild.getNodeType() == DOCUMENT_NODE 
				|| newChild.getNodeType() == ATTRIBUTE_NODE 
				|| newChild.getNodeType() == NOTATION_NODE
				|| newChild.getNodeType() == ENTITY_NODE) {
			throw new XmlException("Attempted to insert an invalid child for this type of node.", XmlException.HIERARCHY_REQUEST_ERR);
		}
		if (newChild.getNodeType() == DOCUMENT_FRAGMENT_NODE) {
			while (newChild.hasChildNodes()) {
				insertBefore(newChild.getFirstChild(), refChild);
			}
			return newChild;
		}
		if (newChild.getOwnerDocument() != null && !(newChild.getOwnerDocument().equals(this) 
				|| newChild.getOwnerDocument().equals(getOwnerDocument()))) {
			throw new XmlException("Attempted to insert a node that was created by another document than the node it is being inserted into.", XmlException.WRONG_DOCUMENT_ERR);
		}
		if (newChild.equals(this) || newChild.isAncestorOf(this)) {
			throw new XmlException("The specified node cannot be inserted as it would cause cycles in the tree.", XmlException.HIERARCHY_REQUEST_ERR);
		}
		if (!newChild.equals(refChild)) {	
			if (newChild.getParentNode() != null) {
				newChild.getParentNode().removeChild(newChild);
			}
			if (refChild == null) { // appendChild()
				getChildNodes().appendItem(newChild);
			} else {
				getChildNodes().insertItem(getChildNodes().indexOf(refChild), newChild);
			}
			newChild.setParentNode(this);
		}
		return newChild;
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
	public XmlNode removeChild(XmlNode oldChild) {
		if (oldChild.getParentNode() == null || !oldChild.getParentNode().equals(this)) {
			throw new XmlException("The specified node must be a child of the node from which it is being removed.", XmlException.NOT_FOUND_ERR);
		}
		getChildNodes().removeItem(oldChild);
		oldChild.setParentNode(null);
		return oldChild;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#replaceChild(proj78.xml.XmlNode, proj78.xml.XmlNode)
	 */
	public final XmlNode replaceChild(XmlNode newChild, XmlNode oldChild) {
		XmlNode refChild = oldChild.getNextSibling();
		removeChild(oldChild);
		insertBefore(newChild, refChild);
		return oldChild;
	}
	
	/**
	 * Copies the children of this node into another node.
	 * 
	 * @param parentNode the node into which the child nodes of this one are 
	 * 	to be copied.
	 */
	protected final void copyChildrenInto(XmlParentNode parentNode) {
		if (hasChildNodes()) {
			for (XmlNode childNode : getChildNodes()) {
				parentNode.appendChild(childNode.cloneNode(true));
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#isAncestorOf(proj78.xml.XmlNode)
	 */ 
	/* internal */ final boolean isAncestorOf(XmlNode node) {
		while ((node = node.getParentNode()) != null) {
			if (node.equals(this)) {
				return true;
			}
		}
		return false;
	}
}