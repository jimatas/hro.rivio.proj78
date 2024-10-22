/*
 * Proj7-8:  DOM Level 1 implementation
 * 
 * K. Atas <0712593@student.hro.nl>
 * H.J. Bos <0779473@student.hro.nl>
 */

package proj78.xml.parser2;

import java.util.Map;

/**
 * Allows for implementing classes to register themselves with an object that acts as a source of 
 * parse events (typically a parser), and be notified of the events that are generated by it.
 * 
 * <p>The parse events that notifications are received of are:
 * <ul>
 * 	<li>XML declaration event</li>
 * 	<li>DOCTYPE declaration event</li>
 * 	<li>Tag start and end events</li>
 * 	<li>Processing instruction event</li>
 * 	<li>Characters event</li>
 * 	<li>CDATA-section event and </li>
 * 	<li>Comment event.</li>
 * </ul>
 * 
 * @author K.A., copyright (c) 2008
 */
public interface XmlParseEventListener {
	/**
	 * Called when the XML declaration is parsed.
	 * 
	 * @param version the XML version in use.
	 * @param encoding the character encoding in use; an optional attribute that may be <code>null</code>.
	 * @param standalone the standalone designation; either "yes", "no" or <code>null</code> (not specified).
	 */
	void onXmlDeclaration(String version, String encoding, String standalone);
	
	/**
	 * Called when the DOCTYPE is parsed.
	 * 
	 * @param dtdName the name immediately following the DOCTYPE keyword.
	 * @param publicId the external subset's public identifier which is optional and may be <code>null</code>.
	 * @param systemId the external subset's system identifier which is optional and may be <code>null</code>.
	 * @param internalSubset a string containing the internal subset; <code>null</code> if not present.
	 */
	void onDoctypeDeclaration(String dtdName, String publicId, String systemId, String internalSubset);
	
	/**
	 * Called when the starting tag of an element is parsed.
	 * 
	 * @param tagName the tag's name.
	 * @param attrs a possibly empty map containing the attributes that were present.
	 */
	void onTagStart(String tagName, Map<String, String> attrs);
	
	/**
	 * Called when the ending tag of an element is parsed.
	 * 
	 * @param tagName the tag's name.
	 */
	void onTagEnd(String tagName);
	
	/**
	 * Called when a processing instruction is parsed.
	 * 
	 * @param target the instruction target.
	 * @param data the instruction data; <code>null</code> if not present.
	 */
	void onProcessingInstruction(String target, String data);
	
	/**
	 * Called when text (character data other than CDATA) is parsed.
	 * 
	 * @param chars the characters that were parsed.
	 */
	void onCharacters(String chars);
	
	/**
	 * Called when a CDATA-section is parsed.
	 * 
	 * @param content the content of the CDATA-section.
	 */
	void onCDataSection(String content);
	
	/**
	 * Called when a comment is parsed.
	 * 
	 * @param content the content of the comment (i.e., everything between <code>&lt;--</code> and <code>--&gt;</code>).
	 */
	void onComment(String content);
}