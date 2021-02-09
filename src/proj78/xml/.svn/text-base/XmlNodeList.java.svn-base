/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an ordered collection of nodes.
 * 
 * @author K.A., copyright (c) 2008
 */
public class XmlNodeList implements Iterable<XmlNode> {
	/**
	 * An indexed collection (i.e., list) containing the nodes.
	 */
	private List<XmlNode> indexedItems;
	
	/**
	 * Internal <code>XmlNodeList</code> constructor.
	 */
	/* internal */ XmlNodeList() {
		indexedItems = new ArrayList<XmlNode>();
	}
	
	/**
	 * Returns the number of nodes in this node list.
	 * 
	 * @return the length of this node list.
	 */
	public int length() {
		return indexedItems.size();
	}
	
	/**
	 * Retrieves the node at the specified position in this node list.
	 * 
	 * @param index the index of the node to retrieve.
	 * @return the node at the specified index or <code>null</code>, if that 
	 * 	index is invalid.
	 */
	public XmlNode item(int index) {
		try { 
			return indexedItems.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * Returns an iterator over the nodes in this node list.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<XmlNode> iterator() {
		return Collections.unmodifiableList(indexedItems).iterator();
	}
	
	/**
	 * Adds a new node to the end of this node list.
	 * 
	 * @param item the node to be added.
	 * @return the node added.
	 */
	/* internal */ XmlNode appendItem(XmlNode item) {
		if (indexedItems.add(item)) {
			return item;
		}
		return null;
	}
	
	/**
	 * Inserts a new node at the specified position in this node list.
	 * 
	 * @param index the index at which to insert the node.
	 * @param item the node to be inserted.
	 * @return the node inserted. 
	 * @throws XmlException <em>INDEX_SIZE_ERR</em>: if the index was out of bounds.
	 */
	/* internal */ XmlNode insertItem(int index, XmlNode item) {
		try {
			indexedItems.add(index, item);
			return item;
		} catch (IndexOutOfBoundsException ex) {
			throw new XmlException("An out of bounds index was specified.", XmlException.INDEX_SIZE_ERR);
		}
	}
	
	/**
	 * Replaces the node at the specified position in this node list with 
	 * another one.
	 * 
	 * @param index the index of the node to replace.
	 * @param item the node to replace with.
	 * @return the node that was replaced.
	 * @throws XmlException <em>INDEX_SIZE_ERR</em>: if the index was out of bounds.
	 */
	/* internal */ XmlNode replaceItem(int index, XmlNode item) {
		try {
			return indexedItems.set(index, item);
		} catch (IndexOutOfBoundsException ex) {
			throw new XmlException("An out of bounds index was specified.", XmlException.INDEX_SIZE_ERR);
		}
	}
	
	/**
	 * Removes the specified node from this node list.
	 * 
	 * @param item the node to be removed.
	 * @return the node removed or <code>null</code>, if that node was not 
	 * 	found in this node list.
	 */
	/* internal */ XmlNode removeItem(XmlNode item) {
		if (indexedItems.remove(item)) {
			return item;
		}
		return null;
	}
	
	/**
	 * Finds the index of the specified node within this node list.
	 * 
	 * @param item the node to search for.
	 * @return the index of that node or -1, if it could not be found.
	 */
	/* internal */ int indexOf(XmlNode item) {
		return indexedItems.indexOf(item);
	}
}