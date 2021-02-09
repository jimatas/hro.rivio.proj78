/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents the primary data type for the entire DOM.
 * 
 * @author K.A., copyright (c) 2008
 */	
public abstract class XmlNode {
	/**
	 * The node is an {@link proj78.xml.XmlElement XmlElement}.
	 */
	public static final int ELEMENT_NODE = 1;
	
	/**
	 * The node is an {@link proj78.xml.XmlAttribute XmlAttribute}.
	 */
	public static final int ATTRIBUTE_NODE = 2;
	
	/**
	 * The node is an {@link proj78.xml.XmlText XmlText}.
	 */
	public static final int TEXT_NODE = 3;
	
	/**
	 * The node is an {@link proj78.xml.XmlCDataSection XmlCDataSection}.
	 */
	public static final int CDATA_SECTION_NODE = 4;
	
	/**
	 * The node is an <code>XmlEntityReference</code>.
	 * 
	 * <p>Note: Not implemented.
	 */
	public static final int ENTITY_REFERENCE_NODE = 5;
	
	/**
	 * The node is an {@link proj78.xml.XmlEntity XmlEntity}.
	 */
	public static final int ENTITY_NODE = 6;
	
	/**
	 * The node is an {@link proj78.xml.XmlProcessingInstruction XmlProcessingInstruction}.
	 */
	public static final int PROCESSING_INSTRUCTION_NODE = 7;
	
	/**
	 * The node is an {@link proj78.xml.XmlComment XmlComment}.
	 */
	public static final int COMMENT_NODE = 8;
	
	/**
	 * The node is an {@link proj78.xml.XmlDocument XmlDocument}.
	 */
	public static final int DOCUMENT_NODE = 9;
	
	/**
	 * The node is an {@link proj78.xml.XmlDocumentType XmlDocumentType}.
	 */
	public static final int DOCUMENT_TYPE_NODE = 10;
	
	/**
	 * The node is an {@link proj78.xml.XmlDocumentFragment XmlDocumentFragment}.
	 */
	public static final int DOCUMENT_FRAGMENT_NODE = 11;
	
	/**
	 * The node is an {@link proj78.xml.XmlNotation XmlNotation}.
	 */
	public static final int NOTATION_NODE = 12;
	
	/**
	 * The owner document of this node; <code>null</code> if this node is a 
	 * document node.
	 */
	private XmlDocument ownerDoc;
	
	/**
	 * The parent of this node; <code>null</code> if this node has not yet been 
	 * added to the tree.
	 */
	private XmlNode parentNode;
	
	/**
	 * Internal <code>XmlNode</code> constructor.
	 */
	/* internal */ XmlNode() {
	}
	
	/**
	 * Returns the type of this node.
	 * 
	 * @return an integer type code identifying the underlying object.
	 */
	public abstract int getNodeType();
	
	/**
	 * Returns the name of this node.
	 * 
	 * @return the node type-dependent name of this node.
	 */
	public abstract String getNodeName();
	
	/**
	 * Returns the value of this node.
	 * 
	 * @return the value of this node; <code>null</code> by default.
	 */
	public String getNodeValue() {
		return null;
	}
	
	/**
	 * Assigns a new value to this node, if supported (i.e., the node is not 
	 * read-only).
	 * 
	 * @param nodeValue the value to assign to this node.
	 * @throws XmlException <em>NO_DATA_ALLOWED_ERR</em>: if this node does not 
	 * 	allow for a value to be set.
	 */
	public void setNodeValue(String nodeValue) {
		throw new XmlException("Attempted to assign a value to a node that does not support data.", XmlException.NO_DATA_ALLOWED_ERR);
	}
	
	/**
	 * Returns the document that this node is associated with.
	 * 
	 * @return the owner document of this node or <code>null</code>, if this 
	 * 	node is itself a document node.
	 */
	public XmlDocument getOwnerDocument() {
		return ownerDoc;
	}
	
	/**
	 * Internal method to set the owner document of this node.
	 * 
	 * @param ownerDoc the owner document of this node.
	 * @throws XmlException <em>HIERARCHY_REQUEST_ERR</em>: if this node is 
	 * 	itself a document node.
	 */
	/* internal */ void setOwnerDocument(XmlDocument ownerDoc) {
		this.ownerDoc = ownerDoc;
	}
	
	/**
	 * Returns the parent of this node.
	 * 
	 * @return the parent of this node or <code>null</code>, if this node has 
	 * 	not yet been added to the tree.
	 */
	public XmlNode getParentNode() {
		return parentNode;
	}
	
	/**
	 * Internal method to set the parent of this node.
	 * 
	 * @param parentNode the new parent of this node; <code>null</code> to 
	 * 	detach this node from its parent.
	 * @throws XmlException <em>HIERARCHY_REQUEST_ERR</em>: if this node cannot 
	 * 	have a parent (i.e., is a document, document fragment or attribute node).
	 */
	/* internal */ void setParentNode(XmlParentNode parentNode) {
		this.parentNode = parentNode;
	}
	
	/**
	 * Determines whether this node has any child nodes.
	 * 
	 * @return <code>true</code> if this node has child nodes, <code>false</code>
	 * 	otherwise.
	 */
	public abstract boolean hasChildNodes();
	
