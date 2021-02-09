/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents the entire XML document.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlDocument extends XmlParentNode {
	/** 
	 * Internal <code>XmlDocument</code> constructor.
	 * 
	 * @see proj78.xml.XmlDomImplementation#createDocument()
	 */
	/* internal */ XmlDocument() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return DOCUMENT_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return "#document";
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlParentNode#setOwnerDocument(proj78.xml.XmlDocument)
	 */
	/* internal */ void setOwnerDocument(XmlDocument ownerDoc) {
		throw new XmlException("Attempted to set an owner document on a node that does not allow for one.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setParentNode(proj78.xml.XmlParentNode)
	 */
	/* internal */ void setParentNode(XmlParentNode parentNode) {
		throw new XmlException("Attempted to set a parent on a node that does not allow for one.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/**
	 * Returns a reference to the DOM implementation object handling this 
	 * document.
	 * 
	 * @return a meta-object for this DOM.
	 */
	public XmlDomImplementation getDomImplementation() {
		return XmlDomImplementationRegistry.getDomImplementation("XML", "1.0");
	}
	
	/**
	 * Returns the XML declaration of this document.
	 * 
	 * @return the XML declaration of this document or <code>null</code>, if it 
	 * 	does not have one.
	 * @since DOM Level 3 (encapsulates the attributes <code>xmlVersion</code>, 
	 * 	<code>xmlEncoding</code> and <code>xmlStandalone</code>)
	 */
	public XmlDeclaration getXmlDeclaration() {
		if (hasChildNodes()) {
			for (XmlNode childNode : getChildNodes()) {
				if (childNode.getNodeType() == PROCESSING_INSTRUCTION_NODE 
						&& childNode.getNodeName().equals("xml")) {
					try {
						return (XmlDeclaration)childNode;
					} catch (ClassCastException ex) {
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the type of this document.
	 * 
	 * @return either the doctype or <code>null</code>, if this document has no 
	 * 	DTD associated with it.
	 */
	public XmlDocumentType getDoctype() {
		if (hasChildNodes()) {
			for (XmlNode childNode : getChildNodes()) {
				if (childNode.getNodeType() == DOCUMENT_TYPE_NODE) {
					return (XmlDocumentType)childNode;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the root element of this document.
	 * 
	 * @return the root element of this document or <code>null</code>, if this 
	 * 	document has no elements.
	 */
	public XmlElement getDocumentElement() {
		if (hasChildNodes()) {
			for (XmlNode childNode : getChildNodes()) {
				if (childNode.getNodeType() == ELEMENT_NODE) {
					return (XmlElement)childNode;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a node list containing all the elements with the specified tag 
	 * name as they appear in this document.
	 * 
	 * @param tagName the tag name to match. The special value "*" matches all tags.
	 * @return a possibly empty node list containing the elements that matched.
	 * @see proj78.xml.XmlElement#getElementsByTagName(java.lang.String)
	 */
	public XmlNodeList getElementsByTagName(String tagName) {
		XmlNodeList nodeList = new XmlNodeList();
		XmlElement root = getDocumentElement();
		if (root != null) {
			root.appendNamedElementsTo(nodeList, tagName);
		}
		return nodeList;
	}
	
	/**
	 * Returns the element that has an ID-attribute with the specified value.
	 * 
	 * <p>Note that a validating parser would be able to identify an ID-attribute
	 * based on the DTD. This implementation just checks for an attribute named 
	 * "id" or (the XML-standard) "xml:id".
	 * 
	 * @param id the value to match.
	 * @return the element with the specified ID-attribute or <code>null</code>, 
	 * 	if no matching element was found.
	 * @since DOM Level 2
	 * @see proj78.xml.XmlAttribute#isId()
	 */
	public XmlElement getElementById(String id) {
		for (XmlNode elem : getElementsByTagName("*")) {
			if (elem.hasAttributes()) {
				for (XmlNode attr : elem.getAttributes()) {
					if (((XmlAttribute)attr).isId() && attr.getNodeValue().equals(id)) {
						return (XmlElement)elem;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * (non-DOM)
	 * Returns a node list containing those elements located in this document 
	 * that have an attribute with the specified name and value.
	 * 
	 * @param name the attribute name to match.
	 * @param value the attribute value to match. The special value "*" matches 
	 * 	all values for that specific attribute.
	 * @return a possibly empty node list containing the elements that matched.
	 */
	public XmlNodeList getElementsByAttribute(String name, String value) {
		XmlNodeList nodeList = new XmlNodeList();
		for (XmlNode elem : getElementsByTagName("*")) {
			if (value.equals("*") ? ((XmlElement)elem).hasAttribute(name) 
					: ((XmlElement)elem).getAttribute(name).equals(value)) {
				nodeList.appendItem(elem);
			}
		}
		return nodeList;
	}
	
	/**
	 * (non-DOM)
	 * Removes all <em>element content whitespace</em> from the document tree.
	 * 
	 * @see proj78.xml.XmlElement#trimElementContentWhitespace()
	 * @see proj78.xml.XmlText#isElementContentWhitespace()
	 */
	public void trimElementContentWhitespace() {
		XmlElement root = getDocumentElement();
		if (root != null) {
			root.trimElementContentWhitespace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlParentNode#insertBefore(proj78.xml.XmlNode, proj78.xml.XmlNode)
	 */
	public XmlNode insertBefore(XmlNode newChild, XmlNode refChild) {
		if (newChild.getNodeType() == DOCUMENT_FRAGMENT_NODE) {
			return super.insertBefore(newChild, refChild);
		}
		if (newChild.getNodeType() == PROCESSING_INSTRUCTION_NODE 
				|| newChild.getNodeType() == DOCUMENT_TYPE_NODE 
				|| newChild.getNodeType() == ELEMENT_NODE 
				|| newChild.getNodeType() == COMMENT_NODE) {
			if (newChild.getNodeType() == DOCUMENT_TYPE_NODE && getDoctype() != null) {
				throw new XmlException("Attempted to insert a second DOCTYPE node into the document.", XmlException.HIERARCHY_REQUEST_ERR);
			} else if (newChild.getNodeType() == ELEMENT_NODE && getDocumentElement() != null) {
				throw new XmlException("Attempted to insert a second root element into the document.", XmlException.HIERARCHY_REQUEST_ERR);
			} else if (newChild.getNodeType() == PROCESSING_INSTRUCTION_NODE && newChild.getNodeName().equals("xml") && getXmlDeclaration() != null) {
				throw new XmlException("Attempted to insert a second XML declaration node into the document.", XmlException.HIERARCHY_REQUEST_ERR);
			}
			if (canInsertBefore(newChild, refChild)) {
				return super.insertBefore(newChild, refChild);
			}
			throw new XmlException("The specified node cannot be inserted at this location.", XmlException.HIERARCHY_REQUEST_ERR);
		}
		throw new XmlException("Attempted to insert an invalid child for this type of node.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/**
	 * Determines whether a (soon-to-be) child node can be inserted relative to 
	 * the position of the reference node into the list of children of this node.
	 * 
	 * @param newChild the node to be inserted.
	 * @param refChild the node that the new child is to be inserted before.
	 * @return <code>true</code> or <code>false</code> as the new child can either 
	 * 	be inserted at that location or not.
	 */
	private boolean canInsertBefore(XmlNode newChild, XmlNode refChild) {
		XmlNode prevSibling;
		if (refChild != null) {
			prevSibling = refChild.getPreviousSibling();
		} else {
			prevSibling = getLastChild();
		}
		while (prevSibling != null) {
			if (newChild.getNodeType() == PROCESSING_INSTRUCTION_NODE && newChild.getNodeName().equals("xml")) {
				if ((prevSibling.getNodeType() == PROCESSING_INSTRUCTION_NODE && !prevSibling.getNodeName().equals("xml"))
						|| prevSibling.getNodeType() == ELEMENT_NODE
						|| prevSibling.getNodeType() == DOCUMENT_TYPE_NODE
						|| prevSibling.getNodeType() == COMMENT_NODE) {
					return false; // An XML declaration node must be the first child.
				}
			} else if (newChild.getNodeType() == DOCUMENT_TYPE_NODE) {
				if (prevSibling.getNodeType() == ELEMENT_NODE) {
					return false; // The DOCTYPE must occur before the root element.
				}
			}
			prevSibling = prevSibling.getPreviousSibling();
		}
		XmlNode nextSibling = refChild;
		while (nextSibling != null) {
			if (newChild.getNodeType() == DOCUMENT_TYPE_NODE) {
				if (nextSibling.getNodeType() == PROCESSING_INSTRUCTION_NODE && nextSibling.getNodeName().equals("xml")) {
					return false; // The DOCTYPE cannot occur after the XML declaration. 
				} 
			} else if (newChild.getNodeType() == ELEMENT_NODE) {
				if ((nextSibling.getNodeType() == PROCESSING_INSTRUCTION_NODE && nextSibling.getNodeName().equals("xml"))
						|| nextSibling.getNodeType() == DOCUMENT_TYPE_NODE){
					return false; // The root element cannot occur before the XML declaration or the DOCTYPE.
				}
			} else if (newChild.getNodeType() == PROCESSING_INSTRUCTION_NODE || newChild.getNodeType() == COMMENT_NODE) {
				if (nextSibling.getNodeType() == PROCESSING_INSTRUCTION_NODE && nextSibling.getNodeName().equals("xml")) {
					return false; // Other nodes (PIs and comments) must occur after the XML declaration.
				}
			}
			nextSibling = nextSibling.getNextSibling();
		}
		return true;
	}
	
	/**
	 * Imports a node from another document into this one.
	 * 
	 * @param node the node being imported.
	 * @param deep indicates whether to recursively import the subtree underneath
	 * 	the specified node (i.e., perform a deep clone).
	 * @return the imported node.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the operation is not 
	 * 	supported for the type of node being imported.
	 * @since DOM Level 2
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode importNode(XmlNode node, boolean deep) {
		if (node.getNodeType() == DOCUMENT_NODE) {
			throw new XmlException("The specified node is of a type that cannot be imported.", XmlException.NOT_SUPPORTED_ERR);
		}
		if (node.getOwnerDocument().equals(this)) {
			return node;
		}
		XmlNode importedNode = node.cloneNode(deep);
		importedNode.setOwnerDocument(this);
		return importedNode;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		XmlDocument doc = getDomImplementation().createDocument();
		if (deep) {
			copyChildrenInto(doc);
		}
		return doc;
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlElement XmlElement} class.
	 * 
	 * @param tagName the tag name of the element.
	 * @return a new <code>XmlElement</code> instance with the specified tag name.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlElement createElement(String tagName) {
		XmlElement elem = new XmlElement(tagName);
		elem.setOwnerDocument(this);
		return elem;
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlDocumentFragment XmlDocumentFragment} class.
	 * 
	 * @return a new <code>XmlDocumentFragment</code> instance.
	 */
	public XmlDocumentFragment createDocumentFragment() {
		XmlDocumentFragment docFrag = new XmlDocumentFragment();
		docFrag.setOwnerDocument(this);
		return docFrag;
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlText XmlText} class.
	 * 
	 * @param data the text node's character content.
	 * @return a new <code>XmlText</code> instance with the specified content.
	 */
	public XmlText createTextNode(String data) {
		XmlText text = new XmlText(data);
		text.setOwnerDocument(this);
		return text;
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlComment XmlComment} class.
	 * 
	 * @param data the character content of the comment.
	 * @return a new <code>XmlComment</code> instance with the specified content.
	 */
	public XmlComment createComment(String data) {
		XmlComment comment = new XmlComment(data);
		comment.setOwnerDocument(this);
		return comment;
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlCDataSection XmlCDataSection} class.
	 * 
	 * @param data the CDATA-section's content.
	 * @return a new <code>XmlCDataSection</code> instance with the specified content.
	 */
	public XmlCDataSection createCDataSection(String data) {
		XmlCDataSection cdataSection = new XmlCDataSection(data);
		cdataSection.setOwnerDocument(this);
		return cdataSection;
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlProcessingInstruction XmlProcessingInstruction} class.
	 * 
	 * @param target the instruction target.
	 * @param data the instruction data.
	 * @return a new <code>XmlProcessingInstruction</code> instance with the specified target and data.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the instruction target is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlProcessingInstruction createProcessingInstruction(String target, String data) {
		XmlProcessingInstruction procInstr = new XmlProcessingInstruction(target, data);
		procInstr.setOwnerDocument(this);
		return procInstr;
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlAttribute XmlAttribute} class.
	 * 
	 * @param name the attribute name.
	 * @return a new <code>XmlAttribute</code> instance with the specified name.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlAttribute createAttribute(String name) {
		XmlAttribute attr = new XmlAttribute(name);
		attr.setOwnerDocument(this);
		return attr;
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlAttribute XmlAttribute} class.
	 * 
	 * @param name the attribute name.
	 * @param value an initial value for the attribute.
	 * @return a new <code>XmlAttribute</code> instance with the specified name and value.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlAttribute createAttribute(String name, String value) {
		XmlAttribute attr = createAttribute(name);
		attr.setValue(value);
		return attr;
	}
}