/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * A globally accessible class that allows for storing and retrieval of DOM 
 * implementations through a static interface.
 * 
 * @author K.A., copyright (c) 2008
 */
public final class XmlDomImplementationRegistry {
	/**
	 * Contains all the registered <code>XmlDomImplementation</code> instances.
	 */
	private static List<XmlDomImplementation> domImplementations = new ArrayList<XmlDomImplementation>();
	
	/**
	 * Non-instantiable.
	 */
	private XmlDomImplementationRegistry() {
	}
	
	static { // Automatically register the default XmlDomImplementation.
		register(new XmlDomImplementation());
	}
	
	/**
	 * Allows for the registration of custom DOM implementations which can later 
	 * be retrieved through the <code>getDomImplementation()</code> method.
	 * 
	 * @param domImpl an object deriving from {@link proj78.xml.XmlDomImplementation XmlDomImplementation}.
	 */
	public static void register(XmlDomImplementation domImpl) {
		domImplementations.add(domImpl);
	}
	
	/**
	 * Returns the DOM implementation that has the desired feature.
	 * 
	 * @param feature the required feature. Currently only "XML" is supported 
	 * 	by default.
	 * @param version the version number of that feature. For the "XML" feature, 
	 * 	only the version string "1.0" is supported by default.
	 * @return an {@link proj78.xml.XmlDomImplementation XmlDomImplementation} object or
	 * 	<code>null</code>, if one supporting the required feature could not be obtained.
	 */
	public static XmlDomImplementation getDomImplementation(String feature, String version) {
		for (XmlDomImplementation domImpl : domImplementations) {
			if (domImpl != null && domImpl.hasFeature(feature, version)) {
				return domImpl;
			}
		}
		return null;
	}
}