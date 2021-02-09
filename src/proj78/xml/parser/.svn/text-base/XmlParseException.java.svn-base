/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser;

import proj78.xml.XmlException;

/**
 * The exception that is thrown to indicate a parse error.
 * 
 * @author K.A., copyright (c) 2008
 */
@SuppressWarnings("serial")
public class XmlParseException extends XmlException {
	/**
	 * (non-DOM)
	 * Badly formed XML was supplied to the parser.
	 */
	public static final int MALFORMED_XML_ERR = 101;
	
	/**
	 * Constructs a new <code>XmlParseException</code> given the detail message.
	 * 
	 * @param errorMessage a message detailing the error that occured.
	 */
	public XmlParseException(String errorMessage) {
		super(String.format("MALFORMED_XML_ERR: %s", errorMessage), MALFORMED_XML_ERR);
	}
}