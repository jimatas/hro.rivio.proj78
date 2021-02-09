/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents a single element in the document.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlElement extends XmlParentNode {
	/**
	 * The tag name of this element.
	 */
	private String tagName;
	
	/**
	 * A node map containing the attributes of this element.
	 */
	private XmlNamedNodeMap attributes;
	
	/**
	 * Internal <code>XmlElement</code> constructor.
	 * 
	 * @param tagName the tag name of this element.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 * @see proj78.xml.XmlDocument#createElement(java.lang.String)
	 */
	/* internal */ XmlElement(String tagName) {
		if (!XmlUtility.isValidName(tagName)) {
			throw new XmlException("An invalid or illegal XML character was specified.", XmlException.INVALID_CHARACTER_ERR);
		}
		this.tagName = tagName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return ELEMENT_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return getTagName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeValue()
	 */
	public String getNodeValue() {
		return getTextContent();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setNodeValue(java.lang.String)
	 */
	public void setNodeValue(String nodeValue) {
		setTextContent(nodeValue);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setOwnerDocument(proj78.xml.XmlDocument)
	 */
	/* internal */ void setOwnerDocument(XmlDocument ownerDoc) {
		super.setOwnerDocument(ownerDoc);
		if (hasAttributes()) { // Iterate over attributes, updating the owner document if necessary.
			for (XmlNode attr : getAttributes()) {
				if (!attr.getOwnerDocument().equals(ownerDoc)) {
					attr.setOwnerDocument(ownerDoc);
				}
			}
		}
	}
	
	/**
	 * Returns the tag name of this element.
	 * 
	 * @return the tag name of this element.
	 * @see proj78.xml.XmlElement#getNodeName()
	 */
	public String getTagName() {
		return tagName;
	}
	
	/**
	 * Returns the text content of this element and all its descendant nodes.
	 * 
	 * @return the text content of this element subtree.
	 * @since DOM Level 3
	 * @see proj78.xml.XmlElement#getNodeValue()
	 */
	public String getTextContent() {
		if (hasChildNodes()) {
			StringBuffer textContent = new StringBuffer();
			for (XmlNode childNode : getChildNodes()) {
				if (childNode.getNodeType() == ELEMENT_NODE
						|| childNode.getNodeType() == TEXT_NODE
						|| childNode.getNodeType() == CDATA_SECTION_NODE) {
					textContent.append(childNode.getNodeValue());
				}
			}
			return textContent.toString();
		}
		return null;
	}
	
	/**
	 * Replaces the entire subtree underneath this element with a single text 
	 * node initialized to the specified value.
	 * 
	 * @param textContent the text to place in this element.
	 * @since DOM Level 3
	 * @see proj78.xml.XmlElement#setNodeValue(java.lang.String)
	 */
	public void setTextContent(String textContent) {
		while (hasChildNodes()) {
			removeChild(getFirstChild());
		}
		if (textContent != null) {
			appendChild(getOwnerDocument().createTextNode(textContent));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#hasAttributes()
	 */
	public boolean hasAttributes() {
		return attributes != null && attributes.length() != 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getAttributes()
	 */
	public XmlNamedNodeMap getAttributes() {
		if (attributes == null) {
			attributes = new XmlNamedNodeMap() {
				/*
				 * (non-Javadoc)
				 * @see proj78.xml.XmlNamedNodeMap#setNamedItem(proj78.xml.XmlNode)
				 */
				public XmlNode setNamedItem(XmlNode item) {
					XmlAttribute attr = (XmlAttribute)item; // throws ClassCastException
					if (attr.getOwnerElement() != null && !attr.getOwnerElement().equals(XmlElement.this)) {
						throw new XmlException("The specified attribute is already in use elsewhere.", XmlException.INUSE_ATTRIBUTE_ERR);
					} else if (!attr.getOwnerDocument().equals(getOwnerDocument())) {
						throw new XmlException("Attempted to insert a node that was created by another document than the node it is being inserted into.", XmlException.WRONG_DOCUMENT_ERR);
					}
					attr.setOwnerElement(XmlElement.this);
					return super.setNamedItem(attr);
				}
				
				/*
				 * (non-Javadoc)
				 * @see proj78.xml.XmlNamedNodeMap#removeNamedItem(java.lang.String)
				 */
				public XmlNode removeNamedItem(String name) {
					XmlAttribute attr = (XmlAttribute)super.removeNamedItem(name);
					if (attr != null) {
						attr.setOwnerElement(null);
					}
					return attr;
				}
			};
		}
		return attributes;
	}
	
	/**
	 * Determines whether this element has an attribute with the specified name.
	 * 
	 * @param name the name of the attribute to search for.
	 * @return <code>true</code> if this element has an attribute with the 
	 * 	specified name, <code>false</code> otherwise.
	 * @since DOM Level 2
	 */
	public boolean hasAttribute(String name) {
		return getAttributeNode(name) != null;
	}
	
	/**
	 * Retrieves an attribute value by name.
	 * 
	 * @param name the name of the attribute to retrieve the value of.
	 * @return the attribute value as a string or an empty string, if that 
	 * 	attribute could not be found or did not have a (default) value.
	 */
	public String getAttribute(String name) {
		XmlAttribute attr = getAttributeNode(name);
		if (attr != null) {
			return attr.getValue();
		}
		return "";
	}
	
	/**
	 * Adds or alters an attribute given its name and value.
	 * 
	 * @param name the name of the attribute to create or alter.
	 * @param value the value to set.
	 */
	public void setAttribute(String name, String value) {
		XmlAttribute attr = getAttributeNode(name);
		if (attr == null) {
			attr = getOwnerDocument().createAttribute(name);
			setAttributeNode(attr);
		}
		attr.setValue(value);
	}
	
	/**
	 * Removes an attribute by name.
	 * 
	 * @param name the name of the attribute to be removed.
	 */
	public void removeAttribute(String name) {
		XmlAttribute attr = getAttributeNode(name);
		if (attr != null) {
			removeAttributeNode(attr);
		}
	}
	
	/**
	 * Retrieves an attribute node by name.
	 * 
	 * @param name the name of the attribute to retrieve.
	 * @return the attribute with the specified name or <code>null</code>, if 
	 * 	it does not exist.
	 */
	public XmlAttribute getAttributeNode(String name) {
		if (hasAttributes()) {
			return (XmlAttribute)getAttributes().getNamedItem(name);
		}
		return null;
	}
	
	/**
	 * Adds a new attribute node.
	 * 
	 * @param attr the attribute to be added.
	 * @return if the attribute to add replaces an exising one, the replaced 
	 * 	attribute is returned, otherwise <code>null</code>.
	 * @throws XmlException <em>INUSE_ATTRIBUTE_ERR</em>: if the attribute being 
	 * 	added is already in use elsewhere.
	 */
	public XmlAttribute setAttributeNode(XmlAttribute attr) {
		return (XmlAttribute)getAttributes().setNamedItem(attr);
	}
	
	/**
	 * Removes an attribute node.
	 * 
	 * @param attr the attribute to be removed.
	 * @return the attribute removed or <code>null</code>, if that attribute 
	 * 	did not exist.
	 * @throws XmlException <em>NOT_FOUND_ERR</em>: if the attribute to be 
	 * 	removed is not an attribute of this element.
	 */
	public XmlAttribute removeAttributeNode(XmlAttribute attr) {
		if (hasAttribute(attr.getName())) {
			return (XmlAttribute)getAttributes().removeNamedItem(attr.getName());
		}
		throw new XmlException("The specified node must be an attribute of the element from which it is being removed.", XmlException.NOT_FOUND_ERR);
	}
	
	/**
	 * Returns a node list containing all the elements with the specified tag 
	 * name as they appear in the subtree underneath this element.
	 * 
	 * @param tagName the tag name to match. The special value "*" matches 
	 * 	all tags.
	 * @return a possibly empty node list containing the elements that matched. 
	 */
	public XmlNodeList getElementsByTagName(String tagName) {
		XmlNodeList nodeList = new XmlNodeList();
		if (hasChildNodes()) {
			for (XmlNode childNode : getChildNodes()) {
				if (childNode.getNodeType() == ELEMENT_NODE) {
					((XmlElement)childNode).appendNamedElementsTo(nodeList, tagName);
				}
			}
		}
		return nodeList;
	}
	
	/**
	 * Internal helper method for <code>getElementsByTagName()</code>. Adds 
	 * this element node to the end of the supplied node list if its name 
	 * matches the specified tag name; recurses over the children.
	 */
	/* internal */ void appendNamedElementsTo(XmlNodeList nodeList, String tagName) {
		if (tagName.equals("*") || tagName.equals(getTagName())) {
			nodeList.appendItem(this);
		}
		if (hasChildNodes()) {
			for (XmlNode childNode : getChildNodes()) {
				if (childNode.getNodeType() == ELEMENT_NODE) {
					((XmlElement)childNode).appendNamedElementsTo(nodeList, tagName);
				}
			}
		}
	}
	
	/**
	 * Puts all the text nodes in the subtree underneath this element into a 
	 * normal form so that there are no more adjacent text nodes left.
	 * 
	 * @since DOM Level 1; moved to node in DOM Level 2.
	 */
	public void normalize() {
		if (hasChildNodes()) {
			for (int i = 0; i < getChildNodes().length() - 1; i++) {
				XmlNode currentChild = getChildNodes().item(i);
				if (currentChild.getNodeType() == TEXT_NODE) {
					XmlNode nextChild = getChildNodes().item(i + 1);
					if (nextChild.getNodeType() == TEXT_NODE) {
						((XmlText)currentChild).appendData(nextChild.getNodeValue());
						removeChild(nextChild);
						i--;
					}
				} else if (currentChild.getNodeType() == ELEMENT_NODE) {
					((XmlElement)currentChild).normalize();
				}
			}
		}
	}
	
	/**
	 * (non-DOM)
	 * Removes all <em>element content whitespace</em> from the subtree underneath 
	 * this element.
	 * 
	 * @see proj78.xml.XmlText#isElementContentWhitespace()
	 */
	public void trimElementContentWhitespace() {
		if (hasChildNodes()) {
			for (int i = 0; i < getChildNodes().length(); i++) {
				XmlNode childNode = getChildNodes().item(i);
				if (childNode.getNodeType() == TEXT_NODE 
						&& ((XmlText)childNode).isElementContentWhitespace()) {
					removeChild(childNode);
					i--;
				} else if (childNode.getNodeType() == ELEMENT_NODE) {
					((XmlElement)childNode).trimElementContentWhitespace();
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlParentNode#insertBefore(proj78.xml.XmlNode, proj78.xml.XmlNode)
	 */
	public XmlNode insertBefore(XmlNode newChild, XmlNode refChild) {
		if ((newChild.getNodeType() == PROCESSING_INSTRUCTION_NODE && newChild.getNodeName().equals("xml"))
				|| newChild.getNodeType() == DOCUMENT_TYPE_NODE) {
			throw new XmlException("Attempted to insert an invalid child for this type of node.", XmlException.HIERARCHY_REQUEST_ERR);
		}
		return super.insertBefore(newChild, refChild);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		XmlElement elem = getOwnerDocument().createElement(getTagName());
		if (hasAttributes()) {
			for (XmlNode attr : getAttributes()) {
				elem.setAttributeNode((XmlAttribute)attr.cloneNode(deep));
			}
		}
		if (deep) {
			copyChildrenInto(elem);
		}
		return elem;
	}
}