/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents a notation declared in the DTD.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlNotation extends XmlLeafNode {
	/**
	 * The declared name of this notation.
	 */
	private String name;
	
	/**
	 * The public identifier of this notation.
	 */
	private String publicId;
	
	/**
	 * The system identifier of this notation.
	 */
	private String systemId;
	
	/**
	 * Internal <code>XmlNotation</code> constructor.
	 * 
	 * @param name the notation name.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 * @see proj78.xml.XmlDocumentType#createNotation(java.lang.String)
	 */
	/* internal */ XmlNotation(String name) {
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
		return NOTATION_NODE;
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
	 * Returns the public identifier of this notation.
	 * 
	 * @return the public identifier of this notation or <code>null</code>, 
	 * 	if one was not specified.
	 */
	public String getPublicId() {
		return publicId;
	}
	
	/* internal */ void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	
	/**
	 * Returns the system identifier of this notation.
	 * 
	 * @return the system identifier of this notation or <code>null</code>, 
	 * 	if one was not specified.
	 */
	public String getSystemId() {
		return systemId;
	}
	
	/* internal */ void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		throw new XmlException("The clone operation is not supported for this type of node.", XmlException.NOT_SUPPORTED_ERR);
	}
}