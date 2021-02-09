/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents a light-weight document which is used for holding portions of an
 * existing document's tree.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlDocumentFragment extends XmlParentNode {
	/**
	 * Internal <code>XmlDocumentFragment</code> constructor.
	 * 
	 * @see proj78.xml.XmlDocument#createDocumentFragment()
	 */
	/* internal */ XmlDocumentFragment() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return DOCUMENT_FRAGMENT_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return "#document-fragment";
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setParentNode(proj78.xml.XmlParentNode)
	 */
	/* internal */ void setParentNode(XmlParentNode parentNode) {
		throw new XmlException("Attempted to set a parent on a node that does not allow for one.", XmlException.HIERARCHY_REQUEST_ERR);
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
		XmlDocumentFragment docFrag = getOwnerDocument().createDocumentFragment();
		if (deep) {
			copyChildrenInto(docFrag);
		}
		return docFrag;
	}
}