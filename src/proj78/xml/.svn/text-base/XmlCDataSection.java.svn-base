/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents a CDATA-section in the document.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlCDataSection extends XmlText {
	/**
	 * Internal <code>XmlCDataSection</code> constructor.
	 * 
	 * @param data the character data of this CDATA-section.
	 * @see proj78.xml.XmlDocument#createCDataSection(java.lang.String)
	 */
	/* internal */ XmlCDataSection(String data) {
		super(data);
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlText#getNodeType()
	 */
	public int getNodeType() {
		return CDATA_SECTION_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlText#getNodeName()
	 */
	public String getNodeName() {
		return "#cdata-section";
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlText#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		return getOwnerDocument().createCDataSection(getData());
	}
}