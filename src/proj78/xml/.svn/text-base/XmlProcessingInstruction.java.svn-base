/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Represents a processing instruction (PI) in the document.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlProcessingInstruction extends XmlLeafNode {
	/**
	 * The instruction target.
	 */
	private String target;
	
	/**
	 * The instruction data.
	 */
	private String data;
	
	/**
	 * Internal <code>XmlProcessingInstruction</code> constructor.
	 * 
	 * @param target the instruction target.
	 * @param data the instruction data; optional and may be <code>null</code>.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the instruction target is empty, 
	 * 	or contains an invalid or illegal XML character.
	 * @see proj78.xml.XmlDocument#createProcessingInstruction(java.lang.String, java.lang.String)
	 */
	/* internal */ XmlProcessingInstruction(String target, String data) {
		this(target);
		setData(data);
	}
	
	/**
	 * Protected <code>XmlProcessingInstruction</code> constructor that accepts 
	 * only the instruction target. Meant for calling from the (read-only) 
	 * subclass {@link proj78.xml.XmlDeclaration XmlDeclaration}.
	 * 
	 * @param target the instruction target.
	 * @throws XmlException <em>INVALID_CHARACTER_ERR</em>: if the instruction target is empty, 
	 * 	or contains an invalid or illegal XML character.
	 */
	protected XmlProcessingInstruction(String target) {
		if (!XmlUtility.isValidName(target)) {
			throw new XmlException("An invalid or illegal XML character was specified.", XmlException.INVALID_CHARACTER_ERR);
		}
		this.target = target;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeType()
	 */
	public int getNodeType() {
		return PROCESSING_INSTRUCTION_NODE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeName()
	 */
	public String getNodeName() {
		return getTarget();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#getNodeValue()
	 */
	public String getNodeValue() {
		return getData();
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#setNodeValue(java.lang.String)
	 */
	public void setNodeValue(String nodeValue) {
		setData(nodeValue);
	}
	
	/**
	 * Returns the instruction target.
	 * 
	 * @return the instruction target.
	 * @see proj78.xml.XmlProcessingInstruction#getNodeName()
	 */
	public String getTarget() {
		return target;
	}
	
	/**
	 * Returns the instruction data.
	 * 
	 * @return a string containing the instruction data.
	 * @see proj78.xml.XmlProcessingInstruction#getNodeValue()
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * Sets the instruction data.
	 * 
	 * @param data a string containing the instruction data.
	 * @see proj78.xml.XmlProcessingInstruction#setNodeValue(java.lang.String)
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	/*
	 * (non-Javadoc)
	 * @see proj78.xml.XmlNode#cloneNode(boolean)
	 */
	public XmlNode cloneNode(boolean deep) {
		return getOwnerDocument().createProcessingInstruction(getTarget(), getData());
	}
}