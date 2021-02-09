/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser;

/**
 * Provides a single static <code>createParser()</code> method, through which 
 * the application can obtain an XML parser.
 * 
 * @author K.A., copyright (c) 2008
 */
public final class XmlParserFactory {
	/**
	 * Non-instantiable.
	 */
	private XmlParserFactory() {
	}
	
	/**
	 * Creates a new <code>XmlParser</code> object.
	 * 
	 * @return an XML parser for use by the application.
	 */
	public static XmlParser createParser() {
		return new XmlParserImpl();
	}
}