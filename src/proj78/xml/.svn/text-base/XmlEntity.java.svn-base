/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents either a parsed or unparsed entity in the document. 
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlEntity extends XmlParentNode {
	/**
	 * The name of this entity.
	 */
	private String name;
	
	/**
	 * The public identifier associated with this entity.
	 */
	private String publicId;

	/**
	 * The system identifier associated with this entity.
	 */
	private String systemId;
	
	/**
	 * The name of the notation for this unparsed entity.
	 */
	private String notationName;
	
	/**
	 * Internal <code>XmlEntity</code> constructor.
	 * 
	 * @param name the name of this entity.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 * @see proj78.xml.XmlDocumentType#createEntity(java.lang.String)
	 */
	/* internal */ XmlEntity(String name) {
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
		return ENTITY_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setParentNode(proj78.xml.XmlParentNode)
	 */
	/* internal */ void setParentNode(XmlParentNode parentNode) {
		throw new XmlException("Attempted to set a parent on a node that does not allow for one.", XmlException.HIERARCHY_REQUEST_ERR);
	}
	
	/**
	 * Returns the public identifier associated with this entity.
	 * 
	 * @return the public identifier associated with this entity or 
	 * 	<code>null</code>, if one was not specified.
	 */
	public String getPublicId() {
		return publicId;
	}
	
	/* internal */ void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	
	/**
	 * Returns the system identifier associated with this entity.
	 * 
	 * @return the system identifier associated with this entity or 
	 * 	<code>null</code>, if one was not specified.
	 */
	public String getSystemId() {
		return systemId;
	}
	
	/* internal */ void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	/**
	 * Returns the name of the notation for this entity.
	 * 
	 * @return for unparsed entities, the name of the notation; <code>null</code> 
	 * 	for parsed entities. 
	 */
	public String getNotationName() {
		return notationName;
	}
	
	/* internal */ void setNotationName(String notationName) {
		this.notationName = notationName;
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
		throw new XmlException("The clone operation is not supported for this type of node.", XmlException.NOT_SUPPORTED_ERR);
	}
}