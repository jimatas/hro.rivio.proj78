/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents the DOCTYPE declaration of the document.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlDocumentType extends XmlLeafNode {
	/**
	 * The name immediately following the DOCTYPE keyword; i.e., the tag name 
	 * of the document's root element.
	 */
	private String dtdName;
	
	/**
	 * The public identifier of the external subset.
	 */
	private String publicId;
	
	/**
	 * The system identifier of the external subset.
	 */
	private String systemId;
	
	/**
	 * A string containing the internal subset.
	 */
	private String internalSubset;
	
	/**
	 * Internal <code>XmlDocumentType</code> constructor.
	 * 
	 * @param dtdName the name immediately following the DOCTYPE keyword.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 * @see proj78.xml.XmlDomImplementation#createDocumentType(java.lang.String)
	 */
	/* internal */ XmlDocumentType(String dtdName) {
		if (!XmlUtility.isValidName(dtdName)) {
			throw new XmlException("An invalid or illegal XML character was specified.", XmlException.INVALID_CHARACTER_ERR);
		}
		this.dtdName = dtdName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return DOCUMENT_TYPE_NODE;
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
	 * @see proj78.xml.XmlNode#setParentNode(proj78.xml.XmlParentNode)
	 */
	/* internal */ void setParentNode(XmlParentNode parentNode) {
		if (parentNode != null && parentNode.getNodeType() == DOCUMENT_NODE) {
			setOwnerDocument((XmlDocument)parentNode);
		}
		super.setParentNode(parentNode);
	}
	
	/**
	 * Returns the name immediately following the DOCTYPE keyword.
	 * 
	 * @return the name immediately following the DOCTYPE keyword.
	 * @see proj78.xml.XmlDocumentType#getNodeName()
	 */
	public String getName() {
		return dtdName;
	}
	
	/**
	 * Returns the public identifier of the external subset.
	 * 
	 * @return the public identifier of the external subset or <code>null</code>, 
	 * 	if it is not present.
	 * @since DOM Level 2
	 */
	public String getPublicId() {
		return publicId;
	}
	
	/**
	 * Internal method to set the public identifier of the external subset.
	 * 
	 * @param publicId the public identifier of the external subset.
	 * @since DOM Level 2
	 */
	/* internal */ void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	
	/**
	 * Returns the system identifier of the external subset.
	 * 
	 * @return the system identifier of the external subset or <code>null</code>, 
	 * 	if it is not present.
	 * @since DOM Level 2
	 */
	public String getSystemId() {
		return systemId;
	}
	
	/**
	 * Internal method to set the system identifier of the external subset.
	 * 
	 * @param systemId the system identifier of the external subset.
	 * @since DOM Level 2
	 */
	/* internal */ void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	/**
	 * Returns a string containing the internal subset.
	 * 
	 * @return a string containing the internal subset or <code>null</code>, if 
	 * 	there is none.
	 * @since DOM Level 2
	 */
	public String getInternalSubset() {
		return internalSubset;
	}
	
	/**
	 * Internal method to assign a string containing the internal subset.
	 * 
	 * @param internalSubset a string containing the internal subset.
	 * @since DOM Level 2
	 */
	/* internal */ void setInternalSubset(String internalSubset) {
		this.internalSubset = internalSubset;
	}
	
	/**
	 * Returns a node map containing the entities declared in the DTD.
	 * 
	 * @return not implemented, always returns <code>null</code>.
	 */
	public XmlNamedNodeMap getEntities() {
		return null;
	}
	
	/**
	 * Returns a node map containing the notations declared in the DTD.
	 * 
	 * @return not implemented, always returns <code>null</code>.
	 */
	public XmlNamedNodeMap getNotations() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		XmlDomImplementation domImpl = XmlDomImplementationRegistry.getDomImplementation("XML", "1.0");
		XmlDocumentType doctype = domImpl.createDocumentType(getName());
		doctype.setPublicId(getPublicId());
		doctype.setSystemId(getSystemId());
		doctype.setInternalSubset(getInternalSubset());
		return doctype;
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlNotation XmlNotation} class.
	 * 
	 * @param name the name of the notation.
	 * @return a new <code>XmlNotation</code> instance with the specified name.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlNotation createNotation(String name) {
		return new XmlNotation(name);
	}
	
	/**
	 * (non-DOM)
	 * Creates a new {@link proj78.xml.XmlNotation XmlNotation} class.
	 * 
	 * @param name the name of the notation.
	 * @param publicId the public identifier of the notation; optional and may be <code>null</code>.
	 * @param systemId the system identifier of the notation; optional and may be <code>null</code>.
	 * @return a new <code>XmlNotation</code> instance with the specified name, and public and system identifiers.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlNotation createNotation(String name, String publicId, String systemId) {
		XmlNotation notation = createNotation(name);
		notation.setPublicId(publicId);
		notation.setSystemId(systemId);
		return notation;
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlEntity XmlEntity} class.
	 * 
	 * @param name the name of the entity.
	 * @return a new <code>XmlEntity</code> instance with the specified name.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlEntity createEntity(String name) {
		return new XmlEntity(name);
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlEntity XmlEntity} class.
	 * 
	 * @param name the name of the entity.
	 * @param publicId the public identifier associated with the entity; optional and may be <code>null</code>.
	 * @param systemId the system identifier associated with the entity; optional and may be <code>null</code>.
	 * @return a new <code>XmlEntity</code> instance with the specified name, and public and system identifiers.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlEntity createEntity(String name, String publicId, String systemId) {
		XmlEntity entity = createEntity(name);
		entity.setPublicId(publicId);
		entity.setSystemId(systemId);
		return entity;
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlEntity XmlEntity} class.
	 * 
	 * @param name the name of the entity.
	 * @param publicId the public identifier associated with the entity; optional and may be <code>null</code>.
	 * @param systemId the system identifier associated with the entity; optional and may be <code>null</code>.
	 * @param notationName for unparsed entities, the name of the notation; <code>null</code> for parsed entities.
	 * @return a new <code>XmlEntity</code> instance with the specified name, public and system identifiers, 
	 * 	and notation name.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the entity name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlEntity createEntity(String name, String publicId, String systemId, String notationName) {
		XmlEntity entity = createEntity(name, publicId, systemId);
		entity.setNotationName(notationName);
		return entity;
	}
}