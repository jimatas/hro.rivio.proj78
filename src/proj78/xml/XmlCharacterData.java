/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

/**
 * Extends the {@link proj78.xml.XmlNode XmlNode} class with a set of methods for manipulating text.
 * 
 * @author K.A., copyright (c) 2008
 */
public abstract class XmlCharacterData extends XmlLeafNode {
	/**
	 * The character data of this node.
	 */
	private String data;
	
	/**
	 * Protected <code>XmlCharacterData</code> constructor.
	 * 
	 * @param data the character data of this node.
	 */
	protected XmlCharacterData(String data) {
		setData(data);
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
	 * Returns the character data of this node.
	 * 
	 * @return the character data of this node or <code>null</code> (or the empty 
	 * 	string), for nodes that do not contain character data.
	 * @see proj78.xml.XmlCharacterData#getNodeValue()
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * Replaces the character data of this node.
	 * 
	 * @param data the character data of this node.
	 * @see proj78.xml.XmlCharacterData#setNodeValue(java.lang.String)
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	/**
	 * Returns the length of the data, in characters. 
	 * 
	 * @return the number of characters.
	 */
	public int length() {
		return getData().length();
	}
	
	/**
	 * Extracts a range of characters from this node.
	 * 
	 * @param offset the position of the first character to extract.
	 * @param length the number of characters to extract.
	 * @return the extracted range.
	 * @throws XmlException <em>INDEX_SIZE_ERR</em>: if the specified range of characters was invalid.
	 */
	public String substringData(int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > length()) {
			throw new XmlException(String.format("An invalid character range was specified (offset: %d, length: %d).", offset, length), XmlException.INDEX_SIZE_ERR);
		}
		return getData().substring(offset, offset + length);
	}
	
	/**
	 * Appends a string to the end of this node.
	 * 
	 * @param data the string to be appended.
	 */
	public void appendData(String data) {
		setData(getData() + data);
	}
	
	/**
	 * Inserts a string at the specified position into this node.
	 * 
	 * @param offset the character position at which to insert the string.
	 * @param data the string to be inserted.
	 * @throws XmlException <em>INDEX_SIZE_ERR</em>: if the character offset was out of bounds.
	 */
	public void insertData(int offset, String data) {
		if (offset < 0 || offset > length()) {
			throw new XmlException("An out of bounds character offset was specified.", XmlException.INDEX_SIZE_ERR);
		}
		setData(getData().substring(0, offset) + data + getData().substring(offset));
	}
	
	/**
	 * Removes a range of characters from this node.
	 * 
	 * @param offset the position of the first character to be removed.
	 * @param length the number of characters to be removed.
	 * @throws XmlException <em>INDEX_SIZE_ERR</em>: if the specified range of characters was invalid.
	 */
	public void deleteData(int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > length()) {
			throw new XmlException(String.format("An invalid character range was specified (offset: %d, length: %d).", offset, length), XmlException.INDEX_SIZE_ERR);
		}
		setData(getData().substring(0, offset) + getData().substring(offset + length));
	}
	
	/**
	 * Replaces a range of characters in this node with the specified string.
	 * 
	 * @param offset the position of the first character to be replaced.
	 * @param length the number of characters to be replaced.
	 * @param data the string to replace with.
	 * @throws XmlException <em>INDEX_SIZE_ERR</em>: if the specified range of characters was invalid.
	 */
	public void replaceData(int offset, int length, String data) {
		if (offset < 0 || length < 0 || offset + length > length()) {
			throw new XmlException(String.format("An invalid character range was specified (offset: %d, length: %d).", offset, length), XmlException.INDEX_SIZE_ERR);
		}
		setData(getData().substring(0, offset) + data + getData().substring(offset + length));
	}
}