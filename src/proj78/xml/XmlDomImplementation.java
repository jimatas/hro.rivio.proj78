/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Provides methods that are independent of any particular instance of the DOM.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlDomImplementation {
	/**
	 * Internal <code>XmlDomImplementation</code> constructor.
	 * 
	 * @see proj78.xml.XmlDomImplementationRegistry#getDomImplementation(java.lang.String, java.lang.String)
	 */
	/* internal */ XmlDomImplementation() {
	}
	
	/**
	 * (non-DOM) 
	 * Creates a new instance of the {@link proj78.xml.XmlDocument XmlDocument} class.
	 * 
	 * @return a new <code>XmlDocument</code> instance.
	 * @see proj78.xml.XmlDomImplementation#createDocument(java.lang.String, proj78.xml.XmlDocumentType)
	 */
	public XmlDocument createDocument() {
		return new XmlDocument();
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlDocument XmlDocument} class.
	 * 
	 * @param tagName the tag name of the root element that will be created together with the document.
	 * @param doctype the type of document to be created; optional and may be <code>null</code>.
	 * @return a new <code>XmlDocument</code> instance with the specified root element and doctype.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the tag name is empty, or contains an invalid or illegal XML character.<br /> 
	 * 	<em>WRONG_DOCUMENT_ERR</em>: if the specified doctype node is already associated with another document.
	 * @since DOM Level 2
	 */
	public XmlDocument createDocument(String tagName, XmlDocumentType doctype) {
		XmlDocument doc = createDocument();
		if (doctype != null) {
			doc.appendChild(doctype);
		}
		doc.appendChild(doc.createElement(tagName));
		return doc;
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlDeclaration XmlDeclaration} class.
	 * 
	 * @param version the version of XML in use; always the string "1.0".
	 * @return a new <code>XmlDeclaration</code> instance with the specified version.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the version string is something other than "1.0".
	 */
	public XmlDeclaration createXmlDeclaration(String version) {
		return createXmlDeclaration(version, null);
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlDeclaration XmlDeclaration} class.
	 * 
	 * @param version the version of XML in use; always the string "1.0".
	 * @param encoding the character encoding in use.
	 * @return a new <code>XmlDeclaration</code> instance with the specified version and character encoding.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the version string is something other than "1.0"; 
	 * 	or an unsupported character encoding is specified.
	 */
	public XmlDeclaration createXmlDeclaration(String version, String encoding) {
		return createXmlDeclaration(version, encoding, null);
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlDeclaration XmlDeclaration} class.
	 * 
	 * @param version the version of XML in use; always the string "1.0".
	 * @param encoding the character encoding in use.
	 * @param standalone the standalone designation of the document; either "yes" or "no".
	 * @return a new <code>XmlDeclaration</code> instance with the specified version, character encoding,
	 * 	and standalone designation.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the version string is something other than "1.0"; 
	 * 	or an unsupported character encoding is specified.<br />
	 *	<em>INVALID_CHARACTER_ERR</em>: if the standalone attribute contains a value other than "yes" or "no".
	 */
	public XmlDeclaration createXmlDeclaration(String version, String encoding, String standalone) {
		return new XmlDeclaration(version, encoding, standalone);
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlDocumentType XmlDocumentType} class.
	 * 
	 * @param dtdName the name immediately following the DOCTYPE keyword.
	 * @return a new <code>XmlDocumentType</code> instance with the specified name.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlDocumentType createDocumentType(String dtdName) {
		return new XmlDocumentType(dtdName);
	}
	
	/**
	 * Creates a new instance of the {@link proj78.xml.XmlDocumentType XmlDocumentType} class.
	 * 
	 * @param dtdName the name immediately following the DOCTYPE keyword.
	 * @param publicId the public identifier of the external subset; optional and may be <code>null</code>.
	 * @param systemId the system identifier of the external subset; optional and may be <code>null</code>.
	 * @return a new <code>XmlDocumentType</code> instance with the specified name, and public and system identifiers.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 * @since DOM Level 2
	 */
	public XmlDocumentType createDocumentType(String dtdName, String publicId, String systemId) {
		XmlDocumentType doctype = createDocumentType(dtdName);
		doctype.setPublicId(publicId);
		doctype.setSystemId(systemId);
		return doctype;
	}
	
	/**
	 * (non-DOM)
	 * Creates a new instance of the {@link proj78.xml.XmlDocumentType XmlDocumentType} class.
	 * 
	 * @param dtdName the name immediately following the DOCTYPE keyword.
	 * @param publicId the public identifier of the external subset; optional and may be <code>null</code>.
	 * @param systemId the system identifier of the external subset; optional and may be <code>null</code>.
	 * @param internalSubset a string containing the internal subset; optional and may be <code>null</code>.
	 * @return a new <code>XmlDocumentType</code> instance with the specified name, public and system identifers,
	 * 	and internal subset.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the specified name is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	public XmlDocumentType createDocumentType(String dtdName, String publicId, String systemId, String internalSubset) {
		XmlDocumentType doctype = createDocumentType(dtdName, publicId, systemId);
		doctype.setInternalSubset(internalSubset);
		return doctype;
	}
	
	/**
	 * Determines whether the DOM supports a given feature.
	 * 
	 * @param feature the name of the feature to test for.
	 * @param version the version number of that feature. If a version number is not specified 
	 * 	(<code>null</code>), any supported version of that feature will return <code>true</code>.
	 * @return <code>true</code> if the feature is supported, <code>false</code> otherwise.
	 */
	public boolean hasFeature(String feature, String version) {
		return feature.equalsIgnoreCase("XML") 
				&& (version == null || version.equals("1.0"));
	}
}