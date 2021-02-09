/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * A processing instruction that represents the XML declaration of the document. 
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlDeclaration extends XmlProcessingInstruction {
	/**
	 * The XML version in use; always the string "1.0".
	 */
	private String version;
	
	/**
	 * The character encoding used in the document; <code>null</code> if not 
	 * specified.
	 */
	private String encoding;
	
	/**
	 * The standalone designation of the document; either "yes", "no" or 
	 * <code>null</code> (not specified).
	 */
	private String standalone;
	
	/**
	 * Internal <code>XmlDeclaration</code> constructor.
	 * 
	 * @param version the declared XML version; always the string "1.0".
	 * @param encoding the declared XML encoding; optional and may be <code>null</code>.
	 * @param standalone the standalone designation of the document; optional and may 
	 * 	be <code>null</code>.
	 * @throws XmlException <em>NOT_SUPPORTED_ERR</em>: if the version string is something other than "1.0" 
	 *	or if an unsupported character encoding is specified.<br />
	 *	<em>INVALID_CHARACTER_ERR</em>: if the standalone attribute, if non-null, contains a value other than "yes" or "no".
	 * @see proj78.xml.XmlDomImplementation#createXmlDeclaration(java.lang.String, java.lang.String, java.lang.String)
	 */
	/* internal */ XmlDeclaration(String version, String encoding, String standalone) {
		super("xml");
		if (version != null && !version.equals("1.0")) {
			throw new XmlException("An unsupported XML version was specified.", XmlException.NOT_SUPPORTED_ERR);
		}
		this.version = version;
		if (encoding != null && !isSupportedEncoding(encoding.toUpperCase())) {
			throw new XmlException("An unsupported character encoding was specified.", XmlException.NOT_SUPPORTED_ERR);
		}
		this.encoding = encoding;
		if (standalone != null && !(standalone.equals("yes") || standalone.equals("no"))) {
			throw new XmlException("The allowed values for the standalone attribute are 'yes' and 'no'.", XmlException.INVALID_CHARACTER_ERR);
		}
		this.standalone = standalone;
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
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlProcessingInstruction#getData()
	 */
	public String getData() {
		String data = String.format("version=\"%s\"", getVersion());
		if (getEncoding() != null) {
			data += String.format(" encoding=\"%s\"", getEncoding());
			if (getStandalone() != null) {
				data += String.format(" standalone=\"%s\"", getStandalone());
			}
		}
		return data;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlProcessingInstruction#setData(java.lang.String)
	 */
	public void setData(String data) {
		throw new XmlException("Attempted to modify a node that is read-only.", XmlException.NO_MODIFICATION_ALLOWED_ERR);
	}
	
	/**
	 * Returns the value of the XML version-attribute.
	 * 
	 * @return always the string "1.0".
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Returns the value of the XML encoding-attribute, if one was declared.
	 * 
	 * @return the declared encoding or <code>null</code>, if no encoding was 
	 * 	specified.
	 */
	public String getEncoding() {
		return encoding;
	}
	
	/**
	 * Returns the value of the XML standalone-attribute, if one was declared.
	 * 
	 * @return either the string "yes" or "no"; <code>null</code> if a standalone 
	 * 	designation was not specified.
	 */
	public String getStandalone() {
		return standalone;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlProcessingInstruction#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		XmlDomImplementation domImpl = XmlDomImplementationRegistry.getDomImplementation("XML", "1.0");
		return domImpl.createXmlDeclaration(getVersion(), getEncoding(), getStandalone());	
	}
	
	/**
	 * Determines whether the specified string contains the MIME name or alias of 
	 * a supported character encoding.
	 * 
	 * @param encoding the character encoding to test.
	 * @return <code>true</code> if the specified character encoding is supported, 
	 * 	<code>false</code> otherwise.
	 */
	private static boolean isSupportedEncoding(String encoding) {
		return // Latin 1:
			encoding.equals("ISO_8859-1:1987")
				|| encoding.equals("ISO_8859-1")
				|| encoding.equals("ISO-8859-1")
				|| encoding.equals("ISO-IR-100")
				|| encoding.equals("CSISOLATIN1")
				|| encoding.equals("L1")
				|| encoding.equals("LATIN1")
				|| encoding.equals("IBM819")
				|| encoding.equals("CP819")
				|| encoding.equals("ISO-8859-1")
				// Unicode:
				|| encoding.equals("UTF8")
				|| encoding.equals("UTF-8");
	}
}