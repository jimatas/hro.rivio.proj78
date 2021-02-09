/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.serializer;

/**
 * Provides the static <code>createSerializer()</code> method (and overload), 
 * through which the application can obtain an XML serializer.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlSerializerFactory {
	/**
	 * Non-instantiable.
	 */
	private XmlSerializerFactory() {
	}
	
	/**
	 * Creates a new <code>XmlSerializer</code> object.
	 * 
	 * @return a standard (i.e., non-formatting) XML serializer.
	 */
	public static XmlSerializer createSerializer() {
		return new XmlSerializer();
	}
	
	/**
	 * Creates a new <code>XmlSerializer</code> object.
	 * 
	 * @param formatting specifies whether the XML serializer returned is to apply
	 *  formatting (i.e., produce "pretty printed" output).
	 * @return either a standard (i.e., non-formatting) or formatting XML serializer,
	 * 	depending on the value of the boolean argument.
	 */
	public static XmlSerializer createSerializer(boolean formatting) {
		if (formatting) {
			return new XmlFormattingSerializer();
		}
		return createSerializer();
	}
}