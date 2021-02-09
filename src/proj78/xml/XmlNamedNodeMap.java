/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents a collection of nodes that can be accessed by name.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlNamedNodeMap implements Iterable<XmlNode> {
	/**
	 * An associative collection (i.e., map) containing the nodes.
	 */
	private Map<String, XmlNode> namedItems;
	
	/**
	 * Internal <code>XmlNamedNodeMap</code> constructor.
	 */
	/* internal */ XmlNamedNodeMap() {
		namedItems = new HashMap<String, XmlNode>();
	}
	
	/**
	 * Returns the number of nodes in this node map.
	 * 
	 * @return the length of this node map.
	 */
	public int length() {
		return namedItems.size();
	}
	
	/**
	 * Returns the node at the specified position in this node map.
	 * 
	 * @param index the index of the node to retrieve.
	 * @return the node at the specified index or <code>null</code>, if that 
	 * 	index is invalid.
	 */
	public XmlNode item(int index) {
		int i = 0;
		for (String name : namedItems.keySet()) {
			if (i++ == index) {
				return namedItems.get(name);
			}
		}
		return null;
	}
	
	/**
	 * Retrieves a node by its node name.
	 * 
	 * @param name the name of the node to retrieve.
	 * @return the node with the specified name or <code>null</code>, if no 
	 * 	such node was found.
	 */
	public XmlNode getNamedItem(String name) {
		return namedItems.get(name);
	}
	
	/**
	 * Adds a new node using its node name.
	 * 
	 * @param item the node to be added.
	 * @return if a node with the same name as that of the node being added 
	 * 	was already present in this node map, it is returned; otherwise 
	 * 	<code>null</code> is returned.
	 */
	public XmlNode setNamedItem(XmlNode item) {
		return namedItems.put(item.getNodeName(), item);
	}
	
	/**
	 * Removes a node using its node name.
	 * 
	 * @param name the name of the node to be removed.
	 * @return the node removed or <code>null</code>, if a node with the 
	 * 	specified name was not found.
	 */ 
	public XmlNode removeNamedItem(String name) {
		return namedItems.remove(name);
	}
	
	/**
	 * Returns an iterator over the nodes in this node map.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<XmlNode> iterator() {
		return Collections.unmodifiableCollection(namedItems.values()).iterator();
	}
}