/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents an attribute (name/value pair) in the starting tag of an element.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlAttribute extends XmlParentNode {
	/**
	 * The owner element of this attribute.
	 */
	private XmlElement ownerElement;
	
	/**
	 * The name of this attribute.
	 */
	private String name;
	
	/**
	 * Internal <code>XmlAttribute</code> constructor.
	 * 
	 * @param name the name of this attribute.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 * @see proj78.xml.XmlDocument#createAttribute(java.lang.String)
	 */
	/* internal */ XmlAttribute(String name) {
		if (!XmlUtility.isValidName(name)) {
			throw new XmlException("An invalid or illegal XML character was specified.", XmlException.INVALID_CHARACTER_ERR);
		}
		this.name = name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return ATTRIBUTE_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return getName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeValue()
	 */
	public String getNodeValue() {
		return getValue();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setNodeValue(java.lang.String)
	 */
	public void setNodeValue(String nodeValue) {
		setValue(nodeValue);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setParentNode(proj78.xml.XmlParentNode)
	 */
	/* internal */ void setParentNode(XmlParentNode parentNode) {
		throw new XmlException("Attempted to set a parent on a node that does not allow for one.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/**
	 * Returns the name of this attribute.
	 * 
	 * @return the name of this attribute.
	 * @see proj78.xml.XmlAttribute#getNodeName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the value of this attribute.
	 * 
	 * @return the value of this attribute.
	 * @see proj78.xml.XmlAttribute#getNodeValue()
	 */
	public String getValue() {
		if (hasChildNodes()) {
			StringBuffer value = new StringBuffer();
			for (XmlNode childNode : getChildNodes()) {
				if (childNode.getNodeType() == TEXT_NODE) {
					value.append(childNode.getNodeValue());
				}
			}
			return value.toString();
		}
		return null;
	}
	
	/**
	 * Assigns a new value to this attribute.
	 * 
	 * @param value the new value of this attribute.
	 * @see proj78.xml.XmlAttribute#setNodeValue(java.lang.String)
	 */
	public void setValue(String value) {
		while (hasChildNodes()) {
			removeChild(getFirstChild());
		}
		if (value != null) {
			appendChild(getOwnerDocument().createTextNode(value));
		}
	}
	
	/**
	 * Determines whether this is an ID-attribute.
	 * 
	 * <p>Note that a validating parser would be able to identify an ID-attribute
	 * based on the DTD. This implementation just checks for an attribute named 
	 * "id" or (the XML-standard) "xml:id".
	 * 
	 * @return <code>true</code> if this is an ID-attribute, <code>false</code>
	 *	otherwise.
	 * @since DOM Level 3
	 */
	public boolean isId() {
		return getName().equals("xml:id") || getName().equals("id");
	}
	
	/**
	 * Returns the element that this attribute is attached to.
	 * 
	 * @return the owner element of this attribute or <code>null</code>, if 
	 * 	this attribute is not in use.
	 * @since DOM Level 2
	 */
	public XmlElement getOwnerElement() {
		return ownerElement;
	}
	
	/**
	 * Internal method to set the element that this attribute is to be attached to.
	 * 
	 * @param ownerElement the new owner element of this attribute; or <code>null</code> 
	 * 	to break the association with the current owner element.
	 * @since DOM Level 2
	 */
	/* internal */ void setOwnerElement(XmlElement ownerElement) {
		this.ownerElement = ownerElement;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlParentNode#insertBefore(proj78.xml.XmlNode, proj78.xml.XmlNode)
	 */
	public XmlNode insertBefore(XmlNode newChild, XmlNode refChild) {
		if (newChild.getNodeType() == TEXT_NODE 
				|| newChild.getNodeType() == ENTITY_REFERENCE_NODE) {
			return super.insertBefore(newChild, refChild);
		}
		throw new XmlException("Attempted to insert an invalid child for this type of node.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		XmlAttribute attr = getOwnerDocument().createAttribute(getName());
		copyChildrenInto(attr); // Recurse over children irrespective of "deep" parameter.
		return attr;
	}
}