	/**
	 * Returns a node list containing all the child nodes of this node.
	 * 
	 * @return a possibly empty node list containing the children of this node. 
	 */
	public abstract XmlNodeList getChildNodes();
	
	/**
	 * Returns the first child of this node.
	 * 
	 * @return the first child of this node or <code>null</code>, if this node 
	 * 	has no children.
	 */
	public abstract XmlNode getFirstChild();
	
	/**
	 * Returns the last child of this node.
	 * 
	 * @return the last child of this node or <code>null</code>, if this node 
	 * 	has no children.
	 */
	public abstract XmlNode getLastChild();
	
	/**
	 * Returns the node immediately preceding this node.
	 * 
	 * @return the node immediately preceding this one or <code>null</code>, if 
	 * 	there is no such node.
	 */
	public final XmlNode getPreviousSibling() {
		if (getParentNode() != null) {
			int i = getParentNode().getChildNodes().indexOf(this);
			if (i >= 1) {
				return getParentNode().getChildNodes().item(i - 1);
			}
		}
		return null;
	}
	
	/**
	 * Returns the node immediately following this node.
	 * 
	 * @return the node immediately following this one or <code>null</code>, if 
	 * 	there is no such node.
	 */
	public final XmlNode getNextSibling() {
		if (getParentNode() != null) {
			int i = getParentNode().getChildNodes().indexOf(this);
			if (i >= 0 && i < getParentNode().getChildNodes().length() - 1) {
				return getParentNode().getChildNodes().item(i + 1);
			}
		}
		return null;
	}
	
	/**
	 * Inserts a new node immediately before the reference node into the list
	 * of children of this node. If the reference node is <code>null</code>, 
	 * the new node will be appended to the end of the list.
	 * 
	 * @param newChild the node to be inserted.
	 * @param refChild the node to insert before, if non-null; otherwise the new 
	 * 	child will be appended to the end of the list of children of this node.
	 * @return the node inserted.
	 * @throws XmlException <em>HIERARCHY_REQUEST_ERR</em>: if the node to be 
	 * 	inserted is not a valid child for this type of node.<br />
	 *	<em>WRONG_DOCUMENT_ERR</em>: the node to be inserted was created by a 
	 *	document other than the one that created this node.<br />
	 *	<em>NOT_FOUND_ERR</em>: the reference node, if non-null, is not an 
	 *	existing child of this node.
	 * @see proj78.xml.XmlNode#appendChild(proj78.xml.XmlNode)
	 */
	public abstract XmlNode insertBefore(XmlNode newChild, XmlNode refChild);
	
	/**
	 * Adds a new node to the end of the list of children of this node.
	 * 
	 * @param newChild the node to be added.
	 * @return the node added.
	 * @throws XmlException <em>HIERARCHY_REQUEST_ERR</em>: if the node to be 
	 * 	added is not a valid child for this type of node.<br />
	 *	<em>WRONG_DOCUMENT_ERR</em>: it was created by a document other than the 
	 *	one that created this node.
	 */
	public abstract XmlNode appendChild(XmlNode newChild);
	
	/**
	 * Removes the specified node from the list of children of this node.
	 * 
	 * @param oldChild the node to be removed.
	 * @return the node removed.
	 * @throws XmlException <em>NOT_FOUND_ERR</em>: if the node to be removed 
	 * 	is not a child of this node.
	 */
	public abstract XmlNode removeChild(XmlNode oldChild);
	
	/**
	 * Replaces a node with another one in the list of children of this node.
	 * 
	 * @param newChild the node to place in the child list.
	 * @param oldChild the node being replaced.
	 * @return the node replaced.
	 * @throws XmlException <em>HIERARCHY_REQUEST_ERR</em>: if the replacement 
	 * 	node is not a valid child for this type of node.<br />
	 * 	<em>WRONG_DOCUMENT_ERR</em>: it was created by a document other than the one
	 * 	that created this node.<br />
	 * 	<em>NOT_FOUND_ERR</em>: if the node to be replaced is not a child of this node.
	 */
	public abstract XmlNode replaceChild(XmlNode newChild, XmlNode oldChild);
	
	/**
	 * Determines whether this node, if it is an element node, has any attributes.
	 * 
	 * @return <code>true</code> if this node is an element node and has attributes, 
	 * 	<code>false</code> otherwise.
	 * @since DOM Level 2
	 */
	public boolean hasAttributes() {
		return false;
	}
	
	/**
	 * Returns a node map containing the attributes of this node, if it is an 
	 * element node.
	 * 
	 * @return a node map containing the attributes of this element node or
	 * 	<code>null</code>, if this node is not an element.
	 */
	public XmlNamedNodeMap getAttributes() {
		return null;
	}
	
	/**
	 * Returns either a shallow or a deep copy of this node.
	 * 
	 * @param deep indicates whether the child nodes are also to be duplicated.
	 * @return either a shallow (i.e., childless) or deep copy of this node.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the operation is not 
	 * 	supported by this node.
	 */
	public abstract XmlNode cloneNode(boolean deep);
	
	/**
	 * Determines whether this node is an ancestor (e.g., parent, grand parent)
	 * of the specified node.
	 * 
	 * @param node a possible descendant of this node. 
	 * @return <code>true</code> if this node is an ancestor of the one specified, 
	 * 	<code>false</code> otherwise.
	 */
	/* internal */ abstract boolean isAncestorOf(XmlNode node);
}